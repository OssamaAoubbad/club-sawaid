package net.aoubbad.clubsawaid.repository;


import net.aoubbad.clubsawaid.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    Optional<PasswordResetToken> findByMembreId(Long membreId);

    boolean existsByToken(String token);

    // Nettoyage des tokens expirés (via @Scheduled)
    @Modifying
    @Query("DELETE FROM PasswordResetToken t WHERE t.expiration < :now")
    void supprimerTokensExpires(@Param("now") LocalDateTime now);

    @Modifying
    @Query("DELETE FROM PasswordResetToken t WHERE t.membre.id = :membreId")
    void supprimerParMembreId(@Param("membreId") Long membreId);
}
