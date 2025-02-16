package esprit.tn.service;

import esprit.tn.entities.Progress;
import esprit.tn.main.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProgressService implements ProgressServiceInterface<Progress> {
    Connection con ;
    public ProgressService() {
        con= DatabaseConnection.getInstance().getCon();
    }


    @Override
    public void update(Progress progress) {
        String req = "UPDATE progress SET statut_progression = ? WHERE id_progress = ?";

        try {
            PreparedStatement stm = con.prepareStatement(req);
            stm.setString(1, progress.getProgress().name());
            stm.setInt(2, progress.getIdProgress());

            stm.executeUpdate();
            System.out.println("Progress with ID " + progress.getIdProgress() + " updated successfully.");


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void create(Progress progress) {
        String req = "INSERT INTO progress (cours_id, eleve_id, statut_progression) VALUES (?, ?, ?)";

        try (PreparedStatement stm = con.prepareStatement(req)) {
            stm.setInt(1, progress.getIdCours());
            stm.setInt(2, progress.getIdEleve());
            stm.setString(3, progress.getProgress().name());
            stm.executeUpdate();
            System.out.println("Progress created successfully for Course ID: " + progress.getIdCours());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
