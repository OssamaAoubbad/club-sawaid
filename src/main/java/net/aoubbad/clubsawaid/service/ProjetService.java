package net.aoubbad.clubsawaid.service;

import lombok.RequiredArgsConstructor;
import net.aoubbad.clubsawaid.dto.ProjetRequest;
import net.aoubbad.clubsawaid.dto.ProjetResponse;
import net.aoubbad.clubsawaid.dto.ProjetStatutRequest;
import net.aoubbad.clubsawaid.dto.ProjetSummaryDto;
import net.aoubbad.clubsawaid.dto.MembreSummaryDto;
import net.aoubbad.clubsawaid.entity.Membre;
import net.aoubbad.clubsawaid.entity.Projet;
import net.aoubbad.clubsawaid.entity.enums.StatutProjet;
import net.aoubbad.clubsawaid.repository.MembreRepository;
import net.aoubbad.clubsawaid.repository.ProjetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjetService {

    private final ProjetRepository projetRepository;
    private final MembreRepository membreRepository;

    public List<ProjetResponse> getTousProjets() {
        return projetRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ProjetResponse> getProjetsParStatut(StatutProjet statut) {
        return projetRepository.findByStatutOrderByDateDebutDesc(statut).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ProjetResponse> getProjetsEnRecrutement() {
        return projetRepository.findProjetsEnRecrutement().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ProjetResponse getProjetById(Long id) {
        return toResponse(projetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Projet introuvable avec l'id : " + id)));
    }

    public List<ProjetResponse> rechercherProjets(String keyword) {
        return projetRepository.searchByKeyword(keyword).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProjetResponse creerProjet(ProjetRequest request) {
        Membre responsable = membreRepository.findById(request.getResponsableId())
                .orElseThrow(() -> new IllegalArgumentException("Responsable introuvable."));

        Projet projet = Projet.builder()
                .titre(request.getTitre())
                .description(request.getDescription())
                .objectifs(request.getObjectifs())
                .imageUrl(request.getImageUrl())
                .dateDebut(request.getDateDebut())
                .dateFin(request.getDateFin())
                .nombreBenevoles(request.getNombreBenevoles())
                .statut(StatutProjet.PLANIFIE)
                .responsable(responsable)
                .build();

        return toResponse(projetRepository.save(projet));
    }

    @Transactional
    public ProjetResponse mettreAJourProjet(Long id, ProjetRequest request) {
        Projet projet = getProjetEntityById(id);
        projet.setTitre(request.getTitre());
        projet.setDescription(request.getDescription());
        projet.setObjectifs(request.getObjectifs());
        projet.setDateDebut(request.getDateDebut());
        projet.setDateFin(request.getDateFin());
        projet.setNombreBenevoles(request.getNombreBenevoles());
        projet.setStatut(request.getStatut());
        if (request.getImageUrl() != null && !request.getImageUrl().isBlank()) {
            projet.setImageUrl(request.getImageUrl());
        }
        return toResponse(projetRepository.save(projet));
    }

    @Transactional
    public void changerStatut(Long id, ProjetStatutRequest request) {
        Projet projet = getProjetEntityById(id);
        projet.setStatut(request.getStatut());
        projetRepository.save(projet);
    }

    @Transactional
    public void supprimerProjet(Long id) {
        if (!projetRepository.existsById(id)) {
            throw new IllegalArgumentException("Projet introuvable avec l'id : " + id);
        }
        projetRepository.deleteById(id);
    }

    public long countParStatut(StatutProjet statut) {
        return projetRepository.countByStatut(statut);
    }

    private Projet getProjetEntityById(Long id) {
        return projetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Projet introuvable avec l'id : " + id));
    }

    private ProjetResponse toResponse(Projet projet) {
        return ProjetResponse.builder()
                .id(projet.getId())
                .titre(projet.getTitre())
                .description(projet.getDescription())
                .objectifs(projet.getObjectifs())
                .imageUrl(projet.getImageUrl())
                .statut(projet.getStatut())
                .dateDebut(projet.getDateDebut())
                .dateFin(projet.getDateFin())
                .nombreBenevoles(projet.getNombreBenevoles())
                .creeLe(projet.getCreeLe())
                .responsable(toSummary(projet.getResponsable()))
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
}
