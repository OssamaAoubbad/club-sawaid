package net.aoubbad.clubsawaid.entity;


import jakarta.persistence.*;
import lombok.*;
import net.aoubbad.clubsawaid.entity.enums.StatutProjet;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "projets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Projet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String objectifs;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private StatutProjet statut;   // EN_COURS, TERMINE, PLANIFIE...

    private LocalDate dateDebut;

    private LocalDate dateFin;

    private Integer nombreBenevoles; // besoin en bénévoles

    @Column(nullable = false, updatable = false)
    private LocalDateTime creeLe;

    // --- Relations ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id", nullable = false)
    private Membre responsable;

    @OneToMany(mappedBy = "projet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Candidature> candidatures;

    @PrePersist
    protected void onCreate() {
        this.creeLe = LocalDateTime.now();
    }


}