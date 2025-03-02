package esprit.tn.controllers;

import esprit.tn.entities.Seance;
import esprit.tn.services.EmailService;
import esprit.tn.services.UserService;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class SeanceVisioController {
    private Seance seance;
    private final UserService userService = new UserService();

    public void setSeance(Seance seance) {
        this.seance = seance;
        chargerJitsi();
    }

    private void chargerJitsi() {
        if (seance != null) {
            try {
                String titreEncode = seance.getTitre().replaceAll(" ", "%20");
                String meetURL = "https://meet.jit.si/" + titreEncode + "-" + seance.getIdSeance()
                        + "#config.prejoinPageEnabled=false";

                System.out.println("🔗 URL générée : " + meetURL);

                Desktop.getDesktop().browse(new URI(meetURL));

                // 📧 Envoyer l'email aux élèves et formateurs
                envoyerEmailAuxParticipants(meetURL);

            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
                System.err.println("❌ Erreur lors de l'ouverture du lien Jitsi !");
            }
        }
    }

    private void envoyerEmailAuxParticipants(String meetURL) {
        List<String> emailsParticipants = userService.getEmailsParticipants();

        String sujet = "📅 Invitation à la séance : " + seance.getTitre();
        String message = "Bonjour,\n\n"
                + "Vous êtes invité(e) à participer à la séance : **" + seance.getTitre() + "**.\n"
                + "Cliquez sur le lien pour rejoindre la réunion : " + meetURL + "\n\n"
                + "À bientôt !";

        for (String email : emailsParticipants) {
            boolean sent = EmailService.envoyerEmail(email, sujet, message);
            if (sent) {
                System.out.println("✅ Email envoyé à : " + email);
            } else {
                System.err.println("❌ Erreur lors de l'envoi de l'email à : " + email);
            }
        }
    }
}
