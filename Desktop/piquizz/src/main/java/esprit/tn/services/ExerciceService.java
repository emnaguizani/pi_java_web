package esprit.tn.services;

import esprit.tn.entities.Exercice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExerciceService {
    private Connection cnx;

    public ExerciceService(Connection cnx) {
        this.cnx = cnx;
    }

    public void ajouterExercice(Exercice exercice) throws SQLException {
        String query = "INSERT INTO exercice (question, options, answer, correctAnswer, score, imagePath, isMandatory, quiz_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = cnx.prepareStatement(query);
        stmt.setString(1, exercice.getQuestion());
        stmt.setString(2, String.join(",", exercice.getOptions()));
        stmt.setString(3, exercice.getAnswer());
        stmt.setString(4, exercice.getCorrectAnswer());
        stmt.setInt(5, exercice.getScore());
        stmt.setString(6, exercice.getImagePath());
        stmt.setBoolean(7, exercice.isMandatory());
        stmt.setInt(8, exercice.getquiz_id());
        stmt.executeUpdate();
    }

    public List<Exercice> getExercicesByQuizId(int quizId) throws SQLException {
        List<Exercice> exercices = new ArrayList<>();
        String query = "SELECT * FROM exercice WHERE quiz_id = ?";
        PreparedStatement stmt = cnx.prepareStatement(query);
        stmt.setInt(1, quizId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            exercices.add(new Exercice(
                    rs.getString("question"),
                    List.of(rs.getString("options").split(",")),
                    rs.getString("answer"),
                    rs.getString("correctAnswer"),
                    rs.getInt("score"),
                    rs.getString("imagePath"),
                    rs.getBoolean("isMandatory"),
                    rs.getInt("quiz_id")
            ));
        }
        return exercices;
    }

    public void updateExercice(Exercice exercice) throws SQLException {
        String query = "UPDATE exercice SET question = ?, options = ?, answer = ?, correctAnswer = ?, score = ?, imagePath = ?, isMandatory = ?, quiz_id = ? WHERE idE = ?";
        PreparedStatement stmt = cnx.prepareStatement(query);
        stmt.setString(1, exercice.getQuestion());
        stmt.setString(2, String.join(",", exercice.getOptions()));
        stmt.setString(3, exercice.getAnswer());
        stmt.setString(4, exercice.getCorrectAnswer());
        stmt.setInt(5, exercice.getScore());
        stmt.setString(6, exercice.getImagePath());
        stmt.setBoolean(7, exercice.isMandatory());
        stmt.setInt(8, exercice.getquiz_id());
        stmt.setInt(9, exercice.getIdE());
        stmt.executeUpdate();
    }

    public void deleteExercice(int id) throws SQLException {
        String query = "DELETE FROM exercice WHERE idE = ?";
        PreparedStatement stmt = cnx.prepareStatement(query);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }

    public void deleteExercicesByQuizId(int quizId) throws SQLException {
        String query = "DELETE FROM exercice WHERE quiz_id = ?";
        PreparedStatement stmt = cnx.prepareStatement(query);
        stmt.setInt(1, quizId);
        stmt.executeUpdate();
    }




}
