package esprit.tn.services;

import esprit.tn.entities.Users;
import esprit.tn.main.DatabaseConnection;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService implements Iservice <Users>{

    Connection cnx;

    public UserService(){

        cnx= DatabaseConnection.getInstance().getCnx();
    }

    public boolean updateUserAccess(int idUser, boolean newAccessStatus) {

        String sql = "UPDATE users SET access = ? WHERE id_user = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setBoolean(1, newAccessStatus); // Correct variable name to match the parameter
            stmt.setInt(2, idUser); // Correct variable name to match the parameter
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newAccessStatus;
    }

    @Override
    public void ajouter(Users users) {
        String req = "INSERT INTO users (fullName, email, dateOfBirth, password, role, access, phoneNumber) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setString(1, users.getFullName());
            stm.setString(2, users.getEmail());
            stm.setDate(3, new java.sql.Date(users.getDateOfBirth().getTime()));
            stm.setString(4, users.getPassword());
            stm.setString(5, users.getRole());
            stm.setBoolean(6, users.getRole().equals("formateur") ? false : true); // Set access to false for formateur
            stm.setString(7, users.getPhoneNumber());
            stm.executeUpdate();
            System.out.println("Added successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void modifier(Users users) {
        String req = "UPDATE users SET fullName = ?, email = ?, password = ? , role = ?  WHERE id_user = ?";
        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setString(1, users.getFullName());
            stm.setString(2, users.getEmail());
            stm.setString(3, users.getPassword());
            stm.setString(4, users.getRole());
            stm.setInt(5, users.getId_user());
            int rowsAffected = stm.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User updated successfully!");
            } else {
                System.out.println("User with ID " + users.getId_user() + " not found.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void supprimer(Users users) {
        String req = "DELETE FROM users WHERE id_user = ?";
        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setInt(1, users.getId_user());
            int rowsAffected = stm.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User deleted successfully!");
            } else {
                System.out.println("User with ID " + users.getId_user() + " not found.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Users> getall() {
        List<Users> usersList = new ArrayList<>();
        String query = "SELECT * FROM users";

        try (Statement stmt = cnx.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Users user = new Users();
                user.setId_user(rs.getInt("id_user"));
                user.setFullNAme(rs.getString("fullName"));
                user.setEmail(rs.getString("email"));
                user.setDateOfBirth(rs.getDate("dateOfBirth"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setAccess(rs.getBoolean("access"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                usersList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usersList;
    }

    @Override
    public Users getone() {
        return null;
    }
    public Users getUserById(int userId) throws SQLException {
        String query = "SELECT * FROM users WHERE id_user = ?";
        Users user = null;

        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                user = new Users();
                user.setId_user(rs.getInt("id"));
                user.setFullNAme(rs.getString("fullname"));
                user.setEmail(rs.getString("email"));
                user.setDateOfBirth(rs.getDate("date_of_birth"));
                user.setRole(rs.getString("role"));
                //user.setAccess(rs.getBoolean("access"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
            }
        }
        return user;
    }
    public boolean isEmailExists(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // If count > 0, email exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public Users authenticateUser(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, email);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                Users user = new Users();
                user.setId_user(rs.getInt("id_user"));
                user.setFullNAme(rs.getString("fullName"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                user.setAccess(rs.getBoolean("access"));  // Get the access status

                return user; // Return the user if found
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean doesEmailOrPhoneExist(String emailOrPhone) {
        String query = "SELECT * FROM users WHERE email = ? OR phoneNumber = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setString(1, emailOrPhone);
            preparedStatement.setString(2, emailOrPhone);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();  // If a result exists, the email or phone number exists
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
 /*   public void approveFormateur(int formateurId) {

        String query = "UPDATE users SET access = true WHERE id_user = ? AND role = 'formateur'";

        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, formateurId);
            stmt.executeUpdate();
            System.out.println("Formateur approved!");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Approval Failed", "Failed to approve formateur.");
        }
    }*/

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public List<Users> getPendingFormateurs() {
        List<Users> pendingList = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role = 'formateur' AND access = false";

        try (PreparedStatement stmt = cnx.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Users user = new Users();
                user.setId_user(rs.getInt("id_user"));
                user.setFullNAme(rs.getString("fullName"));
                user.setEmail(rs.getString("email"));
                pendingList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pendingList;
    }

    public void approveFormateur(int userId) {
        String query = "UPDATE users SET access = true WHERE id_user = ? AND role = 'formateur'";

        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, userId);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Formateur with ID " + userId + " has been approved.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean resetPassword(String emailOrPhone, String newPassword) {
        String query = "UPDATE users SET password = ? WHERE email = ? OR phoneNumber = ?";

        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setString(1, newPassword); // No hashing
            preparedStatement.setString(2, emailOrPhone);
            preparedStatement.setString(3, emailOrPhone);

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0; // Return true if password was successfully updated
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
