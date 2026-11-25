package net.aoubbad.clubsawaid.service;

import lombok.RequiredArgsConstructor;
import net.aoubbad.clubsawaid.dto.GaleriePhotoRequest;
import net.aoubbad.clubsawaid.dto.GaleriePhotoResponse;
import net.aoubbad.clubsawaid.dto.MembreSummaryDto;
import net.aoubbad.clubsawaid.entity.Evenement;
import net.aoubbad.clubsawaid.entity.GaleriePhoto;
import net.aoubbad.clubsawaid.entity.Membre;
import net.aoubbad.clubsawaid.repository.EvenementRepository;
import net.aoubbad.clubsawaid.repository.GaleriePhotoRepository;
import net.aoubbad.clubsawaid.repository.MembreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GaleriePhotoService {

    private final GaleriePhotoRepository galeriePhotoRepository;
    private final MembreRepository membreRepository;
    private final EvenementRepository evenementRepository;

    public List<GaleriePhotoResponse> getToutesLesPhotos() {
        return galeriePhotoRepository.findAllByOrderByDateAjoutDesc().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<GaleriePhotoResponse> getPhotosParEvenement(Long evenementId) {
        return galeriePhotoRepository.findByEvenementIdOrderByDateAjoutDesc(evenementId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<GaleriePhotoResponse> getPhotosGenerales() {
        return galeriePhotoRepository.findByEvenementIsNullOrderByDateAjoutDesc().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<GaleriePhotoResponse> getDernieresPhotos(int n) {
        return galeriePhotoRepository.findDernieresPhotos(n).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public GaleriePhotoResponse ajouterPhoto(GaleriePhotoRequest request) {
        Membre membre = membreRepository.findById(request.getAjouteParId())
                .orElseThrow(() -> new IllegalArgumentException("Membre introuvable."));

        Evenement evenement = null;
        if (request.getEvenementId() != null) {
            evenement = evenementRepository.findById(request.getEvenementId())
                    .orElseThrow(() -> new IllegalArgumentException("Événement introuvable."));
        }

        GaleriePhoto photo = GaleriePhoto.builder()
                .url(request.getUrl())
                .legende(request.getLegende())
                .altText(request.getAltText())
                .ajoutePar(membre)
                .evenement(evenement)
                .build();

        return toResponse(galeriePhotoRepository.save(photo));
    }

    @Transactional
    public void supprimerPhoto(Long id, Long demandeurId, boolean isAdmin) {
        GaleriePhoto photo = galeriePhotoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Photo introuvable avec l'id : " + id));

        boolean estProprio = photo.getAjoutePar().getId().equals(demandeurId);
        if (!isAdmin && !estProprio) {
            throw new SecurityException("Vous n'êtes pas autorisé à supprimer cette photo.");
        }

        galeriePhotoRepository.deleteById(id);
    }

    private GaleriePhotoResponse toResponse(GaleriePhoto photo) {
        return GaleriePhotoResponse.builder()
                .id(photo.getId())
                .url(photo.getUrl())
                .legende(photo.getLegende())
                .altText(photo.getAltText())
                .dateAjout(photo.getDateAjout())
                .evenementId(photo.getEvenement() != null ? photo.getEvenement().getId() : null)
                .ajoutePar(toSummary(photo.getAjoutePar()))
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
