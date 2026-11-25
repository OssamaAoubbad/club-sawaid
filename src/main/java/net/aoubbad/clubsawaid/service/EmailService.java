package net.aoubbad.clubsawaid.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${spring.mail.username}")
    private String expediteur;

    // -------------------------------------------------------------------------
    // Email de vérification de compte
    // -------------------------------------------------------------------------

    @Async
    public void envoyerEmailVerification(String destinataire, String prenom, String token) {
        String lien = baseUrl + "/verify-email?token=" + token;
        String sujet = "✅ Swaid Al Amal — Activez votre compte";
        String corps = buildEmailVerificationHtml(prenom, lien);
        envoyerEmail(destinataire, sujet, corps);
    }

    // -------------------------------------------------------------------------
    // Email de réinitialisation de mot de passe
    // -------------------------------------------------------------------------

    @Async
    public void envoyerEmailReinitialisationMotDePasse(String destinataire, String prenom, String token) {
        String lien = baseUrl + "/reset-password?token=" + token;
        String sujet = "🔑 Swaid Al Amal — Réinitialisation de votre mot de passe";
        String corps = buildPasswordResetHtml(prenom, lien);
        envoyerEmail(destinataire, sujet, corps);
    }

    // -------------------------------------------------------------------------
    // Email de confirmation d'inscription à un événement
    // -------------------------------------------------------------------------

    @Async
    public void envoyerConfirmationInscriptionEvenement(String destinataire, String prenom,
                                                        String titreEvenement, String dateEvenement,
                                                        String lieu) {
        String sujet = "📅 Inscription confirmée — " + titreEvenement;
        String corps = buildInscriptionEvenementHtml(prenom, titreEvenement, dateEvenement, lieu);
        envoyerEmail(destinataire, sujet, corps);
    }

    // -------------------------------------------------------------------------
    // Email de réponse à une candidature
    // -------------------------------------------------------------------------

    @Async
    public void envoyerReponseCandidature(String destinataire, String prenom,
                                          String titreProjet, boolean acceptee) {
        String sujet = acceptee
                ? "🎉 Candidature acceptée — " + titreProjet
                : "📋 Candidature — " + titreProjet;
        String corps = buildReponseCandidatureHtml(prenom, titreProjet, acceptee);
        envoyerEmail(destinataire, sujet, corps);
    }

    // -------------------------------------------------------------------------
    // Méthode générique d'envoi
    // -------------------------------------------------------------------------

    private void envoyerEmail(String destinataire, String sujet, String corpsHtml) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(expediteur, "Swaid Al Amal — ENSAM Casa");
            helper.setTo(destinataire);
            helper.setSubject(sujet);
            helper.setText(corpsHtml, true);
            mailSender.send(message);
            log.info("Email envoyé à {} : {}", destinataire, sujet);
        } catch (MessagingException | java.io.UnsupportedEncodingException e) {
            log.error("Erreur lors de l'envoi de l'email à {} : {}", destinataire, e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Templates HTML
    // -------------------------------------------------------------------------

    private String buildEmailVerificationHtml(String prenom, String lien) {
        return """
            <div style="font-family:Arial,sans-serif;max-width:600px;margin:auto;padding:24px;border:1px solid #e0e0e0;border-radius:8px">
              <h2 style="color:#2e7d32">Bienvenue chez Swaid Al Amal 🌱</h2>
              <p>Bonjour <strong>%s</strong>,</p>
              <p>Merci de vous être inscrit(e) au club <strong>Swaid Al Amal</strong> de l'ENSAM Casablanca.</p>
              <p>Veuillez cliquer sur le bouton ci-dessous pour activer votre compte :</p>
              <div style="text-align:center;margin:32px 0">
                <a href="%s"
                   style="background:#2e7d32;color:#fff;padding:12px 28px;border-radius:6px;text-decoration:none;font-size:16px">
                  Activer mon compte
                </a>
              </div>
              <p style="color:#757575;font-size:13px">Ce lien est valable <strong>24 heures</strong>. Si vous n'êtes pas à l'origine de cette inscription, ignorez cet email.</p>
              <hr style="border:none;border-top:1px solid #e0e0e0;margin:24px 0"/>
              <p style="color:#9e9e9e;font-size:12px;text-align:center">Swaid Al Amal · ENSAM Casablanca</p>
            </div>
            """.formatted(prenom, lien);
    }

    private String buildPasswordResetHtml(String prenom, String lien) {
        return """
            <div style="font-family:Arial,sans-serif;max-width:600px;margin:auto;padding:24px;border:1px solid #e0e0e0;border-radius:8px">
              <h2 style="color:#1565c0">Réinitialisation de mot de passe 🔑</h2>
              <p>Bonjour <strong>%s</strong>,</p>
              <p>Vous avez demandé la réinitialisation de votre mot de passe.</p>
              <div style="text-align:center;margin:32px 0">
                <a href="%s"
                   style="background:#1565c0;color:#fff;padding:12px 28px;border-radius:6px;text-decoration:none;font-size:16px">
                  Réinitialiser mon mot de passe
                </a>
              </div>
              <p style="color:#757575;font-size:13px">Ce lien est valable <strong>1 heure</strong>. Si vous n'avez pas fait cette demande, ignorez cet email.</p>
              <hr style="border:none;border-top:1px solid #e0e0e0;margin:24px 0"/>
              <p style="color:#9e9e9e;font-size:12px;text-align:center">Swaid Al Amal · ENSAM Casablanca</p>
            </div>
            """.formatted(prenom, lien);
    }

    private String buildInscriptionEvenementHtml(String prenom, String titre,
                                                 String date, String lieu) {
        return """
            <div style="font-family:Arial,sans-serif;max-width:600px;margin:auto;padding:24px;border:1px solid #e0e0e0;border-radius:8px">
              <h2 style="color:#6a1b9a">Inscription confirmée 📅</h2>
              <p>Bonjour <strong>%s</strong>,</p>
              <p>Votre inscription à l'événement suivant a bien été enregistrée :</p>
              <table style="width:100%%;margin:16px 0">
                <tr><td style="font-weight:bold;padding:6px 0">Événement :</td><td>%s</td></tr>
                <tr><td style="font-weight:bold;padding:6px 0">Date :</td><td>%s</td></tr>
                <tr><td style="font-weight:bold;padding:6px 0">Lieu :</td><td>%s</td></tr>
              </table>
              <p>À très bientôt ! 🌿</p>
              <hr style="border:none;border-top:1px solid #e0e0e0;margin:24px 0"/>
              <p style="color:#9e9e9e;font-size:12px;text-align:center">Swaid Al Amal · ENSAM Casablanca</p>
            </div>
            """.formatted(prenom, titre, date, lieu);
    }

    private String buildReponseCandidatureHtml(String prenom, String titreProjet, boolean acceptee) {
        String couleur = acceptee ? "#2e7d32" : "#c62828";
        String message = acceptee
                ? "Félicitations ! Votre candidature au projet <strong>" + titreProjet + "</strong> a été <strong>acceptée</strong>. Vous serez contacté(e) prochainement pour la suite."
                : "Votre candidature au projet <strong>" + titreProjet + "</strong> n'a malheureusement pas été retenue cette fois-ci. Nous vous encourageons à postuler à nos prochains projets !";
        return """
            <div style="font-family:Arial,sans-serif;max-width:600px;margin:auto;padding:24px;border:1px solid #e0e0e0;border-radius:8px">
              <h2 style="color:%s">Réponse à votre candidature</h2>
              <p>Bonjour <strong>%s</strong>,</p>
              <p>%s</p>
              <hr style="border:none;border-top:1px solid #e0e0e0;margin:24px 0"/>
              <p style="color:#9e9e9e;font-size:12px;text-align:center">Swaid Al Amal · ENSAM Casablanca</p>
            </div>
            """.formatted(couleur, prenom, message);
    }
}