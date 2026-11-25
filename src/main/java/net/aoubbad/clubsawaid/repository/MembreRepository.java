package net.aoubbad.clubsawaid.repository;


import net.aoubbad.clubsawaid.entity.Membre;
import net.aoubbad.clubsawaid.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembreRepository extends JpaRepository<Membre, Long> {

    // --- Recherche par email  ---
    Optional<Membre> findByEmail(String email);

    boolean existsByEmail(String email);

    // --- Filtres par statut ---
    List<Membre> findByActifTrue();

    List<Membre> findByEmailVerifieFalse();

    List<Membre> findByRole(Role role);

    List<Membre> findByActifTrueAndRole(Role role);

    // --- Recherche par filière / promotion ---
    List<Membre> findByFiliere(String filiere);

    List<Membre> findByPromotion(String promotion);

    List<Membre> findByFiliereAndPromotion(String filiere, String promotion);

    // --- Recherche textuelle (nom ou prénom) ---
    @Query("SELECT m FROM Membre m WHERE " +
            "LOWER(m.nom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(m.prenom) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Membre> searchByNomOrPrenom(@Param("keyword") String keyword);

    // --- Activation du compte ---
    @Modifying
    @Query("UPDATE Membre m SET m.emailVerifie = true, m.actif = true WHERE m.id = :id")
    void activerCompte(@Param("id") Long id);

    // --- Comptages ---
    long countByActifTrue();

    long countByRole(Role role);
}