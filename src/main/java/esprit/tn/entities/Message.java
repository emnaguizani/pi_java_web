package esprit.tn.entities;

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