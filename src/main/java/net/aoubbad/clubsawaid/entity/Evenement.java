package net.aoubbad.clubsawaid.entity;


import jakarta.persistence.*;
import lombok.*;
import net.aoubbad.clubsawaid.entity.enums.TypeEvenement;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "evenements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDateTime dateEvenement;

    private LocalDateTime dateFin;

    @Column(nullable = false)
    private String lieu;

    private String imageUrl;

    private Integer capaciteMax;

    @Enumerated(EnumType.STRING)
    private TypeEvenement type;  // HUMANITAIRE, CULTUREL, SPORTIF, FORMATION...

    private boolean publie = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime creeLe;

    // --- Relations ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisateur_id", nullable = false)
    private Membre organisateur;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "evenement_participants",
            joinColumns = @JoinColumn(name = "evenement_id"),
            inverseJoinColumns = @JoinColumn(name = "membre_id")
    )
    private List<Membre> participants;

    @OneToMany(mappedBy = "evenement", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Commentaire> commentaires;

    @PrePersist
    protected void onCreate() {
        this.creeLe = LocalDateTime.now();
    }


}
