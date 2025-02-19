package esprit.tn.entities;
import java.util.Objects;

import java.time.LocalDateTime;

public class Reclamation {

    private int Id;
    private String Titre;
    private String Description;
    private String Status;
    private String Priorite;
    private LocalDateTime DateCreation;

    public Reclamation(){}

    public Reclamation(int id) {
        this.Id = id;
    }

    public Reclamation(int id, String titre, String description, String status, String priorite, LocalDateTime dateCreation) {
        Id = id;
        Titre = titre;
        Description = description;
        Status = status;
        Priorite = priorite;
        DateCreation = dateCreation;
    }

    public Reclamation(int id, String titre, String description, String status, String priorite) {
        Id = id;
        Titre = titre;
        Description = description;
        Status = status;
        Priorite = priorite;
    }


    public Reclamation(String titre, String description, String status, String priorite, LocalDateTime dateCreation) {
        Titre = titre;
        Description = description;
        Status = status;
        Priorite = priorite;
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

    public String getPriorite() {
        return Priorite;
    }

    public void setPriorite(String priorite) {
        Priorite = priorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reclamation reclamation)) return false;
        return Id == reclamation.Id && Objects.equals(Titre, reclamation.Titre) && Objects.equals(Description, reclamation.Description) && Objects.equals(Status, reclamation.Status) && Objects.equals(Priorite, reclamation.Priorite) && Objects.equals(DateCreation, reclamation.DateCreation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, Titre, Description, Status, Priorite, DateCreation);
    }


    @Override
    public String toString() {
        return "Reclamation{" +
                "Id=" + Id +
                ", Titre = '" + Titre + '\'' +
                ", Decription = '" + Description + '\'' +
                ", Status = '" + Status + '\'' +
                ", Status = '" + Priorite + '\'' +
                ", DateCreation = '" + DateCreation + '\'' +
                '}';
    }
}