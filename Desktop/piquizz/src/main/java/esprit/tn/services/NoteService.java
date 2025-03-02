package esprit.tn.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NoteService {
    private Connection cnx;

    public NoteService(Connection cnx) {
        this.cnx = cnx;
    }

    public double calculerNoteQuiz(int eleveId, int quizId) throws SQLException {
        double noteTotale = 0;
        String query = "SELECT SUM(noteExercice) AS noteTotale FROM reponse r " +
                "JOIN exercice e ON r.exerciceId = e.idE " +
                "WHERE r.eleveId = ? AND e.quiz_id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, eleveId);
            stmt.setInt(2, quizId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                noteTotale = rs.getDouble("noteTotale");
            }
        }
        return noteTotale;
    }

    public boolean noteExiste(int eleveId, int quizId) throws SQLException {
        String query = "SELECT COUNT(*) FROM note WHERE eleveId = ? AND quizId = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, eleveId);
            stmt.setInt(2, quizId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        }
        return false;
    }

    public void ajouterNote(int eleveId, int quizId, double noteTotale) throws SQLException {
        String query = "INSERT INTO note (eleveId, quizId, noteTotale) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, eleveId);
            stmt.setInt(2, quizId);
            stmt.setDouble(3, noteTotale);
            stmt.executeUpdate();
        }
    }

    public void mettreAJourNote(int eleveId, int quizId, double nouvelleNoteTotale) throws SQLException {
        String query = "UPDATE note SET noteTotale = ? WHERE eleveId = ? AND quizId = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setDouble(1, nouvelleNoteTotale);
            stmt.setInt(2, eleveId);
            stmt.setInt(3, quizId);
            stmt.executeUpdate();
        }
    }

    public List<String> getAllNotes() throws SQLException {
        List<String> notes = new ArrayList<>();
        String query = "SELECT eleveId, quizId, noteTotale FROM note";
        try (PreparedStatement stmt = cnx.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int eleveId = rs.getInt("eleveId");
                int quizId = rs.getInt("quizId");
                double noteTotale = rs.getDouble("noteTotale");
                notes.add("Élève ID: " + eleveId + ", Quiz ID: " + quizId + ", Note: " + noteTotale);
            }
        }
        return notes;
    }

    public List<String> getNotesByEleveId(int eleveId) throws SQLException {
        List<String> notes = new ArrayList<>();
        String query = "SELECT quizId, noteTotale FROM note WHERE eleveId = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, eleveId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int quizId = rs.getInt("quizId");
                double noteTotale = rs.getDouble("noteTotale");
                notes.add("Quiz ID: " + quizId + ", Note: " + noteTotale);
            }
        }
        return notes;
    }
}