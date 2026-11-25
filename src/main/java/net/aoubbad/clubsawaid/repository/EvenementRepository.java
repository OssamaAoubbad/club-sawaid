package net.aoubbad.clubsawaid.repository;


import net.aoubbad.clubsawaid.entity.Evenement;
import net.aoubbad.clubsawaid.entity.enums.TypeEvenement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EvenementRepository extends JpaRepository<Evenement, Long> {

    // --- Événements publiés (vue publique) ---
    List<Evenement> findByPublieTrueOrderByDateEvenementAsc();

    // --- Événements à venir ---
    @Query("SELECT e FROM Evenement e WHERE e.publie = true AND e.dateEvenement > :maintenant ORDER BY e.dateEvenement ASC")
    List<Evenement> findEvenementsAVenir(@Param("maintenant") LocalDateTime maintenant);

    // --- Événements passés ---
    @Query("SELECT e FROM Evenement e WHERE e.publie = true AND e.dateEvenement < :maintenant ORDER BY e.dateEvenement DESC")
    List<Evenement> findEvenementsPasses(@Param("maintenant") LocalDateTime maintenant);

    // --- Filtrer par type ---
    List<Evenement> findByTypeAndPublieTrue(TypeEvenement type);

    // --- Événements d'un organisateur ---
    List<Evenement> findByOrganisateurId(Long organisateurId);

    // --- Recherche textuelle sur titre ou description ---
    @Query("SELECT e FROM Evenement e WHERE e.publie = true AND (" +
            "LOWER(e.titre) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Evenement> searchByKeyword(@Param("keyword") String keyword);

    // --- Événements auxquels un membre participe ---
    @Query("SELECT e FROM Evenement e JOIN e.participants p WHERE p.id = :membreId")
    List<Evenement> findByParticipantId(@Param("membreId") Long membreId);

    // --- Vérifier si un membre est déjà inscrit ---
    @Query("SELECT COUNT(e) > 0 FROM Evenement e JOIN e.participants p " +
            "WHERE e.id = :evenementId AND p.id = :membreId")
    boolean isMembreInscrit(@Param("evenementId") Long evenementId,
                            @Param("membreId") Long membreId);

    // --- Nombre de participants d'un événement ---
    @Query("SELECT SIZE(e.participants) FROM Evenement e WHERE e.id = :id")
    int countParticipants(@Param("id") Long id);

    // --- Prochains événements (les N suivants) ---
    @Query("SELECT e FROM Evenement e WHERE e.publie = true AND e.dateEvenement > :maintenant " +
            "ORDER BY e.dateEvenement ASC LIMIT :n")
    List<Evenement> findProchainsEvenements(@Param("maintenant") LocalDateTime maintenant,
                                            @Param("n") int n);
}
