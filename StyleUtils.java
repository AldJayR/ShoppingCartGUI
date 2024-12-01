import javax.swing.*;
import java.awt.*;

public class StyleUtils
{
    public static void styleButton(JButton button)
    {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(30, 144, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void stylePanel(JPanel panel)
    {
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(245, 245, 245));
    }

    public static void styleLabel(JLabel label, int fontSize)
    {
        label.setFont(new Font("Verdana", Font.PLAIN, fontSize));
        label.setForeground(new Color(50, 50, 50));
    }
}
