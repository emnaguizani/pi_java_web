package esprit.tn.services;

import esprit.tn.entities.Reponse;
import esprit.tn.entities.Exercice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReponseService {
    private Connection cnx;
    private ExerciceService exerciceService;

    public ReponseService(Connection cnx) {
        this.cnx = cnx;
        this.exerciceService = new ExerciceService(cnx);
    }

    /**
     * Ajoute une réponse et calcule automatiquement la note en fonction de la réponse correcte.
     */
    public void ajouterReponse(Reponse reponse) throws SQLException {
        Exercice exercice = exerciceService.getExerciceById(reponse.getExerciceId());

        double noteExercice = 0;
        if (exercice != null && reponse.getReponse().equalsIgnoreCase(exercice.getCorrectAnswer())) {
            noteExercice = exercice.getScore();
        }

        reponse.setNoteExercice(noteExercice);

        String query = "INSERT INTO reponse (exerciceId, reponse, noteExercice, eleveId) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, reponse.getExerciceId());
            stmt.setString(2, reponse.getReponse());
            stmt.setDouble(3, reponse.getNoteExercice());
            stmt.setInt(4, reponse.getEleveId());
            stmt.executeUpdate();
        }
    }


    public List<Reponse> getReponsesByEleveId(int eleveId) throws SQLException {
        List<Reponse> reponses = new ArrayList<>();
        String query = "SELECT * FROM reponse WHERE eleveId = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, eleveId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Reponse reponse = new Reponse(
                        rs.getInt("exerciceId"),
                        rs.getString("reponse"),
                        rs.getInt("eleveId")
                );
                reponses.add(reponse);
            }
        }
        return reponses;
    }
}