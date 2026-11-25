package net.aoubbad.clubsawaid.service;

import lombok.RequiredArgsConstructor;
import net.aoubbad.clubsawaid.dto.CommentaireRequest;
import net.aoubbad.clubsawaid.dto.CommentaireResponse;
import net.aoubbad.clubsawaid.entity.Actualite;
import net.aoubbad.clubsawaid.entity.Commentaire;
import net.aoubbad.clubsawaid.entity.Evenement;
import net.aoubbad.clubsawaid.entity.Membre;
import net.aoubbad.clubsawaid.repository.ActualiteRepository;
import net.aoubbad.clubsawaid.repository.CommentaireRepository;
import net.aoubbad.clubsawaid.repository.EvenementRepository;
import net.aoubbad.clubsawaid.repository.MembreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentaireService {

    private final CommentaireRepository commentaireRepository;
    private final MembreRepository membreRepository;
    private final EvenementRepository evenementRepository;
    private final ActualiteRepository actualiteRepository;

    @Transactional
    public CommentaireResponse commenterEvenement(CommentaireRequest request) {
        if (request.getEvenementId() == null) {
            throw new IllegalArgumentException("Identifiant d'événement requis pour un commentaire d'événement.");
        }
        Evenement evenement = evenementRepository.findById(request.getEvenementId())
                .orElseThrow(() -> new IllegalArgumentException("événement  introuvable."));
        Membre auteur = membreRepository.findById(request.getAuteurId())
                .orElseThrow(() -> new IllegalArgumentException("Membre introuvable."));

        Commentaire commentaire = Commentaire.builder()
                .contenu(request.getContenu())
                .auteur(auteur)
                .evenement(evenement)
                .build();

        return toResponse(commentaireRepository.save(commentaire));
    }

    @Transactional
    public CommentaireResponse commenterActualite(CommentaireRequest request) {
        if (request.getActualiteId() == null) {
            throw new IllegalArgumentException("Identifiant d'actualit� requis pour un commentaire d'actualit�.");
        }
        Actualite actualite = actualiteRepository.findById(request.getActualiteId())
                .orElseThrow(() -> new IllegalArgumentException("Actualit� introuvable."));
        Membre auteur = membreRepository.findById(request.getAuteurId())
                .orElseThrow(() -> new IllegalArgumentException("Membre introuvable."));

        Commentaire commentaire = Commentaire.builder()
                .contenu(request.getContenu())
                .auteur(auteur)
                .actualite(actualite)
                .build();

        return toResponse(commentaireRepository.save(commentaire));
    }

    public List<CommentaireResponse> getCommentairesEvenement(Long evenementId) {
        return commentaireRepository.findByEvenementIdOrderByDateCommentaireAsc(evenementId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<CommentaireResponse> getCommentairesActualite(Long actualiteId) {
        return commentaireRepository.findByActualiteIdOrderByDateCommentaireAsc(actualiteId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void supprimerCommentaire(Long commentaireId, Long demandeurId, boolean isAdmin) {
        Commentaire commentaire = commentaireRepository.findById(commentaireId)
                .orElseThrow(() -> new IllegalArgumentException("Commentaire introuvable."));

        boolean estAuteur = commentaire.getAuteur().getId().equals(demandeurId);
        if (!isAdmin && !estAuteur) {
            throw new SecurityException("Vous n'�tes pas autoris� � supprimer ce commentaire.");
        }

        commentaireRepository.deleteById(commentaireId);
    }

    private CommentaireResponse toResponse(Commentaire commentaire) {
        return CommentaireResponse.builder()
                .id(commentaire.getId())
                .contenu(commentaire.getContenu())
                .dateCommentaire(commentaire.getDateCommentaire())
                .auteurId(commentaire.getAuteur() != null ? commentaire.getAuteur().getId() : null)
                .evenementId(commentaire.getEvenement() != null ? commentaire.getEvenement().getId() : null)
                .actualiteId(commentaire.getActualite() != null ? commentaire.getActualite().getId() : null)
                .build();
    }
}
