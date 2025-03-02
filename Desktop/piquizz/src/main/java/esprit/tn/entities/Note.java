package esprit.tn.entities;

import java.util.List;

public class Note {
    private int quizId;
    private int eleveId;
    private double noteTotale;
    private List<Reponse> reponses;

    public Note(int quizId, int eleveId, List<Reponse> reponses) {
        this.quizId = quizId;
        this.eleveId = eleveId;
        this.reponses = reponses;
        this.noteTotale = calculerNoteTotale();
    }

    private double calculerNoteTotale() {
        return reponses.stream()
                .mapToDouble(Reponse::getNoteExercice)
                .sum();
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getEleveId() {
        return eleveId;
    }

    public void setEleveId(int eleveId) {
        this.eleveId = eleveId;
    }

    public double getNoteTotale() {
        return noteTotale;
    }

    public void setNoteTotale(double noteTotale) {
        this.noteTotale = noteTotale;
    }

    public List<Reponse> getReponses() {
        return reponses;
    }

    public void setReponses(List<Reponse> reponses) {
        this.reponses = reponses;
    }
}