package esprit.tn.services;
import esprit.tn.main.DatabaseConnection;
import esprit.tn.entities.Reclamation;
import esprit.tn.entities.Feedback;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FeedbackService implements IserviceF<Feedback> {

    Connection cnx;

    public FeedbackService() {
        cnx = DatabaseConnection.instance.getCnx();
    }

    @Override
    public void ajouterF(Feedback feedback) {

        // Si la réclamation existe, insérer le feedback
        String req = "INSERT INTO Feedback (TypeFeedback, Message, Note, PieceJointeF, DateFeedback, ReclamationId) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement stm = cnx.prepareStatement(req);

            stm.setString(1, feedback.getTypeFeedback());
            stm.setString(2, feedback.getMessage());
            stm.setInt(3, feedback.getNote());
            stm.setString(4, feedback.getPieceJointeF());
            stm.setTimestamp(5, java.sql.Timestamp.valueOf(feedback.getDateFeedback()));
            stm.setInt(6, feedback.getReclamation().getId());

            if (!feedback.getTypeFeedback().equalsIgnoreCase("Positif") & !feedback.getTypeFeedback().equalsIgnoreCase("Correctif") && !feedback.getTypeFeedback().equalsIgnoreCase("Négatif")) {
                throw new IllegalArgumentException("Le Type du feedback doit être 'Positif' ou 'Correctif' ou 'Négatif'.");
            }

            if (feedback.getNote() < 1 || feedback.getNote() > 5) {
                throw new IllegalArgumentException("La note doit être comprise entre 1 et 5.");
            }

            stm.executeUpdate();
            System.out.println("Feedback ajouté avec succès !");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout du feedback", e);
        }
    }

    @Override
    public void modifierF(Feedback feedback) {
        String req = "UPDATE Feedback SET TypeFeedback = ?, Message = ?, Note = ?, PieceJointeF = ?, DateFeedback = ?, ReclamationId = ? WHERE IdFeedback = ?";

        try {
            PreparedStatement stm = cnx.prepareStatement(req);

            // Définir les paramètres de la requête
            stm.setString(1, feedback.getTypeFeedback());
            stm.setString(2, feedback.getMessage());
            stm.setInt(3, feedback.getNote());
            stm.setString(4, feedback.getPieceJointeF());
            stm.setTimestamp(5, java.sql.Timestamp.valueOf(feedback.getDateFeedback()));
            stm.setInt(6, feedback.getReclamation().getId()); // Clé étrangère vers Reclamation
            stm.setInt(7, feedback.getIdFeedback()); // ID du feedback à modifier

            if (!feedback.getTypeFeedback().equalsIgnoreCase("Positif") & !feedback.getTypeFeedback().equalsIgnoreCase("Correctif") && !feedback.getTypeFeedback().equalsIgnoreCase("Négatif")) {
                throw new IllegalArgumentException("Le Type du feedback doit être 'Positif' ou 'Correctif' ou 'Négatif'.");
            }

            if (feedback.getNote() < 1 || feedback.getNote() > 5) {
                throw new IllegalArgumentException("La note doit être comprise entre 1 et 5.");
            }

            stm.executeUpdate();
            System.out.println("Feedback modifié avec succès !");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la modification du feedback", e);
        }
    }

    @Override
    public void supprimerF(Feedback feedback) {
        String req = "DELETE FROM Feedback WHERE IdFeedback = ?";

        try {
            PreparedStatement stm = cnx.prepareStatement(req);

            // Définir le paramètre de la requête
            stm.setInt(1, feedback.getIdFeedback()); // ID du feedback à supprimer

            stm.executeUpdate();
            System.out.println("Feedback supprimé avec succès !");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du feedback", e);
        }
    }

    @Override
    public List<Feedback> getallF() {
        List<Feedback> feedbacks = new ArrayList<>();
        String req = "SELECT * FROM Feedback";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(req);

            while (rs.next()) {
                Feedback feedback = new Feedback();
                feedback.setIdFeedback(rs.getInt("IdFeedback"));
                feedback.setTypeFeedback(rs.getString("TypeFeedback"));
                feedback.setMessage(rs.getString("Message"));
                feedback.setNote(rs.getInt("Note"));
                feedback.setPieceJointeF(rs.getString("PieceJointeF"));
                feedback.setDateFeedback(rs.getTimestamp("DateFeedback").toLocalDateTime());

                // Récupérer la réclamation associée
                Reclamation reclamation = new Reclamation();
                reclamation.setId(rs.getInt("ReclamationId"));
                feedback.setReclamation(reclamation);

                feedbacks.add(feedback);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des feedbacks", e);
        }
        return feedbacks;
    }

    public static void afficherFeedbacks(List<Feedback> feedbacks) {
        System.out.println("Liste des feedbacks disponibles :");
        for (Feedback feedback : feedbacks) {
            System.out.println("ID: " + feedback.getIdFeedback() + " | Type de feedback: " + feedback.getTypeFeedback() + " | Message: " + feedback.getMessage() + " | Note: " + feedback.getNote() + " | Piece Jointe: " + feedback.getPieceJointeF());
        }
    }

    public static Feedback choisirFeedback(List<Feedback> feedbacks) {
        Scanner scanner = new Scanner(System.in);

        // Afficher la liste des feedbacks
        afficherFeedbacks(feedbacks);

        // Demander à l'utilisateur de choisir un feedback par son ID
        System.out.print("Entrez l'ID du feedback : ");
        int idFeedback = scanner.nextInt();
        scanner.nextLine(); // Pour consommer la nouvelle ligne après nextInt()

        // Trouver le feedback correspondant
        for (Feedback feedback : feedbacks) {
            if (feedback.getIdFeedback() == idFeedback) {
                return feedback;
            }
        }

        // Si aucun feedback n'est trouvé
        System.out.println("Aucun feedback trouvé avec l'ID " + idFeedback);
        return null;
    }

    public static Feedback saisirFeedback(Reclamation reclamation) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Entrez le type du feedback : ");
        String typeFeedback = scanner.nextLine();

        // Demander à l'utilisateur de saisir le message du feedback
        System.out.print("Entrez le message du feedback : ");
        String message = scanner.nextLine();

        // Demander à l'utilisateur de saisir la note du feedback
        System.out.print("Entrez la note du feedback (entre 1 et 5) : ");
        int note = scanner.nextInt();
        scanner.nextLine(); // Pour consommer la nouvelle ligne après nextInt()

        System.out.print("Entrez la piece jointe du feedback : ");
        String piecejointeF = scanner.nextLine();

        // Créer et retourner un nouveau feedback
        LocalDateTime dateFeedback = LocalDateTime.now(); // Date actuelle
        return new Feedback(typeFeedback, message, note, piecejointeF, dateFeedback, reclamation);
    }

    public static Feedback saisirNouveauxDetailsFeedback() {
        Scanner scanner = new Scanner(System.in);

        // Demander à l'utilisateur de saisir le nouveau message du feedback
        System.out.print("Entrez le nouveau type du feedback : ");
        String typeFeedback = scanner.nextLine();

        // Demander à l'utilisateur de saisir le nouveau message du feedback
        System.out.print("Entrez le nouveau message du feedback : ");
        String message = scanner.nextLine();

        // Demander à l'utilisateur de saisir la nouvelle note du feedback
        System.out.print("Entrez la nouvelle note du feedback (entre 1 et 5) : ");
        int note = scanner.nextInt();
        scanner.nextLine(); // Pour consommer la nouvelle ligne après nextInt()

        System.out.print("Entrez la nouvelle piece jointe du feedback : ");
        String piecejointeF = scanner.nextLine();

        // Créer et retourner un nouveau feedback avec les nouveaux détails
        LocalDateTime dateFeedback = LocalDateTime.now(); // Date actuelle
        Feedback feedback = new Feedback();
        feedback.setTypeFeedback(typeFeedback);
        feedback.setMessage(message);
        feedback.setNote(note);
        feedback.setPieceJointeF(piecejointeF);
        feedback.setDateFeedback(dateFeedback);

        return feedback;
    }
}