package esprit.tn.entities;


import java.util.List;

public class Exercice {
    private int idE;
    private String question;
    private List<String> options;

    private String correctAnswer;
    private int score;
    private boolean isMandatory;
    private String imagePath;
    private int quiz_id;



    public Exercice() {}

    public Exercice( String question, List<String> options,String correctAnswer, int score, String imagePath, boolean isMandatory) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.score = score;
        this.isMandatory = isMandatory;
        this.imagePath=imagePath;
    }


    public int getIdE() {
        return idE;
    }

    public void setIdE(int idE) {
        this.idE = idE;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {

        this.question = question;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath= imagePath;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setMandatory(boolean mandatory) {
        isMandatory = mandatory;
    }

    public int getquiz_id() {
        return quiz_id;
    }

    public void setquiz_id(int quiz_id) {
        this.quiz_id= quiz_id;;
    }

    @Override
    public String toString() {
        return "Exercice{" +
                "id=" + idE +
                ", question='" + question + '\'' +
                ", options=" + options +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", score=" + score +
                ", isMandatory=" + isMandatory +
                ", quiz_id="+quiz_id+
                '}';
    }

}
