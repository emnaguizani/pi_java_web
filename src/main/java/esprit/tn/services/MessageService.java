package esprit.tn.services;

import esprit.tn.entities.Message;
import esprit.tn.main.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageService {
    private Connection cnx;

    public MessageService() {
        cnx = DatabaseConnection.getInstance().getCnx();
    }

    // Send a message
    public void sendMessage(Message message) {
        String query = "INSERT INTO message (content, sender_id, community_id, sent_at) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, message.getContent());
            stmt.setInt(2, message.getSenderId());
            stmt.setInt(3, message.getCommunityId());
            stmt.setTimestamp(4, Timestamp.valueOf(message.getSentAt()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error sending message: " + e.getMessage());
        }
    }

    // Get all messages in a community
    public List<Message> getMessagesInCommunity(int communityId) {
        List<Message> messages = new ArrayList<>();
        String query = "SELECT * FROM message WHERE community_id = ? ORDER BY sent_at";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, communityId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Message message = new Message();
                message.setId(rs.getInt("id"));
                message.setContent(rs.getString("content"));
                message.setSenderId(rs.getInt("sender_id"));
                message.setCommunityId(rs.getInt("community_id"));
                message.setSentAt(rs.getTimestamp("sent_at").toLocalDateTime());
                messages.add(message);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching messages: " + e.getMessage());
        }
        return messages;
    }
}