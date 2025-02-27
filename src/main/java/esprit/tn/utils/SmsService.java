package esprit.tn.utils;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
public class SmsService {
    private static final String API_URL = "https://m3v189.api.infobip.com/sms/1/text/single";  // Infobip API endpoint
    private static final String API_KEY = "a572a2d1969741047c5120d05775820f-d4b9d800-917e-4697-91ee-bd8774762864";  // Replace with your API key

    public static boolean sendSms(String recipientPhoneNumber, String verificationCode) {
        try {
            // Prepare the JSON payload
            String payload = "{"
                    + "\"from\":\"InfoSMS\","
                    + "\"to\":\"" + recipientPhoneNumber + "\","
                    + "\"text\":\"Your verification code is: " + verificationCode + "\""
                    + "}";

            // Create URL and open connection
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "App " + API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Send request body
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = payload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Read response
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    System.out.println("✅ SMS sent successfully: " + response.toString());
                    return true;
                }
            } else {
                System.err.println("❌ Error sending SMS. HTTP Response Code: " + responseCode);
                return false;
            }

        } catch (Exception e) {
            System.err.println("⚠️ Exception occurred while sending SMS: " + e.getMessage());
            return false;
        }
    }

}
