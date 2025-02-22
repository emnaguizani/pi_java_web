package esprit.tn.entities;

import java.util.List;
import java.util.Objects;

public class Cours {
    private  int idCours ;
    private  String title ;
    private  String description ;
    private  String dateCreation ;
    private  int idFormateur ;
    private  Status status ;
    private Niveau niveau ;
    private List<String> videoPaths;


    public Cours(int idCours, String title, String description, String dateCreation, int idFormateur, Status status, Niveau niveau, List<String> videoPaths) {
        this.idCours = idCours;
        this.title = title;
        this.description = description;
        this.dateCreation = dateCreation;
        this.idFormateur = idFormateur;
        this.status = status;
        this.niveau = niveau;
        this.videoPaths = videoPaths;

    }


    public Cours(String title, String description, String dateCreation, int idFormateur, Status status, Niveau niveau, List<String> videoPaths) {
        this.title = title;
        this.description = description;
        this.dateCreation = dateCreation;
        this.idFormateur = idFormateur;
        this.status = status;
        this.niveau = niveau;
        this.videoPaths = videoPaths;
    }

    public Cours() {
    }

    public int getIdCours() {
        return idCours;
    }

    public void setIdCours(int idCours) {
        this.idCours = idCours;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public int getIdFormateur() {
        return idFormateur;
    }

    public void setIdFormateur(int idFormateur) {
        this.idFormateur = idFormateur;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Niveau getNiveau() {
        return niveau;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }

    public List<String> getVideoPaths() {
        return videoPaths;
    }

    public void setVideoPaths(List<String> videoPaths) {
        this.videoPaths = videoPaths;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cours cours = (Cours) o;
        return idCours == cours.idCours && idFormateur == cours.idFormateur && Objects.equals(title, cours.title) && Objects.equals(description, cours.description) && Objects.equals(dateCreation, cours.dateCreation) && status == cours.status && niveau == cours.niveau;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCours, title, description, dateCreation, idFormateur, status, niveau);
    }

    @Override
    public String toString() {
        return "Cours{" +
                "idCours=" + idCours +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dateCreation='" + dateCreation + '\'' +
                ", idFormateur=" + idFormateur +
                ", status=" + status +
                ", niveau=" + niveau +
                ", videoPaths=" + videoPaths +
                '}';
    }
}
