package esprit.tn.services;

import esprit.tn.entities.Forum;
import esprit.tn.entities.Response;
import esprit.tn.main.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class ForumService {
    private Connection cnx;

    public ForumService() {
        cnx = DatabaseConnection.getInstance().getCnx();
    }

    public void ajouterForum(Forum forum) throws SQLException {
        String req = "INSERT INTO forum (title, description, idAuthor, dateCreation) VALUES (?, ?, ?, ?)";


            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setString(1, forum.getTitle());
            stm.setString(2, forum.getDescription());
            stm.setInt(3, forum.getIdAuthor());
            stm.setTimestamp(4, java.sql.Timestamp.valueOf(forum.getDateCreation()));

            stm.executeUpdate();
            System.out.println("Forum added successfully!");



    }

    public List<Forum> getAllForumsWithResponses() {
        List<Forum> forums = new ArrayList<>();
        String forumQuery = "SELECT * FROM forum";
        String responseQuery = "SELECT * FROM response WHERE forumId = ?";

        try {
            PreparedStatement forumStmt = cnx.prepareStatement(forumQuery);
            ResultSet forumResult = forumStmt.executeQuery();

            while (forumResult.next()) {
                int idForum = forumResult.getInt("idForum");
                String title = forumResult.getString("title");
                String description = forumResult.getString("description");
                int idAuthor = forumResult.getInt("idAuthor");
                LocalDateTime dateCreation = forumResult.getTimestamp("dateCreation").toLocalDateTime();

                PreparedStatement responseStmt = cnx.prepareStatement(responseQuery);
                responseStmt.setInt(1, idForum);
                ResultSet responseResult = responseStmt.executeQuery();

                List<Response> responses = new ArrayList<>();
                while (responseResult.next()) {
                    int responseId = responseResult.getInt("idResponse");
                    String content = responseResult.getString("content");
                    int authorId = responseResult.getInt("authorId");
                    LocalDateTime responseDate = responseResult.getTimestamp("dateCreation").toLocalDateTime();

                    responses.add(new Response(responseId,content, authorId, responseDate));
                }

                Forum forum = new Forum(idForum, title, description, idAuthor, responses);
                forums.add(forum);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error reading forums with responses: " + e.getMessage());
        }

        return forums;
    }

    public void deleteForum(int forumId) {
        String deleteResponsesQuery = "DELETE FROM response WHERE forumId = ?";
        String deleteForumQuery = "DELETE FROM forum WHERE idForum = ?";

        try {

            PreparedStatement deleteResponsesStmt = cnx.prepareStatement(deleteResponsesQuery);
            deleteResponsesStmt.setInt(1, forumId);
            deleteResponsesStmt.executeUpdate();


            PreparedStatement deleteForumStmt = cnx.prepareStatement(deleteForumQuery);
            deleteForumStmt.setInt(1, forumId);
            int rowsAffected = deleteForumStmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Forum deleted successfully!");
            } else {
                System.out.println("No forum found with the given ID.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting forum: " + e.getMessage());
        }
    }
    public void updateForum(Forum updatedForum) {
        String req = "UPDATE forum SET title = ?, description = ?, idAuthor = ?, dateCreation = ? WHERE idForum = ?";

        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setString(1, updatedForum.getTitle());
            stm.setString(2, updatedForum.getDescription());
            stm.setInt(3, updatedForum.getIdAuthor());
            stm.setTimestamp(4, java.sql.Timestamp.valueOf(updatedForum.getDateCreation()));
            stm.setInt(5, updatedForum.getIdForum());

            int rowsAffected = stm.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Forum updated successfully!");
            } else {
                System.out.println("No forum found with the given ID.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating forum: " + e.getMessage());
        }
    }

    public List<Forum> getAllForums() {
        List<Forum> forums = new ArrayList<>();
        String query = "SELECT * FROM forum";

        try {
            PreparedStatement stmt = cnx.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                int idForum = resultSet.getInt("idForum");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                int idAuthor = resultSet.getInt("idAuthor");
                LocalDateTime dateCreation = resultSet.getTimestamp("dateCreation").toLocalDateTime();

                forums.add(new Forum(idForum, title, description, idAuthor, dateCreation));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving forums: " + e.getMessage());
        }

        return forums;
    }

    public List<Response> getResponsesForForum(int forumId) {
        List<Response> responses = new ArrayList<>();
        String query = "SELECT * FROM response WHERE forumId = ?";

        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, forumId);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                int responseId = resultSet.getInt("idResponse");
                String content = resultSet.getString("content");
                int authorId = resultSet.getInt("authorId");
                LocalDateTime createdAt = resultSet.getTimestamp("dateCreation").toLocalDateTime();

                responses.add(new Response(responseId, content, authorId, createdAt));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching responses for forum: " + e.getMessage());
        }

        return responses;
    }


}
