package esprit.tn.services;

import esprit.tn.entities.Exercice;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExerciceService {
    private Connection cnx;

    public ExerciceService(Connection cnx) {
        this.cnx = cnx;
    }

    public void ajouterExercice(Exercice exercice, int quiz_id) throws SQLException {
        String query = "INSERT INTO exercice (question, options, correctAnswer, score, imagePath, isMandatory, quiz_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = cnx.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, exercice.getQuestion());
            stmt.setString(2, String.join(",", exercice.getOptions()));
            stmt.setString(3, exercice.getCorrectAnswer());
            stmt.setInt(4, exercice.getScore());
            stmt.setString(5, exercice.getImagePath());
            stmt.setBoolean(6, exercice.isMandatory());
            stmt.setInt(7, quiz_id);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating Exercice failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    exercice.setIdE(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating Exercice failed, no ID obtained.");
                }
            }
        }
    }
    public List<Exercice> getExercicesByQuizId(int quizId) throws SQLException {
        List<Exercice> exercices = new ArrayList<>();
        String query = "SELECT * FROM exercice WHERE quiz_id = ?";
        PreparedStatement stmt = cnx.prepareStatement(query);
        stmt.setInt(1, quizId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Exercice exercice = new Exercice(
                    rs.getString("question"),
                    List.of(rs.getString("options").split(",")),
                    rs.getString("correctAnswer"),
                    rs.getInt("score"),
                    rs.getString("imagePath"),
                    rs.getBoolean("isMandatory")
            );
            exercice.setIdE(rs.getInt("idE"));
            exercices.add(exercice);
        }
        return exercices;
    }

    public void updateExercice(Exercice exercice) throws SQLException {
        String query = "UPDATE exercice SET question = ?, options = ?, correctAnswer = ?, score = ?, imagePath = ?, isMandatory = ? WHERE idE = ?";

        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, exercice.getQuestion());
            stmt.setString(2, String.join(",", exercice.getOptions()));
            stmt.setString(3, exercice.getCorrectAnswer());
            stmt.setInt(4, exercice.getScore());
            stmt.setString(5, exercice.getImagePath());
            stmt.setBoolean(6, exercice.isMandatory());
            stmt.setInt(7, exercice.getIdE());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating Exercice failed, no rows affected.");
            }
        }
    }

    public void deleteExercicesByQuizId(int quizId) throws SQLException {
        String query = "DELETE FROM exercice WHERE quiz_id = ?";
        PreparedStatement stmt = cnx.prepareStatement(query);
        stmt.setInt(1, quizId);
        stmt.executeUpdate();
    }
    public void deleteExerciceById(int exerciceId) throws SQLException {
        String query = "DELETE FROM exercice WHERE idE = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, exerciceId);
            stmt.executeUpdate();
        }
    }


    }

