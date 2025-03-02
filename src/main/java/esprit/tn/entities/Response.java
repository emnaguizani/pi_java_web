package esprit.tn.entities;

import esprit.tn.services.UserService;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Response {
    private int idResponse;
    private String content;
    private int responseAuthorId;
    private LocalDateTime dateCreationResponse;
    private int parentResponseId;
    private int forumId;
    private int depth;

    public Response() {
        this.dateCreationResponse = LocalDateTime.now();
    }

    public Response(String content, int authorId, LocalDateTime dateCreation) {
        this.content = content;
        this.responseAuthorId = authorId;
        this.dateCreationResponse = dateCreation != null ? dateCreation : LocalDateTime.now();
    }

    public Response(int responseId, String content, int authorId, LocalDateTime responseDate) {
        this.idResponse=responseId;
        this.content = content;
        this.responseAuthorId = authorId;
        this.dateCreationResponse = responseDate != null ? responseDate : LocalDateTime.now();
    }
    public Response(int idResponse, int idForum, int idAuthor, String content, LocalDateTime dateCreated, int parentResponseId) {
        this.idResponse = idResponse;
        this.forumId = idForum;
        this.responseAuthorId = idAuthor;
        this.content = content;
        this.dateCreationResponse = dateCreated;
        this.parentResponseId = parentResponseId;
    }

    public Response(String content, int authorId, LocalDateTime createdAt, int parentResponseId) {
        this.content = content;
        this.responseAuthorId = authorId;
        this.dateCreationResponse = createdAt;
        this.parentResponseId = parentResponseId;
    }

    public Response(int idResponse, String content, int authorId, LocalDateTime dateCreation, int parentResponseId) {
        this.idResponse = idResponse;
        this.responseAuthorId = authorId;
        this.content = content;
        this.dateCreationResponse = dateCreation;
        this.parentResponseId = parentResponseId;
    }

    public int getIdResponse() {
        return idResponse;
    }

    public void setIdResponse(int id) {
        this.idResponse = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAuthor() {
        return responseAuthorId;
    }

    public void setAuthor(int authorId) {
        this.responseAuthorId = authorId;
    }

    public LocalDateTime getCreatedAt() {
        return dateCreationResponse;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.dateCreationResponse = createdAt;
    }

    public int getParentResponseId() {
        return parentResponseId;
    }

    public void setParentResponseId(int parentResponseId) {
        this.parentResponseId = parentResponseId;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getAuthorName(UserService userService) {

            Users user = userService.getUserById2(this.getAuthor());
            if (user != null) {
                return user.getFullName();
            }

        return "Unknown";
    }

    @Override
    public String toString() {
        return "Response{" +
                "Response id=" + idResponse +
                ", content='" + content + '\'' +
                ", authorId=" + responseAuthorId +
                ", createdAt=" + dateCreationResponse +
                '}';
    }
}
