package esprit.tn.services;

import esprit.tn.entities.Seance;
import esprit.tn.main.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeanceService implements Iservice<Seance> {
    private Connection cnx;

    public SeanceService() {
        cnx = DatabaseConnection.getInstance().getCnx();
    }

    @Override
    public void ajouter(Seance seance) {
        String req = "INSERT INTO seance (titre, contenu, datetime, idFormateur, nomFormateur, modeSeance) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setString(1, seance.getTitre());
            stm.setString(2, seance.getContenu());
            stm.setTimestamp(3, seance.getDatetime());
            stm.setInt(4, seance.getIdFormateur());
            stm.setString(5, seance.getNomFormateur());
            stm.setString(6, seance.getModeSeance()); // ✅ Ajout du mode

            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void modifier(Seance seance) {
        String req = "UPDATE seance SET titre = ?, contenu = ?, datetime = ?, idFormateur = ? WHERE idSeance = ?";
        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setString(1, seance.getTitre());
            stm.setString(2, seance.getContenu());
            stm.setTimestamp(3, seance.getDatetime());
            stm.setInt(4, seance.getIdFormateur());
            stm.setInt(5, seance.getIdSeance());
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void supprimer(Seance seance) {
        String query = "DELETE FROM seance WHERE idSeance = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, seance.getIdSeance());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Seance> getAll() {
        List<Seance> seances = new ArrayList<>();
        String query = "SELECT s.idSeance, s.titre, s.contenu, s.datetime, s.idFormateur, s.nomFormateur, s.modeSeance FROM seance s";

        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(query)) {

            while (rs.next()) {
                Seance seance = new Seance(
                        rs.getInt("idSeance"),
                        rs.getString("titre"),
                        rs.getString("contenu"),
                        rs.getTimestamp("datetime"),
                        rs.getInt("idFormateur"),
                        rs.getString("nomFormateur"),
                        rs.getString("modeSeance") // ✅ Récupération du mode de la séance
                );
                seances.add(seance);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des séances !");
            e.printStackTrace();
        }
        return seances;
    }


    public List<Seance> getAllSeances() {
        List<Seance> seances = new ArrayList<>();
        String req = """
    SELECT s.idSeance, s.titre, s.contenu, s.datetime, s.idFormateur, u.fullName AS nomFormateur
    FROM seance s
    JOIN users u ON s.idFormateur = u.id_user
    ORDER BY s.datetime ASC
""";


        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(req)) {

            while (rs.next()) {
                Seance seance = new Seance(
                        rs.getInt("idSeance"),
                        rs.getString("titre"),
                        rs.getString("contenu"),
                        rs.getTimestamp("datetime"),
                        rs.getInt("idFormateur"),
                        rs.getString("nomFormateur") // ✅ Correction ici
                );

                seances.add(seance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
