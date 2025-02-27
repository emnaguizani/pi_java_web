package esprit.tn.utils;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
public class CaptchaGenerator {
    private static final int WIDTH = 150;
    private static final int HEIGHT = 50;
    private static final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ123456789";

    public static String generateCaptchaText(int length) {
        Random random = new Random();
        StringBuilder captchaText = new StringBuilder();
        for (int i = 0; i < length; i++) {
            captchaText.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return captchaText.toString();
    }

    public static Image generateCaptchaImage(String captchaText) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // Background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Random Lines (for difficulty)
        g.setColor(Color.LIGHT_GRAY);
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int x1 = random.nextInt(WIDTH);
            int y1 = random.nextInt(HEIGHT);
            int x2 = random.nextInt(WIDTH);
            int y2 = random.nextInt(HEIGHT);
            g.drawLine(x1, y1, x2, y2);
        }

        // CAPTCHA Text
        g.setFont(new Font("Arial", Font.BOLD, 28));
        g.setColor(Color.BLACK);
        g.drawString(captchaText, 25, 35);
        g.dispose();

        return SwingFXUtils.toFXImage(image, null);
    }

}
