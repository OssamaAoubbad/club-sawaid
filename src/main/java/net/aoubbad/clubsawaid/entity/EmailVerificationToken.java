package net.aoubbad.clubsawaid.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "email_verification_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiration;

    private boolean utilise = false;

    // --- Relation ---

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membre_id", nullable = false)
    private Membre membre;

    // --- Méthodes utilitaires ---

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiration);
    }

    public boolean isValide() {
        return !utilise && !isExpired();
    }

    /**
     * Factory method : génère un token UUID valable 24h
     */
    public static EmailVerificationToken creerPour(Membre membre) {
        return EmailVerificationToken.builder()
                .token(UUID.randomUUID().toString())
                .membre(membre)
                .expiration(LocalDateTime.now().plusHours(24))
                .utilise(false)
                .build();
    }
}