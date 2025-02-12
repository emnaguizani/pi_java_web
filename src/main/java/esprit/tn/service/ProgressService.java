package esprit.tn.service;

import esprit.tn.entities.Progress;
import esprit.tn.main.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProgressService implements ProgressServiceInterface<Progress> {
    Connection con ;
    public ProgressService() {
        con= DatabaseConnection.instance.getCon();
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
}
