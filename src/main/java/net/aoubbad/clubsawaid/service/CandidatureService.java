package net.aoubbad.clubsawaid.service;

import lombok.RequiredArgsConstructor;
import net.aoubbad.clubsawaid.dto.CandidatureRequest;
import net.aoubbad.clubsawaid.dto.CandidatureResponse;
import net.aoubbad.clubsawaid.dto.CandidatureStatutRequest;
import net.aoubbad.clubsawaid.dto.MembreSummaryDto;
import net.aoubbad.clubsawaid.dto.ProjetSummaryDto;
import net.aoubbad.clubsawaid.entity.Candidature;
import net.aoubbad.clubsawaid.entity.Membre;
import net.aoubbad.clubsawaid.entity.Projet;
import net.aoubbad.clubsawaid.entity.enums.StatutCandidature;
import net.aoubbad.clubsawaid.entity.enums.StatutProjet;
import net.aoubbad.clubsawaid.repository.CandidatureRepository;
import net.aoubbad.clubsawaid.repository.MembreRepository;
import net.aoubbad.clubsawaid.repository.ProjetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidatureService {

    private final CandidatureRepository candidatureRepository;
    private final MembreRepository membreRepository;
    private final ProjetRepository projetRepository;
    private final EmailService emailService;

    @Transactional
    public CandidatureResponse postuler(CandidatureRequest request) {
        if (candidatureRepository.existsByMembreIdAndProjetId(request.getMembreId(), request.getProjetId())) {
            throw new IllegalStateException("You already applied to this project.");
        }

        Membre membre = membreRepository.findById(request.getMembreId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found."));
        Projet projet = projetRepository.findById(request.getProjetId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found."));

        if (projet.getStatut() != StatutProjet.EN_COURS &&
                projet.getStatut() != StatutProjet.PLANIFIE) {
            throw new IllegalStateException("This project is no longer recruiting volunteers.");
        }

        Candidature candidature = Candidature.builder()
                .membre(membre)
                .projet(projet)
                .motivation(request.getMotivation())
                .statut(StatutCandidature.EN_ATTENTE)
                .build();

        return toResponse(candidatureRepository.save(candidature));
    }

    public List<CandidatureResponse> getCandidaturesParMembre(Long membreId) {
        return candidatureRepository.findByMembreId(membreId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<CandidatureResponse> getCandidaturesParProjet(Long projetId) {
        return candidatureRepository.findByProjetId(projetId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<CandidatureResponse> getCandidaturesEnAttente() {
        return candidatureRepository.findByStatutOrderByDateCandidatureAsc(StatutCandidature.EN_ATTENTE).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public CandidatureResponse getCandidatureById(Long id) {
        return toResponse(candidatureRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Application not found for id: " + id)));
    }

    @Transactional
    public CandidatureResponse changerStatut(Long id, CandidatureStatutRequest request) {
        Candidature candidature = getCandidatureEntityById(id);

        if (candidature.getStatut() != StatutCandidature.EN_ATTENTE) {
            throw new IllegalStateException("This application has already been processed.");
        }

        candidature.setStatut(request.getStatut());
        candidature.setDateReponse(LocalDateTime.now());
        candidatureRepository.save(candidature);

        Membre membre = candidature.getMembre();
        emailService.envoyerReponseCandidature(
                membre.getEmail(),
                membre.getPrenom(),
                candidature.getProjet().getTitre(),
                request.getStatut() == StatutCandidature.ACCEPTEE
        );

        return toResponse(candidature);
    }

    @Transactional
    public void annulerCandidature(Long candidatureId, Long membreId) {
        Candidature candidature = getCandidatureEntityById(candidatureId);

        if (!candidature.getMembre().getId().equals(membreId)) {
            throw new SecurityException("You are not authorized to cancel this application.");
        }
        if (candidature.getStatut() != StatutCandidature.EN_ATTENTE) {
            throw new IllegalStateException("Only pending applications can be cancelled.");
        }

        candidatureRepository.deleteById(candidatureId);
    }

    public long countCandidaturesEnAttente() {
        return candidatureRepository.countByStatut(StatutCandidature.EN_ATTENTE);
    }

    public long countBenevoleAcceptesParProjet(Long projetId) {
        return candidatureRepository.countBenevoleAcceptesParProjet(projetId);
    }

    private Candidature getCandidatureEntityById(Long id) {
        return candidatureRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Application not found for id: " + id));
    }

    private CandidatureResponse toResponse(Candidature candidature) {
        return CandidatureResponse.builder()
                .id(candidature.getId())
                .motivation(candidature.getMotivation())
                .statut(candidature.getStatut())
                .dateCandidature(candidature.getDateCandidature())
                .dateReponse(candidature.getDateReponse())
                .membre(toSummary(candidature.getMembre()))
                .projet(toSummary(candidature.getProjet()))
                .build();
    }

    private MembreSummaryDto toSummary(Membre membre) {
        if (membre == null) {
            return null;
        }
        return MembreSummaryDto.builder()
                .id(membre.getId())
                .nom(membre.getNom())
                .prenom(membre.getPrenom())
                .email(membre.getEmail())
                .photoUrl(membre.getPhotoUrl())
                .role(membre.getRole())
                .build();
    }

    private ProjetSummaryDto toSummary(Projet projet) {
        if (projet == null) {
            return null;
        }
        return ProjetSummaryDto.builder()
                .id(projet.getId())
                .titre(projet.getTitre())
                .statut(projet.getStatut())
                .build();
    }
}
