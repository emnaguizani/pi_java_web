package esprit.tn.controllers;

import esprit.tn.entities.Users;
import esprit.tn.services.UserService;
import esprit.tn.utils.SessionManager;
import esprit.tn.utils.SmsService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class OtpVerification {
    @FXML
    private TextField otpField;
    public void verifyOtp(ActionEvent actionEvent) {
        String enteredOtp = otpField.getText();
        String storedOtp = SessionManager.getInstance().getOtpCode();
        Users tempUser = SessionManager.getInstance().getTempUser();

        if (enteredOtp.equals(storedOtp) && tempUser != null) {
            // OTP is correct → Add user to DB
            UserService userService = new UserService();
            userService.ajouter(tempUser);

            // Grant access based on role
            if (tempUser.getRole().equals("formateur")) {
                showAlert(Alert.AlertType.INFORMATION, "Pending Approval", "Your request has been sent to the admin.");
            } else {
                SessionManager.getInstance().setLoggedInUser(tempUser);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Registration successful!");
            }

            // Redirect user
            redirectUser(tempUser.getRole());
        } else {
            showAlert(Alert.AlertType.ERROR, "Invalid OTP", "The OTP entered is incorrect.");
        }
    }
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private void redirectUser(String role) {
        String fxmlFile = role.equals("eleve") ? "/AfficherCoursEleve.fxml" : "/HomePage.fxml";
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            otpField.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the next page.");
        }
    }
    public void resendOtp(ActionEvent actionEvent) {
        String phoneNumber = SessionManager.getInstance().getTempUser().getPhoneNumber();
        String newOtp = generateOtp();
        SessionManager.getInstance().setOtpCode(newOtp);
        SmsService.sendSms(phoneNumber, "Your new verification code is: " + newOtp);
        showAlert(Alert.AlertType.INFORMATION, "OTP Resent", "A new OTP has been sent to your phone.");
    }
    private String generateOtp() {
        int otp = (int) (Math.random() * 9000) + 1000; // Generate 4-digit OTP
        return String.valueOf(otp);
    }
    }



