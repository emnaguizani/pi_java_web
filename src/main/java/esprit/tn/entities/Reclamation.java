package esprit.tn.entities;
import java.util.Objects;

import java.time.LocalDateTime;

public class Reclamation {

    private int Id;
    private String Titre;
    private String Description;
    private String Status;
    private LocalDateTime DateCreation;

    public Reclamation(){}

    public Reclamation(int id) {
        this.Id = id;
    }

    public Reclamation(int id, String titre, String description, String status, LocalDateTime dateCreation) {
        id = Id;
        titre = Titre;
        description = Description;
        status = Status;
        dateCreation = DateCreation;
    }

    public Reclamation(int id, String titre, String description, String status) {
        id = Id;
        titre = Titre;
        description = Description;
        status = Status;
    }


    public Reclamation(String titre, String description, String status, LocalDateTime dateCreation) {
        Titre = titre;
        Description = description;
        Status = status;
        DateCreation = dateCreation;
    }

    public LocalDateTime getDateCreation() {
        return DateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        DateCreation = dateCreation;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTitre() {
        return Titre;
    }

    public void setTitre(String titre) {
        Titre = titre;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reclamation reclamation)) return false;
        return Id == reclamation.Id && Objects.equals(Titre, reclamation.Titre) && Objects.equals(Description, reclamation.Description) && Objects.equals(Status, reclamation.Status) && Objects.equals(DateCreation, reclamation.DateCreation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, Titre, Description, Status, DateCreation);
    }


    @Override
    public String toString() {
        return "Reclamation{" +
                "Id=" + Id +
                ", Titre = '" + Titre + '\'' +
                ", Decription = '" + Description + '\'' +
                ", Status = '" + Status + '\'' +
                ", DateCreation = '" + DateCreation + '\'' +
                '}';
    }
}