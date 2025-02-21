package esprit.tn.entities;

import java.time.LocalDateTime;

public class Response {
    private int idResponse;
    private String content;
    private int responseAuthorId;
    private LocalDateTime dateCreationResponse;
    private int parentResponseId;
    private int forumId;

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
