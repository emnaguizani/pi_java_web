package esprit.tn.utils;

import esprit.tn.entities.Users;

public class SessionManager {
    private static SessionManager instance;
    private Users loggedInUser;

    private SessionManager() { }

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
        return loggedInUser != null ? loggedInUser.getId_user() : -1; // Return -1 if no user is logged in
    }
}
