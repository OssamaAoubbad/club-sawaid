package net.aoubbad.clubsawaid.service;

import lombok.RequiredArgsConstructor;
import net.aoubbad.clubsawaid.dto.ActualitePublicationRequest;
import net.aoubbad.clubsawaid.dto.ActualiteRequest;
import net.aoubbad.clubsawaid.dto.ActualiteResponse;
import net.aoubbad.clubsawaid.dto.MembreSummaryDto;
import net.aoubbad.clubsawaid.entity.Actualite;
import net.aoubbad.clubsawaid.entity.Membre;
import net.aoubbad.clubsawaid.repository.ActualiteRepository;
import net.aoubbad.clubsawaid.repository.MembreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActualiteService {

    private final ActualiteRepository actualiteRepository;
    private final MembreRepository membreRepository;

    public Page<ActualiteResponse> getActualitesPubliees(Pageable pageable) {
        return actualiteRepository.findByPublieeTrueOrderByDatePublicationDesc(pageable)
                .map(this::toResponse);
    }

    public List<ActualiteResponse> getDernieresActualites(int n) {
        return actualiteRepository.findDernieresActualites(n).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ActualiteResponse getActualiteById(Long id) {
        return toResponse(actualiteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("News item not found for id: " + id)));
    }

    public List<ActualiteResponse> rechercherActualites(String keyword) {
        return actualiteRepository.searchByKeyword(keyword).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ActualiteResponse creerActualite(ActualiteRequest request) {
        Membre auteur = membreRepository.findById(request.getAuteurId())
                .orElseThrow(() -> new IllegalArgumentException("Author not found."));

        Actualite actualite = Actualite.builder()
                .titre(request.getTitre())
                .contenu(request.getContenu())
                .imageUrl(request.getImageUrl())
                .categorie(request.getCategorie())
                .publiee(false)
                .auteur(auteur)
                .build();

        return toResponse(actualiteRepository.save(actualite));
    }

    @Transactional
    public ActualiteResponse mettreAJourActualite(Long id, ActualiteRequest request) {
        Actualite actualite = getActualiteEntityById(id);
        actualite.setTitre(request.getTitre());
        actualite.setContenu(request.getContenu());
        actualite.setCategorie(request.getCategorie());
        if (request.getImageUrl() != null && !request.getImageUrl().isBlank()) {
            actualite.setImageUrl(request.getImageUrl());
        }
        return toResponse(actualiteRepository.save(actualite));
    }

    @Transactional
    public void changerPublication(Long id, ActualitePublicationRequest request) {
        Actualite actualite = getActualiteEntityById(id);
        actualite.setPubliee(request.getPubliee());
        actualiteRepository.save(actualite);
    }

    @Transactional
    public void supprimerActualite(Long id) {
        if (!actualiteRepository.existsById(id)) {
            throw new IllegalArgumentException("News item not found for id: " + id);
        }
        actualiteRepository.deleteById(id);
    }

    public long countActualitesPubliees() {
        return actualiteRepository.countBypublieeTrue();
    }

    private Actualite getActualiteEntityById(Long id) {
        return actualiteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("News item not found for id: " + id));
    }

    private ActualiteResponse toResponse(Actualite actualite) {
        return ActualiteResponse.builder()
                .id(actualite.getId())
                .titre(actualite.getTitre())
                .contenu(actualite.getContenu())
                .imageUrl(actualite.getImageUrl())
                .categorie(actualite.getCategorie())
                .publiee(actualite.isPubliee())
                .datePublication(actualite.getDatePublication())
                .dateMiseAJour(actualite.getDateMiseAJour())
                .auteur(toSummary(actualite.getAuteur()))
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
