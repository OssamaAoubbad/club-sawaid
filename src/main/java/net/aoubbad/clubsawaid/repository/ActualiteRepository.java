package net.aoubbad.clubsawaid.repository;


import net.aoubbad.clubsawaid.entity.Actualite;
import net.aoubbad.clubsawaid.entity.enums.CategorieActualite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActualiteRepository extends JpaRepository<Actualite, Long> {

    // --- Vue publique (publiées uniquement) avec pagination ---
    Page<Actualite> findBypublieeTrueOrderByDatePublicationDesc(Pageable pageable);

    Page<Actualite> findByPublieeTrueOrderByDatePublicationDesc(Pageable pageable);

    // --- Filtrer par catégorie ---
    List<Actualite> findByCategorieAndPublieeTrue(CategorieActualite categorie);

    // --- Actualités d'un auteur ---
    List<Actualite> findByAuteurId(Long auteurId);

    // --- Recherche textuelle ---
    @Query("SELECT a FROM Actualite a WHERE a.publiee = true AND (" +
            "LOWER(a.titre) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(a.contenu) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Actualite> searchByKeyword(@Param("keyword") String keyword);

    // --- Dernières actualités (pour la page d'accueil) ---
    @Query("SELECT a FROM Actualite a WHERE a.publiee = true " +
            "ORDER BY a.datePublication DESC LIMIT :n")
    List<Actualite> findDernieresActualites(@Param("n") int n);

    // --- Comptage ---
    long countBypublieeTrue();
}
