package esprit.tn.utils;

import esprit.tn.entities.Users;

public class SessionManager {
    private static SessionManager instance;
    private Users loggedInUser;
    private Users tempUser;       // Temporary user before OTP verification
    private String otpCode;
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
        this.loggedInUser = null;
        this.tempUser = null;
        this.otpCode = null;
    }

    public int getUserId() {
        return loggedInUser != null ? loggedInUser.getId_user() : -1; // Return -1 if no user is logged in
    }

    public Users getTempUser() {
        return tempUser;
    }

    // Set the temporary user (before OTP verification)
    public void setTempUser(Users tempUser) {
        this.tempUser = tempUser;
    }

    // Get the OTP code
    public String getOtpCode() {
        return otpCode;
    }

    // Set the OTP code
    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }




}
