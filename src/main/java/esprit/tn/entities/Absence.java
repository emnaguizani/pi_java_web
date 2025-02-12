package esprit.tn.entities;

public class Absence {
    private int idAbsence;
    private int idSeance;
    private int idEleve;
    private String etat;

    public Absence(int idAbsence, int idSeance, int idEleve, String etat) {
        this.idAbsence = idAbsence;
        this.idSeance = idSeance;
        this.idEleve = idEleve;
        this.etat = etat;
    }

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

    @Override
    public String toString() {
        return "Absence [idAbsence=" + idAbsence + ", idSeance=" + idSeance + ", idEleve=" + idEleve + ", etat=" + etat + "]";
    }
}
