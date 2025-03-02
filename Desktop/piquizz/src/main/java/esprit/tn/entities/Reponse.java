package esprit.tn.entities;

public class Reponse {
    private int exerciceId;
    private String reponse;
    private double noteExercice;
    private int eleveId;

    public Reponse() {}

    public Reponse(int exerciceId, String reponse, int eleveId) {
        this.exerciceId = exerciceId;
        this.reponse = reponse;
        this.eleveId = eleveId;
    }

    public int getExerciceId() {
        return exerciceId;
    }

    public void setExerciceId(int exerciceId) {
        this.exerciceId = exerciceId;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public double getNoteExercice() {
        return noteExercice;
    }

    public void setNoteExercice(double noteExercice) {
        this.noteExercice = noteExercice;
    }

    public int getEleveId() {
        return eleveId;
    }

    public void setEleveId(int eleveId) {
        this.eleveId = eleveId;
    }

    @Override
    public String toString() {
        return "Reponse{" +
                "exerciceId=" + exerciceId +
                ", reponse='" + reponse + '\'' +
                ", noteExercice=" + noteExercice +
                ", eleveId=" + eleveId +
                '}';
    }
}