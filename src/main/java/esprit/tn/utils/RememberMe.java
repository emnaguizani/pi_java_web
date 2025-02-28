package esprit.tn.utils;
import java.io.*;
import java.util.Properties;
public class RememberMe {
    private static final String FILE_PATH = "user.properties";  // File to store user credentials

    public static void saveCredentials(String email, String password) {
        Properties properties = new Properties();
        properties.setProperty("email", email);
        properties.setProperty("password", password);

        try (FileOutputStream out = new FileOutputStream(FILE_PATH)) {
            properties.store(out, "User Credentials");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String[] loadCredentials() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            Properties properties = new Properties();
            try (FileInputStream in = new FileInputStream(file)) {
                properties.load(in);
                String email = properties.getProperty("email");
                String password = properties.getProperty("password");
                return new String[] { email, password };
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;  // No credentials saved
    }

    public static void clearCredentials() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            file.delete();  // Delete the file when logging out
        }
    }
}
