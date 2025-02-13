package esprit.tn.services;
import java.util.List;
import java.util.ArrayList;
import esprit.tn.main.DatabaseConnection;

import esprit.tn.entities.Seance;

import java.sql.*;

public class SeanceService implements Iservice<Seance> {
    private Connection cnx;

    public SeanceService() {
        cnx = DatabaseConnection.getInstance().getCnx();
    }

    @Override
    public void ajouter(Seance seance) {
        String req = "INSERT INTO seance (titre, Contenu, Datetime, idFormateur) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setString(1, seance.getTitre());
            stm.setString(2, seance.getContenu());  // Change 'description' to 'Contenu'
            stm.setTimestamp(3, seance.getDatetime());
            stm.setInt(4, seance.getIdFormateur());
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void modifier(Seance seance) {
        String req = "UPDATE seance SET titre = ?, Contenu = ?, Datetime = ?, idFormateur = ? WHERE idSeance = ?";
        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setString(1, seance.getTitre());
            stm.setString(2, seance.getContenu());  // Change 'description' to 'Contenu'
            stm.setTimestamp(3, seance.getDatetime());
            stm.setInt(4, seance.getIdFormateur());
            stm.setInt(5, seance.getIdSeance());
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void supprimer(Seance seance) { // ✅ Correct
        String query = "DELETE FROM seance WHERE idSeance = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, seance.getIdSeance()); // Utilisation de l'ID de l'objet
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    // Méthode pour supprimer toutes les séances
    public void supprimerToutesLesSeances() {
        String req = "DELETE FROM seance";
        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            int affectedRows = stm.executeUpdate();
            System.out.println("✅ " + affectedRows + " séance(s) supprimée(s) avec succès.");
        } catch (SQLException e) {
            throw new RuntimeException("❌ Erreur lors de la suppression des séances : " + e.getMessage());
        }
    }


    @Override
    public List<Seance> getAll() {
        List<Seance> seances = new ArrayList<>();
        String req = "SELECT * FROM seance";
        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(req)) {
            while (rs.next()) {
                Seance seance = new Seance(
                        rs.getInt("idSeance"),           // Assurez-vous que les noms de colonne sont corrects
                        rs.getString("titre"),
                        rs.getString("contenu"),
                        rs.getTimestamp("datetime"),     // Récupération correcte du Timestamp
                        rs.getInt("idFormateur")
                );
                seances.add(seance);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return seances;
    }





    @Override
    public Seance getone(int id) {
        String req = "SELECT * FROM seance WHERE idSeance = ?";
        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return new Seance(
                        rs.getInt("idSeance"),
                        rs.getString("titre"),
                        rs.getString("contenu"),
                        rs.getTimestamp("datetime"),
                        rs.getInt("idFormateur")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
