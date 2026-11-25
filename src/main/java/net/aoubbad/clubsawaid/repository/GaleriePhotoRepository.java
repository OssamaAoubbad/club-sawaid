package net.aoubbad.clubsawaid.repository;


import net.aoubbad.clubsawaid.entity.GaleriePhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GaleriePhotoRepository extends JpaRepository<GaleriePhoto, Long> {

    // --- Photos d'un événement ---
    List<GaleriePhoto> findByEvenementIdOrderByDateAjoutDesc(Long evenementId);

    // --- Photos sans événement (galerie générale) ---
    List<GaleriePhoto> findByEvenementIsNullOrderByDateAjoutDesc();

    // --- Toutes les photos triées par date ---
    List<GaleriePhoto> findAllByOrderByDateAjoutDesc();

    // --- Photos ajoutées par un membre ---
    List<GaleriePhoto> findByAjouteParId(Long membreId);

    // --- Dernières N photos pour la page d'accueil ---
    @Query("SELECT g FROM GaleriePhoto g ORDER BY g.dateAjout DESC LIMIT :n")
    List<GaleriePhoto> findDernieresPhotos(@Param("n") int n);
}
