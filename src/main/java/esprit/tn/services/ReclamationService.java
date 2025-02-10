package esprit.tn.services;

import esprit.tn.entities.Feedback;
import esprit.tn.entities.Reclamation;
import esprit.tn.main.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReclamationService implements IserviceR<Reclamation> {

    Connection cnx;

    public ReclamationService(){

        cnx = DatabaseConnection.instance.getCnx();
    }

    @Override
    public void ajouter(Reclamation reclamation) {
        String req="INSERT INTO Reclamation (Titre, Description, Status, DateCreation) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement stm = cnx.prepareStatement(req);

            stm.setString(1, reclamation.getTitre());
            stm.setString(2, reclamation.getDescription());
            stm.setString(3, reclamation.getStatus());

            // Convertir LocalDateTime en Timestamp
            LocalDateTime dateCreation = reclamation.getDateCreation();
            if (dateCreation != null) {
                stm.setTimestamp(4, java.sql.Timestamp.valueOf(dateCreation)); // Conversion en Timestamp
            } else {
                stm.setNull(4, java.sql.Types.TIMESTAMP); // Gérer le cas où dateCreation est null
            }
            stm.executeUpdate();
            System.out.println("Réclamation ajoutée avec succès !");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void modifier(Reclamation reclamation) {
        String req = "UPDATE Reclamation SET Titre = ?, Description = ?, Status = ?, DateCreation = ? WHERE Id = ?";

        try {
            PreparedStatement stm = cnx.prepareStatement(req);

            stm.setString(1, reclamation.getTitre());
            stm.setString(2, reclamation.getDescription());
            stm.setString(3, reclamation.getStatus());

            // Convertir LocalDateTime en Timestamp
            LocalDateTime dateCreation = reclamation.getDateCreation();
            if (dateCreation != null) {
                stm.setTimestamp(4, java.sql.Timestamp.valueOf(dateCreation)); // Conversion en Timestamp
            } else {
                stm.setNull(4, java.sql.Types.TIMESTAMP); // Gérer le cas où dateCreation est null
            }

            stm.setInt(5, reclamation.getId()); // ID de la réclamation à modifier
            stm.executeUpdate();
            System.out.println("Réclamation modifiée avec succès !");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void supprimer(Reclamation reclamation) {
        String req = "DELETE FROM Reclamation WHERE Id = ?";

        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setInt(1, reclamation.getId()); // ID de la réclamation à supprimer
            stm.executeUpdate();
            System.out.println("Réclamation supprimée avec succès !");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Reclamation> getall() {
        List<Reclamation> ps = new ArrayList<>();
        String req = "SELECT * FROM Reclamation";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(req);

            while (rs.next()){
                Reclamation p = new Reclamation();
                p.setId(rs.getInt("Id"));
                p.setTitre(rs.getString("Titre"));
                p.setDescription(rs.getString("Description"));
                p.setStatus(rs.getString("Status"));
                java.sql.Timestamp timestamp = rs.getTimestamp("DateCreation");
                if (timestamp != null) {
                    p.setDateCreation(timestamp.toLocalDateTime());
                }
                ps.add(p);
            }

            System.out.println(ps);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ps;
    }

    public static Reclamation choisirReclamation(List<Reclamation> reclamations) {
        Scanner scanner = new Scanner(System.in);

        // Afficher la liste des réclamations
        System.out.println("Liste des réclamations disponibles :");
        for (Reclamation reclamation : reclamations) {
            System.out.println("ID: " + reclamation.getId() + " | Titre: " + reclamation.getTitre());
        }

        // Demander à l'utilisateur de choisir une réclamation par son ID
        System.out.print("Entrez l'ID de la réclamation à laquelle ajouter un feedback : ");
        int idReclamation = scanner.nextInt();
        scanner.nextLine(); // Pour consommer la nouvelle ligne après nextInt()

        // Trouver la réclamation correspondante
        for (Reclamation reclamation : reclamations) {
            if (reclamation.getId() == idReclamation) {
                return reclamation;
            }
        }

        // Si aucune réclamation n'est trouvée
        System.out.println("Aucune réclamation trouvée avec l'ID " + idReclamation);
        return null;
    }

    @Override
    public List<Reclamation> rechercherRec(String critere) {
        List<Reclamation> reclamations = new ArrayList<>();
        String req = "SELECT * FROM Reclamation WHERE Titre LIKE ? OR Description LIKE ? OR Status LIKE ?";

        try {
            PreparedStatement stm = cnx.prepareStatement(req);

            // Ajouter des wildcards (%) pour rechercher des correspondances partielles
            stm.setString(1, "%" + critere + "%");
            stm.setString(2, "%" + critere + "%");
            stm.setString(3, "%" + critere + "%");

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Reclamation reclamation = new Reclamation();
                reclamation.setId(rs.getInt("Id"));
                reclamation.setTitre(rs.getString("Titre"));
                reclamation.setDescription(rs.getString("Description"));
                reclamation.setStatus(rs.getString("Status"));
                reclamation.setDateCreation(rs.getTimestamp("DateCreation").toLocalDateTime());

                reclamations.add(reclamation);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche des réclamations", e);
        }

        return reclamations;
    }

    public static Reclamation saisirReclamation() {
        Scanner scanner = new Scanner(System.in);

        // Demander à l'utilisateur de saisir le message du feedback
        System.out.print("Entrez le titre de la réclamation : ");
        String titre = scanner.nextLine();

        // Demander à l'utilisateur de saisir la note du feedback
        System.out.print("Entrez la description de la réclamation : ");
        String description = scanner.nextLine();
        scanner.nextLine();

        System.out.print("Entrez le status de la réclamation : ");
        String status = scanner.nextLine();
        scanner.nextLine();

        // Créer et retourner un nouveau feedback
        LocalDateTime dateCreation = LocalDateTime.now(); // Date actuelle
        return new Reclamation(titre, description, status, dateCreation);
    }

    public static void afficherReclamations(List<Reclamation> reclamations) {
        System.out.println("Liste des réclamations disponibles :");
        for (Reclamation reclamation : reclamations) {
            System.out.println("ID: " + reclamation.getId() + " | Message: " + reclamation.getTitre() + " | Note: " + reclamation.getDescription());
        }
    }

    public static Reclamation choixReclamation(List<Reclamation> reclamations) {
        Scanner scanner = new Scanner(System.in);

        // Afficher la liste des feedbacks
        afficherReclamations(reclamations);

        // Demander à l'utilisateur de choisir un feedback par son ID
        System.out.print("Entrez l'ID de la réclamation : ");
        int idReclamation = scanner.nextInt();
        scanner.nextLine(); // Pour consommer la nouvelle ligne après nextInt()

        // Trouver le feedback correspondant
        for (Reclamation reclamation : reclamations) {
            if (reclamation.getId() == idReclamation) {
                return reclamation;
            }
        }

        // Si aucun feedback n'est trouvé
        System.out.println("Aucun feedback trouvé avec l'ID " + idReclamation);
        return null;
    }
}