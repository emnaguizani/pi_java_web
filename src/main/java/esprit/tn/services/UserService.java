package esprit.tn.services;

import esprit.tn.entities.Users;
import esprit.tn.main.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService implements Iservice<Users> {

    private Connection cnx;

    public UserService() {
        cnx = DatabaseConnection.getInstance().getCnx();
    }

    @Override
    public void ajouter(Users users) {
        String req = "INSERT INTO users (fullName, email, dateOfBirth, password, role) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setString(1, users.getFullName());
            stm.setString(2, users.getEmail());
            stm.setDate(3, new java.sql.Date(users.getDateOfBirth().getTime()));
            stm.setString(4, users.getPassword());
            stm.setString(5, users.getRole());

            stm.executeUpdate();
            System.out.println("✅ Utilisateur ajouté avec succès !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout de l'utilisateur !");
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Users users) {
        String req = "UPDATE users SET fullName = ?, email = ?, password = ?, role = ? WHERE id_user = ?";
        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setString(1, users.getFullName());
            stm.setString(2, users.getEmail());
            stm.setString(3, users.getPassword());
            stm.setString(4, users.getRole());
            stm.setInt(5, users.getId_user());

            int rowsAffected = stm.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Utilisateur mis à jour avec succès !");
            } else {
                System.out.println("⚠️ Utilisateur avec ID " + users.getId_user() + " non trouvé.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la mise à jour de l'utilisateur !");
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(Users users) {
        String req = "DELETE FROM users WHERE id_user = ?";
        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setInt(1, users.getId_user());

            int rowsAffected = stm.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Utilisateur supprimé avec succès !");
            } else {
                System.out.println("⚠️ Utilisateur avec ID " + users.getId_user() + " non trouvé.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression de l'utilisateur !");
            e.printStackTrace();
        }
    }

    @Override
    public List<Users> getAll() {
        List<Users> usersList = new ArrayList<>();
        String req = "SELECT * FROM users";

        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(req)) {

            while (rs.next()) {
                Users user = new Users();
                user.setId_user(rs.getInt("id_user"));
                user.setFullName(rs.getString("fullName"));
                user.setEmail(rs.getString("email"));
                user.setDateOfBirth(rs.getDate("dateOfBirth"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));

                usersList.add(user);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des utilisateurs !");
            e.printStackTrace();
        }

        return usersList;
    }


    @Override
    public Users getone(int id) {
        String req = "SELECT * FROM users WHERE id_user = ?";
        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return new Users(
                        rs.getInt("id_user"),
                        rs.getString("fullName"),
                        rs.getString("email"),
                        rs.getDate("dateOfBirth"),
                        rs.getString("password"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération de l'utilisateur !");
            e.printStackTrace();
        }
        return null;
    }
    public List<String> getEmailsParticipants() {
        List<String> emails = new ArrayList<>();
        String query = "SELECT email FROM users WHERE role = 'eleve' OR role = 'formateur'";

        try (PreparedStatement stm = cnx.prepareStatement(query);
             ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                emails.add(rs.getString("email"));
            }

            System.out.println("✅ " + emails.size() + " emails récupérés.");

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des emails !");
            e.printStackTrace();
        }

        return emails;
    }

    /**
     * ✅ Méthode pour récupérer uniquement les élèves depuis la base de données.
     */
    public List<Users> getAllEleves() {
        List<Users> eleves = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role = 'eleve'"; // Filtrer par rôle "eleve"

        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(query)) {

            while (rs.next()) {
                Users eleve = new Users();
                eleve.setId_user(rs.getInt("id_user"));
                eleve.setFullName(rs.getString("fullName"));
                eleve.setEmail(rs.getString("email"));
                eleve.setDateOfBirth(rs.getDate("dateOfBirth"));
                eleve.setRole(rs.getString("role"));

                eleves.add(eleve);
            }
        } catch (SQLException e) {
            throw new RuntimeException("❌ Erreur lors de la récupération des élèves", e);
        }

        return eleves;
    }
    public List<Users> getElevesBySeance(int idSeance) {
        List<Users> eleves = new ArrayList<>();

        // Récupérer uniquement les élèves (rôle = 'eleve')
        String query = """
        SELECT id_user, fullName, email, dateOfBirth, role
        FROM users
        WHERE role = 'eleve';
    """;

        try (PreparedStatement stm = cnx.prepareStatement(query)) {
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Users eleve = new Users(
                        rs.getInt("id_user"),
                        rs.getString("fullName"),
                        rs.getString("email"),
                        rs.getDate("dateOfBirth"),
                        "", // Pas besoin de récupérer le mot de passe
                        rs.getString("role")
                );
                eleves.add(eleve);
            }

            System.out.println("✅ " + eleves.size() + " élèves récupérés avec succès.");

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des élèves.");
            e.printStackTrace();
        }

        return eleves;
    }


}