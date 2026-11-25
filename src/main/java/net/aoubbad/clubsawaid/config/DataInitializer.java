package net.aoubbad.clubsawaid.config;

import lombok.RequiredArgsConstructor;
import net.aoubbad.clubsawaid.entity.Actualite;
import net.aoubbad.clubsawaid.entity.Evenement;
import net.aoubbad.clubsawaid.entity.Membre;
import net.aoubbad.clubsawaid.entity.Projet;
import net.aoubbad.clubsawaid.entity.enums.CategorieActualite;
import net.aoubbad.clubsawaid.entity.enums.Role;
import net.aoubbad.clubsawaid.entity.enums.StatutProjet;
import net.aoubbad.clubsawaid.entity.enums.TypeEvenement;
import net.aoubbad.clubsawaid.repository.ActualiteRepository;
import net.aoubbad.clubsawaid.repository.EvenementRepository;
import net.aoubbad.clubsawaid.repository.MembreRepository;
import net.aoubbad.clubsawaid.repository.ProjetRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final MembreRepository membreRepository;
    private final EvenementRepository evenementRepository;
    private final ActualiteRepository actualiteRepository;
    private final ProjetRepository projetRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // --- Initialisation des membres ---
        if (membreRepository.count() == 0) {
            Membre admin = Membre.builder()
                    .nom("Admin")
                    .prenom("Super")
                    .email("admin@example.com")
                    .motDePasse(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .actif(true)
                    .emailVerifie(true)
                    .dateInscription(LocalDate.now())
                    .build();

            Membre membre = Membre.builder()
                    .nom("John")
                    .prenom("Doe")
                    .email("membre@example.com")
                    .motDePasse(passwordEncoder.encode("membre123"))
                    .role(Role.MEMBRE)
                    .actif(true)
                    .emailVerifie(true)
                    .dateInscription(LocalDate.now())
                    .build();

            Membre visiteur = Membre.builder()
                    .nom("Visitor")
                    .prenom("Guest")
                    .email("visiteur@example.com")
                    .motDePasse(passwordEncoder.encode("visiteur123"))
                    .role(Role.VISITEUR)
                    .actif(true)
                    .emailVerifie(true)
                    .dateInscription(LocalDate.now())
                    .build();

            membreRepository.save(admin);
            membreRepository.save(membre);
            membreRepository.save(visiteur);

            System.out.println("=== Comptes initiaux créés : ADMIN, MEMBRE, VISITEUR ===");
        }

        // --- Initialisation des événements ---
        if (evenementRepository.count() == 0) {
            Membre organisateur = membreRepository.findByEmail("membre@example.com").orElseThrow();

            Evenement event1 = Evenement.builder()
                    .titre("Journée Humanitaire")
                    .description("Collecte de vêtements et nourriture pour les plus démunis.")
                    .dateEvenement(LocalDateTime.now().plusDays(5))
                    .dateFin(LocalDateTime.now().plusDays(5).plusHours(4))
                    .lieu("Centre Communautaire")
                    .capaciteMax(100)
                    .type(TypeEvenement.HUMANITAIRE)
                    .organisateur(organisateur)
                    .imageUrl("9ec9a270-c0b9-4bf2-a66d-4d4b39bcb8d9_EDT_Master_IL")
                    .publie(true)
                    .participants(Collections.emptyList())
                    .build();

            Evenement event2 = Evenement.builder()
                    .titre("Tournoi de Football")
                    .description("Tournoi interclubs ouvert à tous les membres.")
                    .dateEvenement(LocalDateTime.now().plusDays(10))
                    .dateFin(LocalDateTime.now().plusDays(10).plusHours(3))
                    .lieu("Stade Municipal")
                    .capaciteMax(50)
                    .type(TypeEvenement.SPORTIF)
                    .organisateur(organisateur)
                    .imageUrl(null)
                    .publie(true)
                    .participants(Collections.emptyList())
                    .build();

            evenementRepository.save(event1);
            evenementRepository.save(event2);

            System.out.println("=== Événements initiaux créés ===");
        }

        // --- Initialisation des actualités ---
        if (actualiteRepository.count() == 0) {
            Membre auteur = membreRepository.findByEmail("admin@example.com").orElseThrow();

            Actualite act1 = Actualite.builder()
                    .titre("Nouveau Club Ouvert")
                    .contenu("Nous avons le plaisir d’annoncer l’ouverture du nouveau club sportif.")
                    .categorie(CategorieActualite.ANNONCE)
                    .auteur(auteur)
                    .publiee(true)
                    .imageUrl(null)
                    .build();

            Actualite act2 = Actualite.builder()
                    .titre("Rapport Annuel")
                    .contenu("Le rapport annuel de nos activités est maintenant disponible.")
                    .categorie(CategorieActualite.RAPPORT_ACTIVITE)
                    .auteur(auteur)
                    .publiee(true)
                    .imageUrl(null)
                    .build();

            actualiteRepository.save(act1);
            actualiteRepository.save(act2);

            System.out.println("=== Actualités initialisées ===");
        }

        // --- Initialisation des projets ---
        if (projetRepository.count() == 0) {
            Membre responsable1 = membreRepository.findByEmail("membre@example.com").orElseThrow();
            Membre responsable2 = membreRepository.findByEmail("admin@example.com").orElseThrow();

            Projet projet1 = Projet.builder()
                    .titre("Projet Humanitaire")
                    .description("Organisation d'une collecte alimentaire pour les familles démunies.")
                    .objectifs("Distribuer de la nourriture et sensibiliser les citoyens.")
                    .imageUrl(null)
                    .statut(StatutProjet.EN_COURS)
                    .dateDebut(LocalDate.now().plusDays(3))
                    .dateFin(LocalDate.now().plusDays(10))
                    .nombreBenevoles(20)
                    .responsable(responsable1)
                    .candidatures(Collections.emptyList())
                    .build();

            Projet projet2 = Projet.builder()
                    .titre("Projet Culturel")
                    .description("Atelier de peinture et exposition pour les jeunes artistes.")
                    .objectifs("Promouvoir l'art local et développer la créativité.")
                    .imageUrl(null)
                    .statut(StatutProjet.PLANIFIE)
                    .dateDebut(LocalDate.now().plusDays(15))
                    .dateFin(LocalDate.now().plusDays(20))
                    .nombreBenevoles(10)
                    .responsable(responsable2)
                    .candidatures(Collections.emptyList())
                    .build();

            Projet projet3 = Projet.builder()
                    .titre("Projet Sportif")
                    .description("Organisation d'un tournoi interclubs pour les jeunes.")
                    .objectifs("Encourager l'esprit sportif et la cohésion sociale.")
                    .imageUrl(null)
                    .statut(StatutProjet.TERMINE)
                    .dateDebut(LocalDate.now().minusDays(10))
                    .dateFin(LocalDate.now().minusDays(5))
                    .nombreBenevoles(15)
                    .responsable(responsable1)
                    .candidatures(Collections.emptyList())
                    .build();

            Projet projet4 = Projet.builder()
                    .titre("Projet Annulé")
                    .description("Projet initialement prévu mais annulé pour raisons organisationnelles.")
                    .objectifs("Simuler un projet annulé pour les tests.")
                    .imageUrl(null)
                    .statut(StatutProjet.ANNULE)
                    .dateDebut(LocalDate.now().plusDays(1))
                    .dateFin(LocalDate.now().plusDays(5))
                    .nombreBenevoles(5)
                    .responsable(responsable2)
                    .candidatures(Collections.emptyList())
                    .build();

            projetRepository.save(projet1);
            projetRepository.save(projet2);
            projetRepository.save(projet3);
            projetRepository.save(projet4);

            System.out.println("=== Projets initiaux créés (incluant un projet annulé) ===");
        }
    }
}