package esprit.tn.entities;

import java.util.Objects;

public class Progress {
    private  int idProgress ;
    private  int idCours ;
    private  int idEleve ;
    private  StProgress progress ;


    public Progress() {
    }

    public Progress(int idCours, int idEleve, StProgress progress, int idProgress) {
        this.idCours = idCours;
        this.idEleve = idEleve;
        this.progress = progress;
        this.idProgress = idProgress;
    }

    public Progress(int idCours, int idEleve, StProgress progress) {
        this.idCours = idCours;
        this.idEleve = idEleve;
        this.progress = progress;
    }

    public int getIdProgress() {
        return idProgress;
    }

    public void setIdProgress(int idProgress) {
        this.idProgress = idProgress;
    }

    public int getIdCours() {
        return idCours;
    }

    public void setIdCours(int idCours) {
        this.idCours = idCours;
    }

    public int getIdEleve() {
        return idEleve;
    }

    public void setIdEleve(int idEleve) {
        this.idEleve = idEleve;
    }

    public StProgress getProgress() {
        return progress;
    }

    public void setProgress(StProgress progress) {
        this.progress = progress;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Progress progress1 = (Progress) o;
        return idProgress == progress1.idProgress && idCours == progress1.idCours && idEleve == progress1.idEleve && progress == progress1.progress;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProgress, idCours, idEleve, progress);
    }

    @Override
    public String toString() {
        return "Progress{" +
                "idProgress=" + idProgress +
                ", idCours=" + idCours +
                ", idEleve=" + idEleve +
                ", progress=" + progress +
                '}';
    }
}
