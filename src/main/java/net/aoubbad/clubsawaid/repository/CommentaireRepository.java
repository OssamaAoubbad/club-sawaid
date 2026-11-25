package net.aoubbad.clubsawaid.repository;


import net.aoubbad.clubsawaid.entity.Commentaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentaireRepository extends JpaRepository<Commentaire, Long> {

    // --- Commentaires d'un événement ---
    List<Commentaire> findByEvenementIdOrderByDateCommentaireAsc(Long evenementId);

    // --- Commentaires d'une actualité ---
    List<Commentaire> findByActualiteIdOrderByDateCommentaireAsc(Long actualiteId);

    // --- Commentaires d'un membre ---
    List<Commentaire> findByAuteurId(Long auteurId);

    // --- Supprimer tous les commentaires d'un événement ---
    @Modifying
    @Query("DELETE FROM Commentaire c WHERE c.evenement.id = :evenementId")
    void deleteByEvenementId(@Param("evenementId") Long evenementId);

    // --- Supprimer tous les commentaires d'une actualité ---
    @Modifying
    @Query("DELETE FROM Commentaire c WHERE c.actualite.id = :actualiteId")
    void deleteByActualiteId(@Param("actualiteId") Long actualiteId);

    // --- Comptages ---
    long countByEvenementId(Long evenementId);

    long countByActualiteId(Long actualiteId);
}