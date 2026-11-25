package net.aoubbad.clubsawaid.repository;

import net.aoubbad.clubsawaid.entity.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {

    Optional<EmailVerificationToken> findByToken(String token);

    Optional<EmailVerificationToken> findByMembreId(Long membreId);

    boolean existsByToken(String token);

    // Supprimer les tokens expirés (à appeler via un @Scheduled)
    @Modifying
    @Query("DELETE FROM EmailVerificationToken t WHERE t.expiration < :now")
    void supprimerTokensExpires(@Param("now") LocalDateTime now);

    // Supprimer l'ancien token d'un membre avant d'en créer un nouveau
    @Modifying
    @Query("DELETE FROM EmailVerificationToken t WHERE t.membre.id = :membreId")
    void supprimerParMembreId(@Param("membreId") Long membreId);
}
