package esprit.tn.entities;

import esprit.tn.services.UserService;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class Message {
    private int id;
    private String content;
    private int senderId;
    private int communityId;
    private LocalDateTime sentAt;

    public Message() {}

    public Message(String content, int senderId, int communityId) {
        this.content = content;
        this.senderId = senderId;
        this.communityId = communityId;
        this.sentAt = LocalDateTime.now();
    }
    public Message(String content, int senderId, int communityId, LocalDateTime sentAt) {
        this.content = content;
        this.senderId = senderId;
        this.communityId = communityId;
        this.sentAt = sentAt;
    }

    public Message(int id, String content, int senderId, int communityId, LocalDateTime sentAt) {
        this.id=id;
        this.content = content;
        this.senderId = senderId;
        this.communityId = communityId;
        this.sentAt = sentAt;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public String getSenderName(UserService userService) {

            Users user = userService.getUserById2(this.getSenderId());
            if (user != null) {
                return user.getFullName();
            }
        return "Unknown";
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", senderId=" + senderId +
                ", communityId=" + communityId +
                ", sentAt=" + sentAt +
                '}';
    }
}