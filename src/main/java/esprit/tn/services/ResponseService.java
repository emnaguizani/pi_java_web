package esprit.tn.services;

import esprit.tn.entities.Response;
import esprit.tn.main.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class ResponseService {
     Connection cnx;

    public ResponseService() {

        cnx = DatabaseConnection.getInstance().getCnx();
    }

    public void ajouter(Response response, int forumId) {
        String req = "INSERT INTO response (content, authorId, dateCreation, forumId, parentResponseId) VALUES (?, ?, ?, ?,?)";

        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setString(1, response.getContent());
            stm.setInt(2, response.getAuthor());
            stm.setObject(3, response.getCreatedAt());
            stm.setInt(4, forumId);

            if (response.getParentResponseId() == 0) {
                stm.setNull(5, Types.INTEGER);
            } else {
                stm.setInt(5, response.getParentResponseId());
            }

            stm.executeUpdate();
            System.out.println("Response added successfully!");
        } catch (SQLException e) {
            throw new RuntimeException("Error adding response: " + e.getMessage());
        }
    }
    public void deleteResponse(int responseId) {
        String req = "DELETE FROM response WHERE idResponse = ?";

        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setInt(1, responseId);
            int rowsAffected = stm.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Response deleted successfully!");
            } else {
                System.out.println("No response found with the given ID.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting response: " + e.getMessage());
        }
    }
    public void updateResponse(Response updatedResponse) {
        String req = "UPDATE response SET content = ?, authorId = ?, dateCreation = ? WHERE idResponse = ?";

        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setString(1, updatedResponse.getContent());
            stm.setInt(2, updatedResponse.getAuthor());
            stm.setObject(3, updatedResponse.getCreatedAt());
            stm.setInt(4, updatedResponse.getIdResponse());

            int rowsAffected = stm.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Response updated successfully!");
            } else {
                System.out.println("No response found with the given ID.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating response: " + e.getMessage());
        }
    }

    public boolean responseExists(String content, int forumId) {
        String req = "SELECT COUNT(*) FROM response WHERE content = ? AND forumId = ?";
        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setString(1, content);
            stm.setInt(2, forumId);
            var resultSet = stm.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking response existence: " + e.getMessage());
        }
        return false;
    }



}
