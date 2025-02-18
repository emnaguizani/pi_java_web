package esprit.tn.entities;

import java.time.LocalDateTime;

public class Feedback {

    private int IdFeedback;
    private String TypeFeedback;
    private  String Message;
    private int Note;
    private LocalDateTime DateFeedback;
    private Reclamation ReclamationId;

    public Feedback(){}

    public Feedback(int idFeedback, String typeFeedback, String message, int note, LocalDateTime dateFeedback, Reclamation reclamationId) {
        IdFeedback = idFeedback;
        this.TypeFeedback = typeFeedback;
        this.Message = message;
        this.Note = note;
        DateFeedback = dateFeedback;
        ReclamationId = reclamationId;
    }

    public Feedback(String typeFeedback, String message, int note, LocalDateTime dateFeedback, Reclamation reclamationId) {
        this.TypeFeedback = typeFeedback;
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

    public String getTypeFeedback() {
        return TypeFeedback;
    }

    public void setTypeFeedback(String typeFeedback) {
        TypeFeedback = typeFeedback;
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

    public int getReclamationIdValue() {
        return ReclamationId != null ? ReclamationId.getId() : -1; // Retourne -1 si la réclamation est null
    }
}