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
                //envoyerEmailAuxParticipants(meetURL);

            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
                System.err.println("❌ Erreur lors de l'ouverture du lien Jitsi !");
            }
        }
    }


}
