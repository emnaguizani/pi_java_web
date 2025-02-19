package esprit.tn.entities;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Forum {


    private int idForum;
    private String titleForum;
    private String descriptionForum;
    private int idAuthor;
    private LocalDateTime dateCreationForum;
    private List<Response> responses;
    private boolean isBlocked = false;




    public Forum(){};
    public Forum(int id, String title, String description,int idAuthor) {
        idForum = id;
        this.titleForum = title;
        this.descriptionForum = description;
        this.idAuthor = idAuthor;
        this.dateCreationForum=LocalDateTime.now();
    }
    public Forum(int id, String title, String description,int idAuthor,List<Response> responses) {
        idForum = id;
        this.titleForum = title;
        this.descriptionForum = description;
        this.idAuthor = idAuthor;
        this.responses = responses;
        this.dateCreationForum=LocalDateTime.now();
    }
    public Forum(int idForum, String title, String description, int idAuthor, LocalDateTime dateCreation) {
        this.idForum = idForum;
        this.titleForum = title;
        this.descriptionForum = description;
        this.idAuthor = idAuthor;
        this.dateCreationForum = dateCreation;
    }

    public Forum(String title, String description,int idAuthor,List<Response> responses) {
        this.titleForum = title;
        this.descriptionForum = description;
        this.idAuthor = idAuthor;
        this.responses = responses;
        this.dateCreationForum=LocalDateTime.now();

    }

    public Forum(int id, String title, String description, int idAuthor, LocalDateTime dateCreation, boolean isBlocked) {
        this.idForum = id;
        this.titleForum = title;
        this.descriptionForum = description;
        this.idAuthor = idAuthor;
        this.dateCreationForum=dateCreation;
        this.dateCreationForum=LocalDateTime.now();
        this.isBlocked=isBlocked;
    }

    public int getIdForum() {
        return idForum;
    }

    public String getTitle() {
        return titleForum;
    }

    public String getDescription() {
        return descriptionForum;
    }

    public int getIdAuthor() {
        return idAuthor;
    }

    public LocalDateTime getDateCreation() {
        return dateCreationForum;
    }

    public List<Response> getResponses() {
        return responses;
    }

    public void setIdForum(int idForum) {
        this.idForum = idForum;
    }

    public void setTitle(String title) {
        this.titleForum = title;
    }

    public void setDescription(String description) {
        this.descriptionForum = description;
    }

    public void setIdAuthor(int idAuthor) {
        this.idAuthor = idAuthor;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreationForum = dateCreation;
    }

    public void setResponses(List<Response> responses) {
        this.responses = responses;
    }

    public void addResponse(Response response) {
        this.responses.add(response);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Forum forum)) return false;
        return idForum == forum.idForum && Objects.equals(titleForum, forum.titleForum) && Objects.equals(idAuthor, forum.idAuthor) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idForum, idAuthor, titleForum);
    }


    @Override
    public String toString() {
        return "Forum{" +
                "forum Id=" + idForum +
                ", author id='" + idAuthor + '\'' +
                ", titre='" + titleForum + '\'' +
                ", description='" + descriptionForum + '\'' +
                ", date de creation='" + dateCreationForum + '\'' +
                ", isBlocked=" + isBlocked +
                ", responses='" + responses + '\'' +
                '}';
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }


}
