package esprit.tn.main;

import esprit.tn.entities.Reclamation;
import esprit.tn.services.ReclamationService;

import esprit.tn.entities.Feedback;
import esprit.tn.services.FeedbackService;

import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.List;

import static esprit.tn.services.FeedbackService.*;
import static esprit.tn.services.ReclamationService.*;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection.getInstance();

        //--------------------- Ajout Réclamation dans la base de données ---------------------
        ReclamationService reclamationService = new ReclamationService();

        LocalDateTime dateCreation = LocalDateTime.now();

        List<Reclamation> allReclamations = reclamationService.getall(); // Récupérer toutes les réclamations

        //--------------------- Ajout Réclamation ---------------------------------------------
        //Reclamation recla = saisirReclamationAjout();
        //reclamationService.ajouter(recla);

        //--------------------- Modification Réclamation --------------------------------------
        Reclamation ReclamationChoisi = choixReclamation(allReclamations);

        Reclamation ReclaModif = saisirReclamationModification();
        // Mettre à jour le feedback choisi avec les nouveaux détails
        ReclamationChoisi.setTitre(ReclaModif.getTitre());
        ReclamationChoisi.setDescription(ReclaModif.getDescription());
        ReclamationChoisi.setStatus(ReclaModif.getStatus());
        ReclamationChoisi.setDateCreation(ReclaModif.getDateCreation());

        // Modifier la réclamation dans la base de données
        reclamationService.modifier(ReclamationChoisi);


    }
}