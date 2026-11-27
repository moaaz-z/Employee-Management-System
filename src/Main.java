import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import gui.LoginForm;

public class Main {
    public static void main(String[] args) {
        // Use system look and feel for a native OS appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Start on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            LoginForm login = new LoginForm();
            login.setVisible(true);
        });}
}
