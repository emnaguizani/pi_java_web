package esprit.tn.services;

import esprit.tn.entities.Absence;
import esprit.tn.main.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbsenceService {

    private final Connection cnx;

    public AbsenceService() {
        cnx = DatabaseConnection.getInstance().getCnx();
    }

    public void ajouter(Absence absence) {
        // Vérifier si une absence existe déjà pour cet élève et cette séance
        if (existeAbsence(absence.getIdSeance(), absence.getIdEleve())) {
            System.err.println("⚠️ L'élève " + absence.getEleveFullName() + " a déjà une absence pour cette séance !");
            return;
        }

        String req = "INSERT INTO absence (idSeance, idEleve, Etat, commentaire) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stm = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            stm.setInt(1, absence.getIdSeance());
            stm.setInt(2, absence.getIdEleve());
            stm.setString(3, absence.getEtat());
            stm.setString(4, (absence.getCommentaire() == null || absence.getCommentaire().trim().isEmpty()) ? "Aucun commentaire" : absence.getCommentaire());

            int rowsInserted = stm.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = stm.getGeneratedKeys();
                if (generatedKeys.next()) {
                    absence.setIdAbsence(generatedKeys.getInt(1));
                }
                System.out.println("✅ Absence ajoutée avec succès !");
            } else {
                System.err.println("❌ L'ajout de l'absence a échoué !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean existeAbsence(int idSeance, int idEleve) {
        String req = "SELECT COUNT(*) FROM absence WHERE idSeance = ? AND idEleve = ?";
        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setInt(1, idSeance);
            stm.setInt(2, idEleve);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // S'il y a au moins 1 absence, retourne true
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }








    public void modifier(Absence absence) {
        String req = "UPDATE absence SET Etat = ?, commentaire = ? WHERE idAbsence = ?";
        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setString(1, absence.getEtat());
            stm.setString(2, (absence.getCommentaire() == null) ? "" : absence.getCommentaire());
            stm.setInt(3, absence.getIdAbsence());

            System.out.println("📝 Mise à jour de l'absence : " + absence);

            int rowsUpdated = stm.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Absence mise à jour !");
            } else {
                System.err.println("❌ Échec de la mise à jour !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





    public void supprimer(Absence absence) {
        String req = "DELETE FROM absence WHERE idAbsence = ?";
        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setInt(1, absence.getIdAbsence());
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Absence> getAll() {
        List<Absence> absences = new ArrayList<>();
        String req = "SELECT a.idAbsence, a.idSeance, a.idEleve, a.Etat, u.fullName " +
                "FROM absence a JOIN users u ON a.idEleve = u.id_user";

        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(req)) {

            while (rs.next()) {
                Absence absence = new Absence(
                        rs.getInt("idAbsence"),
                        rs.getInt("idSeance"),
                        rs.getInt("idEleve"),
                        rs.getString("Etat"),
                        rs.getString("fullName")
                );
                absences.add(absence);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return absences;
    }

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
                        rs.getString("Etat")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Absence> getAllWithUserDetails() {
        List<Absence> absences = new ArrayList<>();
        String req = """
        SELECT a.idAbsence, a.idSeance, a.idEleve, a.Etat, a.commentaire, u.fullName, s.titre AS seanceTitre
        FROM absence a
        JOIN users u ON a.idEleve = u.id_user
        JOIN seance s ON a.idSeance = s.idSeance
        ORDER BY s.titre, u.fullName;
    """;

        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(req)) {

            while (rs.next()) {
                Absence absence = new Absence(
                        rs.getInt("idAbsence"),
                        rs.getInt("idSeance"),
                        rs.getInt("idEleve"),
                        rs.getString("Etat"),
                        rs.getString("fullName"),
                        rs.getString("seanceTitre"),
                        rs.getString("commentaire")
                );
                absences.add(absence);
            }

            System.out.println("🔍 Absences récupérées : " + absences.size());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return absences;
    }




    public List<Absence> getAbsencesBySeance(int idSeance) {
        List<Absence> absences = new ArrayList<>();
        String req = """
    SELECT a.idAbsence, a.idSeance, a.idEleve, a.Etat, a.commentaire, u.fullName, s.titre AS seanceTitre
    FROM absence a
    JOIN users u ON a.idEleve = u.id_user
    JOIN seance s ON a.idSeance = s.idSeance
    WHERE a.idSeance = ?
    ORDER BY u.fullName;
    """;

        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setInt(1, idSeance);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Absence absence = new Absence(
                        rs.getInt("idAbsence"),
                        rs.getInt("idSeance"),
                        rs.getInt("idEleve"),
                        rs.getString("Etat"),
                        rs.getString("fullName"),
                        rs.getString("seanceTitre"),
                        rs.getString("commentaire")
                );
                absences.add(absence);
            }

            // ✅ LOG IMPORTANT : Vérifier si les absences sont récupérées
            System.out.println("🔎 Absences récupérées pour idSeance " + idSeance + " : " + absences.size());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return absences;
    }
    public List<Absence> getAbsencesForEleve(int idEleve) {
        List<Absence> absences = new ArrayList<>();
        String req = """
        SELECT a.idAbsence, a.idSeance, a.idEleve, a.Etat, a.commentaire, u.fullName, s.titre AS seanceTitre
        FROM absence a
        JOIN users u ON a.idEleve = u.id_user
        JOIN seance s ON a.idSeance = s.idSeance
        WHERE a.idEleve = ?
    """;

        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setInt(1, idEleve);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Absence absence = new Absence(
                        rs.getInt("idAbsence"),
                        rs.getInt("idSeance"),
                        rs.getInt("idEleve"),
                        rs.getString("Etat"),
                        rs.getString("fullName"),     // ✅ Nom de l'élève
                        rs.getString("seanceTitre"),  // ✅ Titre de la séance
                        rs.getString("commentaire")   // ✅ Commentaire
                );
                absences.add(absence);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return absences;
    }















}
