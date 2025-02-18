package esprit.tn.services;

import esprit.tn.entities.Seance;
import esprit.tn.main.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeanceService implements InterfaceSeance<Seance> {
    private Connection cnx;

    public SeanceService() {
        cnx = DatabaseConnection.getInstance().getCnx();
    }

    @Override
    public void ajouter(Seance seance) {
        String req = "INSERT INTO seance (titre, contenu, datetime, idFormateur) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setString(1, seance.getTitre());
            stm.setString(2, seance.getContenu());
            stm.setTimestamp(3, seance.getDatetime());
            stm.setInt(4, seance.getIdFormateur());
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

    @Override
    public List<Seance> getAll() {
        List<Seance> seances = new ArrayList<>();
        String req = "SELECT * FROM seance";
        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(req)) {
            while (rs.next()) {
                Seance seance = new Seance(
                        rs.getInt("idSeance"),
                        rs.getString("titre"),
                        rs.getString("contenu"),
                        rs.getTimestamp("datetime"),
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
