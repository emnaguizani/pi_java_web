package esprit.tn.entities;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Seance {
    private int idSeance;
    private String titre;
    private String contenu;  // Utiliser 'contenu' au lieu de 'description'
    private Timestamp datetime;  // Utiliser 'datetime' comme dans votre DB
    private int idFormateur;

    public Seance(int idSeance, String titre, String contenu, Timestamp datetime, int idFormateur) {
        this.idSeance = idSeance;
        this.titre = titre;
        this.contenu = contenu;
        this.datetime = datetime;
        this.idFormateur = idFormateur;
    }
    public Seance(int idSeance, String titre, String contenu, Timestamp datetime) {
        this.idSeance = idSeance;
        this.titre = titre;
        this.contenu = contenu;
        this.datetime = datetime;
    }





    // Getters et Setters
    public int getIdSeance() {  // Correspond à 'getId' utilisé
        return idSeance;
    }

    public void setIdSeance(int idSeance) {
        this.idSeance = idSeance;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getContenu() {  // Correspond à 'getDescription' utilisé
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Timestamp getDatetime() {  // Correspond à 'getDateHeure' utilisé
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }

    public int getIdFormateur() {
        return idFormateur;
    }

    public void setIdFormateur(int idFormateur) {
        this.idFormateur = idFormateur;
    }
    @Override
    public String toString() {
        return "Seance [idSeance=" + idSeance + ", titre=" + titre + ", contenu=" + contenu + ", datetime=" + datetime + ", idFormateur=" + idFormateur + "]";
    }

}
