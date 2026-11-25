package net.aoubbad.clubsawaid.service;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import net.aoubbad.clubsawaid.dto.ActivationRequest;
import net.aoubbad.clubsawaid.dto.ChangePasswordRequest;
import net.aoubbad.clubsawaid.dto.MembreResponse;
import net.aoubbad.clubsawaid.dto.MembreUpdateRequest;
import net.aoubbad.clubsawaid.dto.RoleChangeRequest;
import net.aoubbad.clubsawaid.entity.Membre;
import net.aoubbad.clubsawaid.entity.enums.Role;
import net.aoubbad.clubsawaid.repository.MembreRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MembreService {

    private final MembreRepository membreRepository;
    private final PasswordEncoder passwordEncoder;

    public List<MembreResponse> getTousMembres() {
        return membreRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<MembreResponse> getMembresActifs() {
        return membreRepository.findByActifTrue().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<MembreResponse> getMembresParRole(Role role) {
        return membreRepository.findByActifTrueAndRole(role).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public MembreResponse getMembreById(Long id) {
        return toResponse(membreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Membre introuvable avec l'id : " + id)));
    }

    public MembreResponse getMembreByEmail(String email) {
        return toResponse(membreRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Membre introuvable avec l'email : " + email)));
    }

    public List<MembreResponse> rechercherMembres(String keyword) {
        return membreRepository.searchByNomOrPrenom(keyword).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public MembreResponse mettreAJourProfil(Long id, @NotNull MembreUpdateRequest request) {
        Membre membre = membreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Membre introuvable avec l'id : " + id));
        membre.setNom(request.getNom());
        membre.setPrenom(request.getPrenom());
        membre.setTelephone(request.getTelephone());
        membre.setFiliere(request.getFiliere());
        membre.setPromotion(request.getPromotion());
        if (request.getPhotoUrl() != null && !request.getPhotoUrl().isBlank()) {
            membre.setPhotoUrl(request.getPhotoUrl());
        }
        return toResponse(membreRepository.save(membre));
    }

    @Transactional
    public void changerMotDePasse(Long id, @NotNull ChangePasswordRequest request) {
        Membre membre = membreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Membre introuvable avec l'id : " + id));
        if (!passwordEncoder.matches(request.getAncienMotDePasse(), membre.getMotDePasse())) {
            throw new IllegalArgumentException("L'ancien mot de passe est incorrect.");
        }
        membre.setMotDePasse(passwordEncoder.encode(request.getNouveauMotDePasse()));
        membreRepository.save(membre);
    }

    @Transactional
    public void changerRole(Long id, @NotNull RoleChangeRequest request) {
        Membre membre = membreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Membre introuvable avec l'id : " + id));
        membre.setRole(request.getRole());
        membreRepository.save(membre);
    }

    @Transactional
    public void activerOuDesactiverMembre(Long id, @NotNull ActivationRequest request) {
        Membre membre = membreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Membre introuvable avec l'id : " + id));
        membre.setActif(request.getActif());
        membreRepository.save(membre);
    }

    @Transactional
    public void supprimerMembre(Long id) {
        if (!membreRepository.existsById(id)) {
            throw new IllegalArgumentException("Membre introuvable avec l'id : " + id);
        }
        membreRepository.deleteById(id);
    }

    public long countMembresActifs() {
        return membreRepository.countByActifTrue();
    }

    public long countParRole(Role role) {
        return membreRepository.countByRole(role);
    }

    private MembreResponse toResponse(Membre membre) {
        return MembreResponse.builder()
                .id(membre.getId())
                .nom(membre.getNom())
                .prenom(membre.getPrenom())
                .email(membre.getEmail())
                .telephone(membre.getTelephone())
                .filiere(membre.getFiliere())
                .promotion(membre.getPromotion())
                .photoUrl(membre.getPhotoUrl())
                .role(membre.getRole())
                .dateInscription(membre.getDateInscription())
                .actif(membre.isActif())
                .emailVerifie(membre.isEmailVerifie())
                .build();
    }

    public MembreResponse getMe() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof Membre membre) {
            return toResponse(membre);
        }

        throw new IllegalStateException("Utilisateur non connecté");
    }
}
