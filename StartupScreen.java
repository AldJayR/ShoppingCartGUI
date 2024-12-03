import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class StartupScreen extends JFrame {
    private final int DELAY = 3000; // 3 seconds
    private Timer timer;
    private MainView mainView;

    public StartupScreen() {
        setTitle("Fowlers");
        setUndecorated(true); 
        setResizable(false);
        setSize(400, 300);  

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(new Color(0, 122, 255));

        JLabel label = new JLabel("Fowlers");
        label.setFont(new Font("Segoe UI", Font.BOLD, 48));
        label.setForeground(Color.WHITE);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(label, BorderLayout.CENTER);

        setContentPane(contentPane);
        setLocationRelativeTo(null);
        setVisible(true);

        mainView = new MainView();
        mainView.setExtendedState(JFrame.MAXIMIZED_BOTH);

        timer = new Timer(DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchToMainView();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }



    private void switchToMainView() {

        setVisible(false); 
        dispose();
        mainView.setVisible(true);


    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new StartupScreen(); 


        });
    }
}