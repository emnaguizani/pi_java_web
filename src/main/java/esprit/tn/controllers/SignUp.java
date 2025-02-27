package esprit.tn.controllers;

import esprit.tn.entities.Users;
import esprit.tn.services.UserService;
import esprit.tn.utils.CaptchaGenerator;
import esprit.tn.utils.SessionManager;
import esprit.tn.utils.SmsService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp {
    @FXML
    private DatePicker dateofbirthid;

    @FXML
    private TextField emailid;

    @FXML
    private TextField fullnameid;

    @FXML
    private PasswordField passwordid;
    @FXML
    private Button homePage;
    @FXML
    private TextField roleid;
    @FXML
    private TextField phoneNumberid;
    @FXML
    private Label infoLabel;
    @FXML
    private RadioButton eleveRadioButton, formateurRadioButton;
    @FXML
    private Button signupid;
    @FXML
    private Button signUp;
    @FXML
    private Button loginid;
    @FXML private ImageView captchaImage;
    @FXML private Button refreshCaptchaButton;
    @FXML private TextField captchaField;

    @FXML private Label captchaStatus;

    private String generatedCaptcha;
    private ToggleGroup roleToggleGroup;
    public void initialize(){
        roleToggleGroup = new ToggleGroup();
        eleveRadioButton.setToggleGroup(roleToggleGroup);
        formateurRadioButton.setToggleGroup(roleToggleGroup);
        refreshCaptcha();
    }

    @FXML
    private void refreshCaptcha() {
        generatedCaptcha = CaptchaGenerator.generateCaptchaText(5); // 5-character CAPTCHA
        Image image = CaptchaGenerator.generateCaptchaImage(generatedCaptcha);
        captchaImage.setImage(image);
    }
    public void signUp(ActionEvent actionEvent) throws SQLException {
        if (!captchaField.getText().equals(generatedCaptcha)) {
            showAlert(Alert.AlertType.ERROR, "CAPTCHA Error", "Incorrect CAPTCHA. Please try again.");
            return;
        }
        String role = eleveRadioButton.isSelected() ? "eleve" : "formateur";
        if (roleToggleGroup.getSelectedToggle() == null) {
            showAlert(Alert.AlertType.ERROR, "Role Selection Error", "Please select either 'Élève' or 'Formateur'.");
            return;
        }

        // Validate fields
        if (fullnameid.getText().isEmpty() || emailid.getText().isEmpty() || passwordid.getText().isEmpty() || dateofbirthid.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields must be filled.");
            return;
        }

        if (!isValidEmail(emailid.getText())) {
            showAlert(Alert.AlertType.ERROR, "Invalid Email", "Please enter a valid email.");
            return;
        }

        if (!isValidPassword(passwordid.getText())) {
            showAlert(Alert.AlertType.ERROR, "Weak Password", "Password must contain at least 5 characters, 2 numbers, and 1 special character.");
            return;
        }

        // Validate age
        LocalDate birthDate = dateofbirthid.getValue();
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        if (age < 6) {
            showAlert(Alert.AlertType.ERROR, "Age Restriction", "You must be at least 6 years old.");
            return;
        }

        // Check email duplication
        UserService us = new UserService();
        if (us.isEmailExists(emailid.getText())) {
            showAlert(Alert.AlertType.ERROR, "Email Already in Use", "This email is already registered.");
            return;
        }

        // Validate phone number (assuming there's a phone number field)
        String phoneNumber = phoneNumberid.getText();
        if (phoneNumber.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Missing Phone Number", "Please enter a valid phone number.");
            return;
        }

        // Create user object (but DO NOT insert into DB yet)
        Users tempUser = new Users();
        tempUser.setFullNAme(fullnameid.getText());
        tempUser.setEmail(emailid.getText());
        tempUser.setDateOfBirth(java.sql.Date.valueOf(dateofbirthid.getValue()));
        tempUser.setPassword(passwordid.getText());
        tempUser.setRole(role);
        tempUser.setPhoneNumber(phoneNumber);

        // Generate and send OTP
        String otpCode = generateOtp();
        SmsService.sendSms(phoneNumber, "Your verification code is: " + otpCode);

        // Store user & OTP temporarily (Session or Singleton)
        SessionManager.getInstance().setTempUser(tempUser);
        SessionManager.getInstance().setOtpCode(otpCode);

        // Open OTP verification window
        openOtpVerificationWindow();
    }

    private void openOtpVerificationWindow() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/otpVerification.fxml"));
            fullnameid.getScene().setRoot(root); // Change root without creating a new scene
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isValidPassword(String text) {
        String passwordRegex = "^(?=.*\\d.*\\d)(?=.*[*/+\\-$^é\"'(-è_çà]).{5,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(passwordid.getText());
        return matcher.matches();
    }

    private boolean isValidEmail(String text) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(emailid.getText());
        return matcher.matches();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public void sendOtpToUser(String phoneNumber) {
        String verificationCode = generateOtp(); // Generate a random OTP
        SmsService.sendSms(phoneNumber, verificationCode);
    }

    private String generateOtp() {
        int otp = (int) (Math.random() * 9000) + 1000; // Generate 4-digit OTP
        return String.valueOf(otp);
    }

    public void goToLogIn(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/LogIn.fxml"));
            loginid.getScene().setRoot(root); // Change root without creating a new scene
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void homePage(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/HomePage.fxml"));
            homePage.getScene().setRoot(root); // Change root without creating a new scene
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void generateCaptcha() {
        generatedCaptcha = CaptchaGenerator.generateCaptchaText(6);  // Generate new CAPTCHA text
        captchaImage.setImage(CaptchaGenerator.generateCaptchaImage(generatedCaptcha));  // Show image
    }
    public void refersh(ActionEvent actionEvent) {
        refreshCaptcha();
    }
}
 