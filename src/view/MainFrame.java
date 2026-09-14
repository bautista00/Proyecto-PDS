package view;

import controller.OdontologoController;
import controller.PacienteController;
import controller.SecretariaController;
import controller.TurnoController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// Ventana principal de la aplicación: arma las cuatro pestañas (Pacientes, Odontólogos,
// Secretarias y Turnos) y las muestra.
// Recibe los controllers ya construidos desde DependenciasClinica, no los crea.
// Al cerrar la ventana pregunta si se quieren guardar los datos antes de salir.
public class MainFrame extends JFrame {

    private final Runnable guardarDatos;
    private final TurnoPanel panelTurnos;

    public MainFrame(PacienteController pacienteController,
                     OdontologoController odontologoController,
                     SecretariaController secretariaController,
                     TurnoController turnoController,
                     Runnable guardarDatos) {
        this.guardarDatos = guardarDatos;

        PacientePanel panelPacientes = new PacientePanel(pacienteController);
        OdontologoPanel panelOdontologos = new OdontologoPanel(odontologoController);
        SecretariaPanel panelSecretarias = new SecretariaPanel(secretariaController);
        this.panelTurnos = new TurnoPanel(
                turnoController,
                pacienteController,
                odontologoController,
                secretariaController);

        configurarVentana(panelPacientes, panelOdontologos, panelSecretarias);
    }

    private void configurarVentana(PacientePanel panelPacientes,
                                    OdontologoPanel panelOdontologos,
                                    SecretariaPanel panelSecretarias) {
        setTitle("Clínica Odontológica");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(950, 680);
        setLocationRelativeTo(null);

        add(crearPestanas(panelPacientes, panelOdontologos, panelSecretarias));
        configurarGuardadoAlCerrar();
    }

    private JTabbedPane crearPestanas(PacientePanel panelPacientes,
                                      OdontologoPanel panelOdontologos,
                                      SecretariaPanel panelSecretarias) {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Pacientes", panelPacientes);
        tabs.addTab("Odontólogos", panelOdontologos);
        tabs.addTab("Secretarias", panelSecretarias);
        tabs.addTab("Turnos", panelTurnos);

        tabs.addChangeListener(e -> {
            if (tabs.getSelectedComponent() == panelTurnos) {
                panelTurnos.actualizarCombos();
            }
        });
        return tabs;
    }

    private void configurarGuardadoAlCerrar() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int opcion = JOptionPane.showConfirmDialog(
                        MainFrame.this,
                        "¿Desea guardar los datos antes de salir?",
                        "Salir",
                        JOptionPane.YES_NO_CANCEL_OPTION
                );
                if (opcion == JOptionPane.YES_OPTION) {
                    guardarDatos.run();
                    dispose();
                } else if (opcion == JOptionPane.NO_OPTION) {
                    dispose();
                }
            }
        });
    }
}
