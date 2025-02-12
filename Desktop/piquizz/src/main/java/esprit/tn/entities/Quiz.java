package esprit.tn.entities;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private int quiz_id;
    private String title;
    private String description;
    private int duration;
    private int totalScore;
    private LocalDateTime creationDate;
    private String author;
    private List<Exercice> exercices;



    public Quiz() {
        this.creationDate = LocalDateTime.now();
        this.exercices = new ArrayList<>();
    }

    public Quiz( String title, String description, int duration, int totalScore, LocalDateTime creationDate, String author, List<Exercice> exercices) {
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.totalScore = totalScore;
        this.creationDate = creationDate;
        this.author = author;
        this.exercices = (exercices != null) ? exercices : new ArrayList<>();
        }

    public int getquiz_id() {
        return quiz_id;
    }

    public void setquiz_id(int quiz_id) {
        this.quiz_id = quiz_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {

        this.duration = duration;
    }
    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {

        this.totalScore = totalScore;
    }
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {

        this.creationDate = creationDate;
    }
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {

        this.author = author;
    }
    public List<Exercice> getExercices() {
        return exercices;
    }
    public void setExercices(List<Exercice> exercices) {

        this.exercices = exercices;
    }
    @Override
    public String toString() {
        return "Quiz{" +
                "id=" + quiz_id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", duration=" + duration +
                ", totalScore=" + totalScore +
                ", creationDate=" + creationDate +
                ", author='" + author + '\'' +
                ", number of exercices=" + exercices.size() +
                '}';
    }
}