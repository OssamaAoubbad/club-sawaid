package net.aoubbad.clubsawaid.entity;


import jakarta.persistence.*;
import lombok.*;
import net.aoubbad.clubsawaid.entity.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "membres")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Membre implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String motDePasse;

    private String telephone;

    private String filiere;

    private String promotion;

    private String photoUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private LocalDate dateInscription;

    /**
     * false par défaut → le compte est inactif jusqu'à vérification de l'email.
     * Passe à true une fois l'email confirmé.
     */
    @Builder.Default
    private boolean actif = false;

    /**
     * Passe à true lorsque le membre clique sur le lien de vérification.
     */
    @Builder.Default
    private boolean emailVerifie = false;

    // -------------------------------------------------------------------------
    // Relations
    // -------------------------------------------------------------------------

    @OneToOne(mappedBy = "membre", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private EmailVerificationToken emailVerificationToken;

    @OneToOne(mappedBy = "membre", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PasswordResetToken passwordResetToken;

    @OneToMany(mappedBy = "auteur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Actualite> actualites;

    @OneToMany(mappedBy = "organisateur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Evenement> evenementsOrganises;

    @ManyToMany(mappedBy = "participants", fetch = FetchType.LAZY)
    private List<Evenement> evenementsInscrits;

    @OneToMany(mappedBy = "membre", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Candidature> candidatures;

    // -------------------------------------------------------------------------
    // Spring Security — UserDetails
    // -------------------------------------------------------------------------

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() { return motDePasse; }

    @Override
    public String getUsername() { return email; }

    @Override
    public boolean isAccountNonExpired()     { return true; }

    @Override
    public boolean isAccountNonLocked()      { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    /**
     * Un membre peut se connecter uniquement si son email est vérifié ET son compte actif.
     */
    @Override
    public boolean isEnabled() { return actif && emailVerifie; }
}