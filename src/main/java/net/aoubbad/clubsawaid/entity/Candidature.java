package net.aoubbad.clubsawaid.entity;


import jakarta.persistence.*;
import lombok.*;
import net.aoubbad.clubsawaid.entity.enums.StatutCandidature;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "candidatures",
        uniqueConstraints = @UniqueConstraint(columnNames = {"membre_id", "projet_id"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String motivation;

    @Enumerated(EnumType.STRING)
    private StatutCandidature statut;  // EN_ATTENTE, ACCEPTEE, REFUSEE

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCandidature;

    private LocalDateTime dateReponse;

    // --- Relations ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membre_id", nullable = false)
    private Membre membre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projet_id", nullable = false)
    private Projet projet;

    @PrePersist
    protected void onCreate() {
        this.dateCandidature = LocalDateTime.now();
        this.statut = StatutCandidature.EN_ATTENTE;
    }


}