package esprit.tn.utils;

import esprit.tn.entities.Users;

public class SessionManager {
    private static SessionManager instance;
    private Users loggedInUser;

    private SessionManager() {
        // ✅ Simuler une connexion avec un utilisateur existant (Hamdi)
        this.loggedInUser = new Users(1, "ghassen", "hamdi@gmail.com", "hamdi123");
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setLoggedInUser(Users user) {
        this.loggedInUser = user;
    }

    public Users getLoggedInUser() {
        return loggedInUser;
    }

    public void clearSession() {
        loggedInUser = null;
    }

    public int getUserId() {
        return loggedInUser != null ? loggedInUser.getId_user() : -1; // Retourne -1 si aucun utilisateur n'est connecté
    }
}
