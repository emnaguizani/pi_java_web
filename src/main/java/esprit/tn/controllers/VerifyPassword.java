package esprit.tn.controllers;

import esprit.tn.services.UserService;
import esprit.tn.utils.EmailSender;
import esprit.tn.utils.SmsService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Random;

public class VerifyPassword {
    @FXML
    private Button emailid;

    @FXML
    private TextField otpField;

    @FXML
    private Button smsid;

    @FXML
    private Button backid;

    @FXML
    private Button verifyid;

    @FXML
    private TextField contactField;
    private UserService userService = new UserService();
    private String generatedOtp; // Store the OTP temporarily
    private String userContact; // Store user email or phone
    private static String userEmailOrPhone; // Store the input

    public static String getUserEmailOrPhone() {
        return userEmailOrPhone;
    }

    // Replace with your actual classes for sending emails and SMS
    private EmailSender emailSender = new EmailSender();
    private SmsService smsSender = new SmsService();

    public void verification(ActionEvent actionEvent) {
        String enteredOtp = otpField.getText().trim();
        userEmailOrPhone = contactField.getText();
        if (enteredOtp.isEmpty()) {
            showAlert("Error", "Please enter the OTP.");
            return;
        }

        if (enteredOtp.equals(generatedOtp)) {
            showAlert("Success", "OTP verified! Redirecting to reset password...");
            redirectToResetPassword();
        } else {
            showAlert("Error", "Invalid OTP. Please try again.");
        }
    }

    private void redirectToResetPassword() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ForgotPasswordInterface.fxml"));
            backid.getScene().setRoot(root); // Change root without creating a new scene
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void backButton(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/LogIn.fxml"));
            backid.getScene().setRoot(root); // Change root without creating a new scene
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendButton(ActionEvent actionEvent) {
        userContact = contactField.getText().trim(); // Get the entered contact

        if (userContact.isEmpty()) {
            showAlert("Error", "Please enter your email or phone number.");
            return;
        }
        if (!userService.doesEmailOrPhoneExist(userContact)) {
            showAlert("Error", "Email or phone number not found in the database.");
            return;
        }
        generatedOtp = generateOtp(); // Generate a new OTP

        if (userContact.contains("@")) { // If it's an email
            boolean emailSent = emailSender.sendEmail(userContact, "Your OTP Code", "Your OTP is: " + generatedOtp);
            if (emailSent) {
                showAlert("Success", "OTP sent to your email!");
            } else {
                showAlert("Error", "Failed to send OTP via email.");
            }
        } else { // If it's a phone number
            boolean smsSent = smsSender.sendSms(userContact, "Your OTP is: " + generatedOtp);
            if (smsSent) {
                showAlert("Success", "OTP sent via SMS!");
            } else {
                showAlert("Error", "Failed to send OTP via SMS.");
            }
        }
    }

    private String generateOtp() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
