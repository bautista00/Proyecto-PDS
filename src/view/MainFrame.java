package view;

import controller.OdontologoController;
import controller.PacienteController;
import controller.SecretariaController;
import controller.TurnoController;
import entity.CoberturaPaciente;
import entity.EspecialidadOdontologica;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.util.List;

public class MainFrame extends JFrame {

    private final TurnoPanel panelTurnos;

    public MainFrame(PacienteController pacienteController,
                     OdontologoController odontologoController,
                     SecretariaController secretariaController,
                     TurnoController turnoController,
                     List<CoberturaPaciente> coberturas,
                     List<EspecialidadOdontologica> especialidades) {
        PacientePanel panelPacientes = new PacientePanel(pacienteController, coberturas);
        OdontologoPanel panelOdontologos = new OdontologoPanel(
                odontologoController, especialidades);
        SecretariaPanel panelSecretarias = new SecretariaPanel(secretariaController);
        panelTurnos = new TurnoPanel(
                turnoController,
                pacienteController,
                odontologoController,
                secretariaController);

        configurarVentana(panelPacientes, panelOdontologos, panelSecretarias);
    }

    private void configurarVentana(PacientePanel panelPacientes,
                                    OdontologoPanel panelOdontologos,
                                    SecretariaPanel panelSecretarias) {
        setTitle("Clinica Odontologica");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(950, 680);
        setLocationRelativeTo(null);
        add(crearPestanas(panelPacientes, panelOdontologos, panelSecretarias));
    }

    private JTabbedPane crearPestanas(PacientePanel panelPacientes,
                                      OdontologoPanel panelOdontologos,
                                      SecretariaPanel panelSecretarias) {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Pacientes", panelPacientes);
        tabs.addTab("Odontologos", panelOdontologos);
        tabs.addTab("Secretarias", panelSecretarias);
        tabs.addTab("Turnos", panelTurnos);
        tabs.addChangeListener(evento -> {
            if (tabs.getSelectedComponent() == panelTurnos) {
                panelTurnos.actualizarCombos();
            }
        });
        return tabs;
    }
}
