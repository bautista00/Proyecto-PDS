import config.DependenciasClinica;
import view.MainFrame;
import view.ManejadorCierreAplicacion;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DependenciasClinica dependencias = new DependenciasClinica();
            MainFrame frame = new MainFrame(
                    dependencias.getPacienteController(),
                    dependencias.getOdontologoController(),
                    dependencias.getSecretariaController(),
                    dependencias.getTurnoController(),
                    dependencias.getCoberturas(),
                    dependencias.getEspecialidades());
            new ManejadorCierreAplicacion(frame, dependencias::guardarDatos).configurar();
            frame.setVisible(true);
        });
    }
}
