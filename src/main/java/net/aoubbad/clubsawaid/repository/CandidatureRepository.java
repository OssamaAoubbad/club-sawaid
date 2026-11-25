package net.aoubbad.clubsawaid.repository;


import net.aoubbad.clubsawaid.entity.Candidature;
import net.aoubbad.clubsawaid.entity.enums.StatutCandidature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidatureRepository extends JpaRepository<Candidature, Long> {

    // --- Candidatures d'un membre ---
    List<Candidature> findByMembreId(Long membreId);

    List<Candidature> findByMembreIdAndStatut(Long membreId, StatutCandidature statut);

    // --- Candidatures pour un projet ---
    List<Candidature> findByProjetId(Long projetId);

    List<Candidature> findByProjetIdAndStatut(Long projetId, StatutCandidature statut);

    // --- Vérifier si un membre a déjà postulé à un projet ---
    boolean existsByMembreIdAndProjetId(Long membreId, Long projetId);

    Optional<Candidature> findByMembreIdAndProjetId(Long membreId, Long projetId);

    // --- Nombre de bénévoles acceptés sur un projet ---
    @Query("SELECT COUNT(c) FROM Candidature c WHERE c.projet.id = :projetId AND c.statut = 'ACCEPTEE'")
    long countBenevoleAcceptesParProjet(@Param("projetId") Long projetId);

    // --- Toutes les candidatures EN_ATTENTE (pour l'admin) ---
    List<Candidature> findByStatutOrderByDateCandidatureAsc(StatutCandidature statut);

    // --- Comptages ---
    long countByStatut(StatutCandidature statut);

    long countByProjetId(Long projetId);
}
