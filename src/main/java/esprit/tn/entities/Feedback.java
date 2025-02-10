package esprit.tn.entities;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Feedback {

    private int IdFeedback;
    private  String Message;
    private int Note;
    private LocalDateTime DateFeedback;
    private Reclamation ReclamationId;

    public Feedback(){}

    public Feedback(int idFeedback, String message, int note, LocalDateTime dateFeedback, Reclamation reclamationId) {
        IdFeedback = idFeedback;
        this.Message = message;
        this.Note = note;
        DateFeedback = dateFeedback;
        ReclamationId = reclamationId;
    }

    public Feedback(String message, int note, LocalDateTime dateFeedback, Reclamation reclamationId) {
        this.Message = message;
        this.Note = note;
        DateFeedback = dateFeedback;
        ReclamationId = reclamationId;
    }

    public int getIdFeedback() {
        return IdFeedback;
    }

    public void setIdFeedback(int idFeedback) {
        IdFeedback = idFeedback;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        this.Message = message;
    }

    public int getNote() {
        return Note;
    }

    public void setNote(int note) {
        this.Note = note;
    }

    public LocalDateTime getDateFeedback() {
        return DateFeedback;
    }

    public void setDateFeedback(LocalDateTime dateFeedback) {
        DateFeedback = dateFeedback;
    }

    public Reclamation getReclamationId() {
        return ReclamationId;
    }

    public void setReclamationId(Reclamation reclamationId) {
        ReclamationId = reclamationId;
    }

    public Reclamation getReclamation() {
        return ReclamationId;
    }

    public void setReclamation(Reclamation reclamation) {
        this.ReclamationId = reclamation;
    }
}