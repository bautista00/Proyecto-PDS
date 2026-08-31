package view;

import config.DependenciasClinica;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {

    private final DependenciasClinica dependencias;
    private final TurnoPanel panelTurnos;

    public MainFrame(DependenciasClinica dependencias) {
        this.dependencias = dependencias;

        PacientePanel panelPacientes = new PacientePanel(dependencias.getPacienteController());
        OdontologoPanel panelOdontologos = new OdontologoPanel(dependencias.getOdontologoController());
        SecretariaPanel panelSecretarias = new SecretariaPanel(dependencias.getSecretariaController());
        this.panelTurnos = new TurnoPanel(
                dependencias.getTurnoController(),
                dependencias.getPacienteController(),
                dependencias.getOdontologoController(),
                dependencias.getSecretariaController());

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
                    dependencias.guardarDatos();
                    dispose();
                } else if (opcion == JOptionPane.NO_OPTION) {
                    dispose();
                }
            }
        });
    }
}
