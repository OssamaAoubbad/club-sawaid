package net.aoubbad.clubsawaid.entity;

import jakarta.persistence.*;
import lombok.*;
import net.aoubbad.clubsawaid.entity.enums.CategorieActualite;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "actualites")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Actualite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contenu;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private CategorieActualite categorie;   // ANNONCE, RAPPORT, TEMOIGNAGE...

    private boolean publiee = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime datePublication;

    private LocalDateTime dateMiseAJour;

    // --- Relations ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auteur_id", nullable = false)
    private Membre auteur;

    @OneToMany(mappedBy = "actualite", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Commentaire> commentaires;

    @PrePersist
    protected void onCreate() {
        this.datePublication = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.dateMiseAJour = LocalDateTime.now();
    }


}