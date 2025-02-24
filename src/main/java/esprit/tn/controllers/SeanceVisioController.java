package esprit.tn.controllers;

import esprit.tn.entities.Seance;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class SeanceVisioController {
    private Seance seance;

    public void setSeance(Seance seance) {
        this.seance = seance;
        chargerJitsi();
    }

    private void chargerJitsi() {
        if (seance != null) {
            try {
                // 🔍 Encode le titre pour éviter les espaces et caractères spéciaux
                String titreEncode = seance.getTitre().replaceAll(" ", "%20");

                // 🚀 Générer une URL Jitsi sans la salle d'attente
                String meetURL = "https://meet.jit.si/" + titreEncode + "-" + seance.getIdSeance()
                        + "#config.prejoinPageEnabled=false";

                System.out.println("🔗 URL générée : " + meetURL);

                // 📂 Ouvrir Jitsi dans le navigateur par défaut
                Desktop.getDesktop().browse(new URI(meetURL));

            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
                System.err.println("❌ Erreur lors de l'ouverture du lien Jitsi !");
            }
        }
    }
}