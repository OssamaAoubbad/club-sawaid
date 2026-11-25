package net.aoubbad.clubsawaid.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "galerie_photos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GaleriePhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    private String legende;

    private String altText;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateAjout;

    // --- Relations ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evenement_id")
    private Evenement evenement;   // photo liée à un événement (optionnel)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ajoute_par_id", nullable = false)
    private Membre ajoutePar;

    @PrePersist
    protected void onCreate() {
        this.dateAjout = LocalDateTime.now();
    }
}