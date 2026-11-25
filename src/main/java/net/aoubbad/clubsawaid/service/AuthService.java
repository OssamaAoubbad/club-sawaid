package net.aoubbad.clubsawaid.service;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import net.aoubbad.clubsawaid.dto.*;
import net.aoubbad.clubsawaid.entity.EmailVerificationToken;
import net.aoubbad.clubsawaid.entity.Membre;
import net.aoubbad.clubsawaid.entity.PasswordResetToken;
import net.aoubbad.clubsawaid.entity.enums.Role;
import net.aoubbad.clubsawaid.repository.EmailVerificationTokenRepository;
import net.aoubbad.clubsawaid.repository.MembreRepository;
import net.aoubbad.clubsawaid.repository.PasswordResetTokenRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MembreRepository membreRepository;
    private final EmailVerificationTokenRepository emailTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    @Transactional
    public String inscrire(@NotNull AuthRegisterRequest request) {
        if (membreRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Cet email est deje utilis�.");
        }

        Membre membre = Membre.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .motDePasse(passwordEncoder.encode(request.getMotDePasse()))
                .filiere(request.getFiliere())
                .promotion(request.getPromotion())
                .role(Role.MEMBRE)
                .dateInscription(LocalDate.now())
                .actif(false)
                .emailVerifie(false)
                .build();

        membreRepository.save(membre);

        EmailVerificationToken token = EmailVerificationToken.creerPour(membre);
        emailTokenRepository.save(token);
        emailService.envoyerEmailVerification(membre.getEmail(), membre.getPrenom(), token.getToken());

        return "Votre compte a ete cree avec succes. Verifiez votre email pour activer votre acces.";
    }

    @Transactional
    public void verifierEmail(@NotNull EmailVerificationRequest request) {
        EmailVerificationToken token = emailTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new IllegalArgumentException("Token de v�rification invalide."));

        if (token.isUtilise()) {
            throw new IllegalStateException("Ce lien de verification a deja ete utilise.");
        }
        if (token.isExpired()) {
            throw new IllegalStateException("Ce lien de verification a expire. Veuillez en demander un nouveau.");
        }

        Membre membre = token.getMembre();
        membre.setEmailVerifie(true);
        membre.setActif(true);
        membreRepository.save(membre);

        token.setUtilise(true);
        emailTokenRepository.save(token);
    }

    @Transactional
    public void renvoyerEmailVerification(@NotNull ResendVerificationRequest request) {
        Membre membre = membreRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Aucun compte trouve avec cet email."));

        if (membre.isEmailVerifie()) {
            throw new IllegalStateException("Cet email est deja vérifie.");
        }

        emailTokenRepository.supprimerParMembreId(membre.getId());
        EmailVerificationToken token = EmailVerificationToken.creerPour(membre);
        emailTokenRepository.save(token);
        emailService.envoyerEmailVerification(membre.getEmail(), membre.getPrenom(), token.getToken());
    }

    public AuthResponse connecter(@NotNull AuthLoginRequest request) {
        Membre membre = membreRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Email ou mot de passe incorrect."));

        if (!membre.isEmailVerifie()) {
            throw new DisabledException("Veuillez verifier votre email avant de vous connecter.");
        }
        if (!membre.isActif()) {
            throw new DisabledException("Votre compte est d�sactiv�. Contactez l'administrateur.");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getMotDePasse())
        );

        String token = jwtService.generateToken(membre);
        return AuthResponse.builder().token(token).build();
    }

    @Transactional
    public void demanderReinitialisationMotDePasse(@NotNull PasswordResetRequest request) {
        Membre membre = membreRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Aucun compte trouve avec cet email."));

        passwordResetTokenRepository.supprimerParMembreId(membre.getId());

        PasswordResetToken token = PasswordResetToken.builder()
                .token(UUID.randomUUID().toString())
                .membre(membre)
                .expiration(LocalDateTime.now().plusHours(1))
                .utilise(false)
                .build();

        passwordResetTokenRepository.save(token);
        emailService.envoyerEmailReinitialisationMotDePasse(membre.getEmail(), membre.getPrenom(), token.getToken());
    }

    @Transactional
    public void reinitialiserMotDePasse(@NotNull PasswordResetConfirmationRequest request) {
        PasswordResetToken token = passwordResetTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new IllegalArgumentException("Token de r�initialisation invalide."));

        if (token.isUtilise()) {
            throw new IllegalStateException("Ce lien a deje ete utilise.");
        }
        if (token.isExpired()) {
            throw new IllegalStateException("Ce lien a expir�. Veuillez en demander un nouveau.");
        }

        Membre membre = token.getMembre();
        membre.setMotDePasse(passwordEncoder.encode(request.getNouveauMotDePasse()));
        membreRepository.save(membre);

        token.setUtilise(true);
        passwordResetTokenRepository.save(token);
    }





}
