import javax.swing.*;
import java.awt.*;

public class NotificationManager {
    public static void showNotification(String productName, int quantity) {
        SwingUtilities.invokeLater(() -> {
            JPanel notification = createNotificationPanel(productName, quantity);
            JDialog dialog = createNotificationDialog(notification);
            showAndFadeNotification(dialog);
        });
    }

    private static JPanel createNotificationPanel(String productName, int quantity) {
        JPanel notification = new JPanel();
        notification.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        notification.setBackground(new Color(40, 167, 69));

        JLabel message = new JLabel("✓ " + quantity + " " + productName + " added to cart");
        message.setForeground(Color.WHITE);
        message.setFont(new Font("Segoe UI", Font.BOLD, 14));

        notification.add(message);
        notification.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 0, 50), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        return notification;
    }

    private static JDialog createNotificationDialog(JPanel notification) {
        JDialog dialog = new JDialog((Frame)null);
        dialog.setUndecorated(true);
        dialog.setAlwaysOnTop(true);
        dialog.setModal(false);
        dialog.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        dialog.add(notification);
        dialog.pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setLocation(
            screenSize.width - dialog.getWidth() - 20,
            screenSize.height - dialog.getHeight() - 50
        );

        return dialog;
    }

    private static void showAndFadeNotification(JDialog dialog) {
        dialog.setOpacity(1.0f);
        dialog.setVisible(true);

        Timer timer = new Timer(2000, e -> {
            dialog.dispose();
        });
        timer.setRepeats(false);
        timer.start();
    }
}