package esprit.tn.services;

import esprit.tn.entities.Users;
import esprit.tn.main.DatabaseConnection;
import esprit.tn.utils.SessionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserService implements Iservice <Users>{

    Connection cnx;

    public UserService(){

        cnx= DatabaseConnection.getInstance().getCnx();
    }
    @Override
    public void ajouter(Users users) {
        String req= "INSERT INTO users (fullName,email,dateOfBirth,password,role) VALUES (?,?,?,?,?)";
        try{
            PreparedStatement stm= cnx.prepareStatement(req);

            stm.setString(1, users.getFullName());
            stm.setString(2, users.getEmail());
            stm.setDate(3, new java.sql.Date(users.getDateOfBirth().getTime()));
            stm.setString(4, users.getPassword());
            stm.setString(5, users.getRole());

            stm.executeUpdate();
            System.out.println("added successfully");
        }
        catch(SQLException e){
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
        //return List.of();
        List<Users> ps=new ArrayList<>();

        String req="SELECT * FROM users";

        try {
            Statement stm=cnx.createStatement();
            ResultSet rs= stm.executeQuery(req);

            while (rs.next()){
                Users p=new Users();
                p.setId_user( rs.getInt("id_user"));
                p.setFullNAme(  rs.getString("fullName"));
                p.setEmail(rs.getString("email"));
                p.setDateOfBirth(rs.getDate("dateOfBirth"));
                p.setPassword(rs.getString("password"));
                p.setRole(rs.getString("role"));
                ps.add(p);
            }

            System.out.println(ps);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ps;
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
        Users user = null;

        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) { // If user exists
                user = new Users();
                user.setId_user(rs.getInt("id_user"));
                user.setFullNAme(rs.getString("fullName"));
                user.setEmail(rs.getString("email"));
                user.setDateOfBirth(rs.getDate("dateOfBirth"));
                user.setRole(rs.getString("role"));

                // Store user in session
                SessionManager.getInstance().setLoggedInUser(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user; // Returns the user if found, or null if authentication fails
    }


}
