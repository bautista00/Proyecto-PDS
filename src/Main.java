import config.DependenciasClinica;
import view.MainFrame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DependenciasClinica dependencias = new DependenciasClinica();
            MainFrame frame = new MainFrame(dependencias);
            frame.setVisible(true);
        });
    }
}
