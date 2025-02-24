package esprit.tn.services;

import esprit.tn.entities.Community;
import esprit.tn.main.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommunityService {
    private Connection cnx;

    public CommunityService() {
        cnx = DatabaseConnection.getInstance().getCnx();
    }

    // Add a new community
    public void addCommunity(Community community) {
        String query = "INSERT INTO community (name, description, creator_id, created_at, members) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = cnx.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, community.getName());
            stmt.setString(2, community.getDescription());
            stmt.setInt(3, community.getCreatorId());
            stmt.setTimestamp(4, Timestamp.valueOf(community.getCreatedAt()));
            stmt.setString(5, community.getMembersAsString());
            stmt.executeUpdate();

            // Retrieve the generated ID
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                community.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error adding community: " + e.getMessage());
        }
    }

    // Get all communities
    public List<Community> getAllCommunities() {
        List<Community> communities = new ArrayList<>();
        String query = "SELECT * FROM community";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Community community = new Community();
                community.setId(rs.getInt("id"));
                community.setName(rs.getString("name"));
                community.setDescription(rs.getString("description"));
                community.setCreatorId(rs.getInt("creator_id"));
                community.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                community.setMembersFromString(rs.getString("members"));
                communities.add(community);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching communities: " + e.getMessage());
        }
        return communities;
    }

    // Update a community (e.g., add/remove members)
    public void updateCommunity(Community community) {
        String query = "UPDATE community SET name = ?, description = ?, members = ? WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, community.getName());
            stmt.setString(2, community.getDescription());
            stmt.setString(3, community.getMembersAsString());
            stmt.setInt(4, community.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating community: " + e.getMessage());
        }
    }
}