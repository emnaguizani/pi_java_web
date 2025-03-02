package esprit.tn.entities;

import java.sql.Timestamp;

public class Seance {
    private int idSeance;
    private String titre;
    private String contenu;  // Utiliser 'contenu' au lieu de 'description'
    private Timestamp datetime;  // Utiliser 'datetime' comme dans ta DB
    private int idFormateur;
    private String nomFormateur;
    private String modeSeance;

    // 🟢 Constructeur vide requis pour éviter les erreurs de compilation
    public Seance() {
    }
    public Seance(int idSeance, String titre, String contenu, Timestamp datetime, int idFormateur) {
        this.idSeance = idSeance;
        this.titre = titre;
        this.contenu = contenu;
        this.datetime = datetime;
        this.idFormateur = idFormateur;

    }
    public Seance(int idSeance, String titre, String contenu, Timestamp datetime, int idFormateur, String nomFormateur, String modeSeance) {
        this.idSeance = idSeance;
        this.titre = titre;
        this.contenu = contenu;
        this.datetime = datetime;
        this.idFormateur = idFormateur;
        this.nomFormateur = nomFormateur;
        this.modeSeance = modeSeance;
    }


    // 🟢 Constructeur avec tous les paramètres
    public Seance(int idSeance, String titre, String contenu, Timestamp datetime, int idFormateur, String nomFormateur) {
        this.idSeance = idSeance;
        this.titre = titre;
        this.contenu = contenu;
        this.datetime = datetime;
        this.idFormateur = idFormateur;
        this.nomFormateur = nomFormateur;
    }

    // 🟢 Constructeur sans le formateur
    public Seance(int idSeance, String titre, String contenu, Timestamp datetime) {
        this.idSeance = idSeance;
        this.titre = titre;
        this.contenu = contenu;
        this.datetime = datetime;
    }
    public String getModeSeance() {
        return modeSeance;
    }

    public void setModeSeance(String modeSeance) {
        this.modeSeance = modeSeance;
    }

    // 🟢 Getters et Setters
    public int getIdSeance() {
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

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Timestamp getDatetime() {
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

    public String getNomFormateur() {
        return nomFormateur;
    }

    public void setNomFormateur(String nomFormateur) {
        this.nomFormateur = nomFormateur;
    }

    @Override
    public String toString() {
        return "Seance [idSeance=" + idSeance + ", titre=" + titre + ", contenu=" + contenu + ", datetime=" + datetime + ", idFormateur=" + idFormateur + "]";
    }
}
