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
        Reclamation recla = saisirReclamation();
        reclamationService.ajouter(recla);

        //--------------------- Modification Réclamation --------------------------------------
        Reclamation ReclamationChoisi = choixReclamation(allReclamations);
        // Mettre à jour le feedback choisi avec les nouveaux détails
        ReclamationChoisi.setTitre(saisirReclamation().getTitre());
        ReclamationChoisi.setDescription(saisirReclamation().getDescription());
        ReclamationChoisi.setStatus(saisirReclamation().getStatus());
        ReclamationChoisi.setDateCreation(saisirReclamation().getDateCreation());

        // Modifier la réclamation dans la base de données
        reclamationService.modifier(ReclamationChoisi);

        //--------------------- Suppression Réclamation ----------------------------------------
        Reclamation reclamationChoisi = choixReclamation(allReclamations);
        reclamationService.supprimer(reclamationChoisi);

        //--------------------- Afficher Réclamation -------------------------------------------

        for (Reclamation reclamationAff : allReclamations) {
            System.out.println("ID: " + reclamationAff.getId());
            System.out.println("Titre: " + reclamationAff.getTitre());
            System.out.println("Description: " + reclamationAff.getDescription());
            System.out.println("Statut: " + reclamationAff.getStatus());
            System.out.println("Date de création: " + reclamationAff.getDateCreation());
            System.out.println("-----------------------------");
        }

        //--------------------- Rechercher Réclamation -----------------------------------------
        String critere = "cours";
        List<Reclamation> reclamationsTrouvees = reclamationService.rechercherRec(critere);

        //--------------------- Afficher les réclamations trouvées -----------------------------
        System.out.println("------------- Résultats de recherche de réclamations ----------------");
        for (Reclamation reclamation : reclamationsTrouvees) {
            System.out.println("ID: " + reclamation.getId());
            System.out.println("Titre: " + reclamation.getTitre());
            System.out.println("Description: " + reclamation.getDescription());
            System.out.println("Statut: " + reclamation.getStatus());
            System.out.println("Date de création: " + reclamation.getDateCreation());
            System.out.println("-----------------------------");
        }

        /////////////////////////////////////////////////////////////////////////////////////////

        //--------------------- Ajout Feedback dans la base de données --------------------------
        FeedbackService feedbackService = new FeedbackService();

        //--------------------- Ajout Feedback --------------------------------------------------
        Reclamation reclamationChoisie = choisirReclamation(allReclamations);

        if (reclamationChoisie != null) {
        Feedback feedback = saisirFeedback(reclamationChoisie);

        feedbackService.ajouterF(feedback);
            System.out.println("Feedback ajouté avec succès !");
        } else {
            System.out.println("Aucune réclamation valide sélectionnée.");
        }

        LocalDateTime dateFeedback = LocalDateTime.now();

        //--------------------- Modification Feedback --------------------------------------------
        List<Feedback> allFeedbacks = feedbackService.getallF();

        // Afficher les feedbacks et demander à l'utilisateur de choisir un feedback à modifier
        Feedback feedbackChoisi = choisirFeedback(allFeedbacks);

        if (feedbackChoisi != null) {
            // Demander à l'utilisateur de saisir les nouveaux détails du feedback
            Feedback nouveauxDetailsFeedback = saisirNouveauxDetailsFeedback();

            // Mettre à jour le feedback choisi avec les nouveaux détails
            feedbackChoisi.setMessage(nouveauxDetailsFeedback.getMessage());
            feedbackChoisi.setNote(nouveauxDetailsFeedback.getNote());
            feedbackChoisi.setDateFeedback(nouveauxDetailsFeedback.getDateFeedback());

            // Modifier le feedback dans la base de données
            feedbackService.modifierF(feedbackChoisi);
            System.out.println("Feedback modifié avec succès !");
        } else {
            System.out.println("Aucun feedback valide sélectionné.");
        }

        //--------------------- Suppression Feedback ---------------------------------------------
        feedbackService.supprimerF(feedbackChoisi);

        //--------------------- Afficher Feedback ------------------------------------------------

        for (Feedback FeedbackAff : allFeedbacks) {
            System.out.println("ID Feedback: " + FeedbackAff.getIdFeedback());
            System.out.println("Message: " + FeedbackAff.getMessage());
            System.out.println("Note: " + FeedbackAff.getNote());
            System.out.println("Date Feedback: " + FeedbackAff.getDateFeedback());
            System.out.println(FeedbackAff.getReclamation());
            System.out.println("-----------------------------");
        }
    }
}