package esprit.tn.services;

import esprit.tn.entities.Absence;
import esprit.tn.main.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbsenceService implements Iservice<Absence> {
    private Connection cnx;

    public AbsenceService() {
        cnx = DatabaseConnection.getInstance().getCnx();
    }

    @Override
    public void ajouter(Absence absence) {
        String req = "INSERT INTO absence (idSeance, idEleve, Etat) VALUES (?, ?, ?)";
        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setInt(1, absence.getIdSeance());
            stm.setInt(2, absence.getIdEleve());
            stm.setString(3, absence.getEtat());
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void modifier(Absence absence) {
        String req = "UPDATE absence SET idSeance = ?, idEleve = ?, Etat = ? WHERE idAbsence = ?";
        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setInt(1, absence.getIdSeance());
            stm.setInt(2, absence.getIdEleve());
            stm.setString(3, absence.getEtat());
            stm.setInt(4, absence.getIdAbsence());
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void supprimer(Absence absence) {
        String req = "DELETE FROM absence WHERE idAbsence = ?";
        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setInt(1, absence.getIdAbsence());
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Absence> getAll() {
        List<Absence> absences = new ArrayList<>();
        String req = "SELECT * FROM absence";
        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(req)) {
            while (rs.next()) {
                Absence absence = new Absence(
                        rs.getInt("idAbsence"),
                        rs.getInt("idSeance"),
                        rs.getInt("idEleve"),
                        rs.getString("Etat")
                );
                absences.add(absence);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return absences;
    }

    @Override
    public Absence getone(int id) {
        String req = "SELECT * FROM absence WHERE idAbsence = ?";
        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return new Absence(
                        rs.getInt("idAbsence"),
                        rs.getInt("idSeance"),
                        rs.getInt("idEleve"),
                        rs.getString("etat")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
