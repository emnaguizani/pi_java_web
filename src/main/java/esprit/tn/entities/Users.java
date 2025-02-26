package esprit.tn.entities;

import java.util.Date;
import java.util.Objects;

public class Users {
    private int id_user;
    private String fullName;
    private String email;
    private Date dateOfBirth;
    private String password;
    private String role;
    private Boolean access=false;
    private String phoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getAccess() {
        return access;
    }

    public void setAccess(Boolean access) {
        this.access = access;
    }

    public Users() {}
    public Users(int id_user, String fullNAme, String email, Date dateOfBirth, String password, String role, Boolean access, String phoneNumber) {
        this.id_user = id_user;
        this.fullName = fullNAme;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.password = password;
        this.role = role;
        this.access =access;
        this.phoneNumber = phoneNumber;
    }

    public Users(String fullNAme, String email, Date dateOfBirth, String password, String role, Boolean access, String phoneNumber) {
        this.fullName = fullNAme;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.password = password;
        this.role = role;
        this.access =access;
        this.phoneNumber = phoneNumber;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullNAme(String fullNAme) {
        this.fullName = fullNAme;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id_user=" + id_user +
                ", fullNAme='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", access='" + access + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return id_user == users.id_user && Objects.equals(fullName, users.fullName) && Objects.equals(email, users.email) && Objects.equals(dateOfBirth, users.dateOfBirth) && Objects.equals(password, users.password) && Objects.equals(role, users.role) && Objects.equals(access, users.access) && Objects.equals(phoneNumber, users.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_user, fullName, email, dateOfBirth, password, role,access,phoneNumber);
    }
}
