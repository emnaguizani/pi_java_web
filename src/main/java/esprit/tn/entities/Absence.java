package esprit.tn.entities;

public class Absence {
    private int idAbsence;
    private int idSeance;
    private int idEleve;
    private String etat;
    private String eleveFullName;
    private String seanceTitre;
    private String commentaire; // ✅ Ajout du champ commentaire

    // ✅ Constructeur complet avec commentaire
    public Absence(int idAbsence, int idSeance, int idEleve, String etat, String eleveFullName, String seanceTitre, String commentaire) {
        this.idAbsence = idAbsence;
        this.idSeance = idSeance;
        this.idEleve = idEleve;
        this.etat = etat;
        this.eleveFullName = eleveFullName;
        this.seanceTitre = seanceTitre;
        this.commentaire = commentaire;
    }

    // ✅ Constructeur avec 6 paramètres (sans commentaire)
    public Absence(int idAbsence, int idSeance, int idEleve, String etat, String eleveFullName, String seanceTitre) {
        this(idAbsence, idSeance, idEleve, etat, eleveFullName, seanceTitre, ""); // Valeur vide par défaut pour commentaire
    }

    // ✅ Constructeur avec 5 paramètres (sans titre de séance et sans commentaire)
    public Absence(int idAbsence, int idSeance, int idEleve, String etat, String eleveFullName) {
        this(idAbsence, idSeance, idEleve, etat, eleveFullName, "", ""); // Valeurs par défaut pour titre et commentaire
    }

    // ✅ Constructeur simplifié sans titre de séance, nom d’élève et commentaire
    public Absence(int idAbsence, int idSeance, int idEleve, String etat) {
        this(idAbsence, idSeance, idEleve, etat, "", "", ""); // Valeurs par défaut vides
    }

    // ✅ Getters & Setters
    public int getIdAbsence() {
        return idAbsence;
    }

    public void setIdAbsence(int idAbsence) {
        this.idAbsence = idAbsence;
    }

    public int getIdSeance() {
        return idSeance;
    }

    public void setIdSeance(int idSeance) {
        this.idSeance = idSeance;
    }

    public int getIdEleve() {
        return idEleve;
    }

    public void setIdEleve(int idEleve) {
        this.idEleve = idEleve;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getEleveFullName() {
        return eleveFullName;
    }

    public void setEleveFullName(String eleveFullName) {
        this.eleveFullName = eleveFullName;
    }

    public String getSeanceTitre() {
        return seanceTitre;
    }

    public void setSeanceTitre(String seanceTitre) {
        this.seanceTitre = seanceTitre;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    @Override
    public String toString() {
        return "Absence{" +
                "idAbsence=" + idAbsence +
                ", idSeance=" + idSeance +
                ", idEleve=" + idEleve +
                ", etat='" + etat + '\'' +
                ", eleveFullName='" + eleveFullName + '\'' +
                ", seanceTitre='" + seanceTitre + '\'' +
                ", commentaire='" + commentaire + '\'' +
                '}';
    }
}
