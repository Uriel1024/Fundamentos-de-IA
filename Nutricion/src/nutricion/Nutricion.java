    package nutricion;

    import javax.swing.*;

    public class Nutricion {
        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                GUIFrame frame = new GUIFrame();
                frame.setVisible(true);
            });
        }
    }