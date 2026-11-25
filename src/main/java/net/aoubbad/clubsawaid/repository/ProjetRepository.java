package net.aoubbad.clubsawaid.repository;


import net.aoubbad.clubsawaid.entity.Projet;
import net.aoubbad.clubsawaid.entity.enums.StatutProjet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjetRepository extends JpaRepository<Projet, Long> {

    // --- Filtrer par statut ---
    List<Projet> findByStatut(StatutProjet statut);

    List<Projet> findByStatutOrderByDateDebutDesc(StatutProjet statut);

    // --- Projets d'un responsable ---
    List<Projet> findByResponsableId(Long responsableId);

    // --- Projets en recrutement (EN_COURS avec des places disponibles) ---
    @Query("SELECT p FROM Projet p WHERE p.statut = 'EN_COURS' AND " +
            "(p.nombreBenevoles IS NULL OR " +
            "p.nombreBenevoles > (SELECT COUNT(c) FROM Candidature c WHERE c.projet = p AND c.statut = 'ACCEPTEE'))")
    List<Projet> findProjetsEnRecrutement();

    // --- Recherche textuelle ---
    @Query("SELECT p FROM Projet p WHERE " +
            "LOWER(p.titre) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Projet> searchByKeyword(@Param("keyword") String keyword);

    // --- Comptages par statut ---
    long countByStatut(StatutProjet statut);
}
