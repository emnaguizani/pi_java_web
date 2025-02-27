package esprit.tn.utils;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
public class EmailSender {
    public static boolean sendEmail(String toEmail, String subject, String body) {
        String host = "smtp.gmail.com";  // Gmail SMTP server
        int port = 587;  // Use port 587 for TLS
        String username = "guizani.emna14@gmail.com";  // Your Gmail address
        String appPassword = "snfg uhqk kvlb wwoi\n";  // The App Password you generated for Gmail

        // Set properties for Gmail SMTP
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true"); // Use TLS
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);

        // Create the session
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, appPassword);
            }
        });

        try {
            // Create the email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));  // Sender's email
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));  // Recipient's email
            message.setSubject(subject);  // Email subject
            message.setText(body);  // Email body

            // Send the email
            Transport.send(message);
            System.out.println("Email sent successfully!");
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Failed to send email.");
            return false;
        }
    }

    public static void main(String[] args) {
        // Test the email sender
        sendEmail("recipient-email@example.com", "Welcome to our service", "Your account has been successfully verified. Welcome!");
    }
}
