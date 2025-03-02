package esprit.tn.entities;

import java.util.Date;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Users {
    private int id_user;
    private StringProperty fullName;
    private StringProperty email;
    private Date dateOfBirth;
    private StringProperty password;
    private StringProperty role;

    public Users() {
        this.fullName = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.password = new SimpleStringProperty();
        this.role = new SimpleStringProperty();
    }

    // ✅ Ajout du bon constructeur pour SessionManager
    public Users(int id_user, String fullName, String email, String password) {
        this.id_user = id_user;
        this.fullName = new SimpleStringProperty(fullName);
        this.email = new SimpleStringProperty(email);
        this.password = new SimpleStringProperty(password);
        this.role = new SimpleStringProperty("eleve"); // Par défaut élève
    }

    public Users(int id_user, String fullName, String email, Date dateOfBirth, String password, String role) {
        this();
        this.id_user = id_user;
        setFullName(fullName);
        setEmail(email);
        this.dateOfBirth = dateOfBirth;
        setPassword(password);
        setRole(role);
    }

    public int getId_user() { return id_user; }
    public void setId_user(int id_user) { this.id_user = id_user; }

    public String getFullName() { return fullName.get(); }
    public void setFullName(String fullName) { this.fullName.set(fullName); }
    public StringProperty fullNameProperty() { return fullName; }

    public String getEmail() { return email.get(); }
    public void setEmail(String email) { this.email.set(email); }
    public StringProperty emailProperty() { return email; }

    public Date getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getPassword() { return password.get(); }
    public void setPassword(String password) { this.password.set(password); }
    public StringProperty passwordProperty() { return password; }

    public String getRole() { return role.get(); }
    public void setRole(String role) { this.role.set(role); }
    public StringProperty roleProperty() { return role; }
}
