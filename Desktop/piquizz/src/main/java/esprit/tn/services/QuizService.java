package esprit.tn.services;

import esprit.tn.entities.Quiz;
import esprit.tn.entities.Exercice;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class QuizService {
    private Connection cnx;

    public QuizService(Connection cnx) {

        this.cnx = cnx;
    }

    public int ajouterQuiz(Quiz quiz) throws SQLException {
        String query = "INSERT INTO quiz (title, description, duration, totalScore, creationDate, author) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = cnx.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, quiz.getTitle());
        stmt.setString(2, quiz.getDescription());
        stmt.setInt(3, quiz.getDuration());
        stmt.setInt(4, quiz.getTotalScore());
        stmt.setTimestamp(5, Timestamp.valueOf(quiz.getCreationDate()));
        stmt.setString(6, quiz.getAuthor());

        int rowsInserted = stmt.executeUpdate();
        if (rowsInserted == 0) {
            throw new SQLException("Creating quiz failed, no rows affected.");
        }

        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            int generatedId = rs.getInt(1);
            quiz.setQuiz_id(generatedId);
            System.out.println("Generated ID: " + generatedId);
            return generatedId;
        } else {
            throw new SQLException("Creating quiz failed, no ID obtained.");
        }
    }


    public Quiz getQuizById(int id) throws SQLException {
        String query = "SELECT * FROM quiz WHERE quiz_id = ?";
        PreparedStatement stmt = cnx.prepareStatement(query);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            List<Exercice> exercices = new ExerciceService(cnx).getExercicesByQuizId(id);

            return new Quiz(
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getInt("duration"),
                    rs.getInt("totalScore"),
                    rs.getTimestamp("creationDate").toLocalDateTime(),
                    rs.getString("author"),
                    exercices
            );
        }
        return null;
    }

    public List<Quiz> getAllQuizzes() throws SQLException {
        List<Quiz> quizzes = new ArrayList<>();
        String query = "SELECT * FROM quiz";
        try (PreparedStatement stmt = cnx.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            ExerciceService exerciceService = new ExerciceService(cnx);
            while (rs.next()) {
                List<Exercice> exercices = exerciceService.getExercicesByQuizId(rs.getInt("quiz_id"));

                Quiz quiz = new Quiz(
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getInt("duration"),
                        rs.getInt("totalScore"),
                        rs.getTimestamp("creationDate").toLocalDateTime(),
                        rs.getString("author"),
                        exercices
                );

                quiz.setQuiz_id(rs.getInt("quiz_id"));
                quizzes.add(quiz);
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            throw e;
        }
        return quizzes;
    }


    public void updateQuiz(Quiz quiz) throws SQLException {
        String query = "UPDATE quiz SET title = ?, description = ?, duration = ?, totalScore = ?, creationDate = ?, author = ? WHERE quiz_id = ?";
        PreparedStatement stmt = cnx.prepareStatement(query);
        stmt.setString(1, quiz.getTitle());
        stmt.setString(2, quiz.getDescription());
        stmt.setInt(3, quiz.getDuration());
        stmt.setInt(4, quiz.getTotalScore());
        stmt.setTimestamp(5, Timestamp.valueOf(quiz.getCreationDate()));
        stmt.setString(6, quiz.getAuthor());
        stmt.setInt(7, quiz.getQuiz_id());
        stmt.executeUpdate();


    }

    public void deleteQuiz(int id) throws SQLException {
        ExerciceService exerciceService = new ExerciceService(cnx);
        exerciceService.deleteExercicesByQuizId(id);

        String query = "DELETE FROM quiz WHERE quiz_id = ?";
        PreparedStatement stmt = cnx.prepareStatement(query);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }


}
