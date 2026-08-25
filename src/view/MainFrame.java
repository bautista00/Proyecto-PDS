package view;

import controller.OdontologoController;
import controller.PacienteController;
import controller.SecretariaController;
import controller.TurnoController;
import persistence.PersistenciaServicio;
import repository.OdontologoRepository;
import repository.PacienteRepository;
import repository.SecretariaRepository;
import repository.TurnoRepository;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {

    private final PersistenciaServicio persistencia;
    private final PacienteRepository   pacienteRepository;
    private final OdontologoRepository odontologoRepository;
    private final SecretariaRepository secretariaRepository;
    private final TurnoRepository      turnoRepository;

    private final TurnoPanel panelTurnos;

    public MainFrame(PersistenciaServicio persistencia,
                     PacienteController pacienteController,
                     OdontologoController odontologoController,
                     SecretariaController secretariaController,
                     TurnoController turnoController,
                     PacienteRepository pacienteRepository,
                     OdontologoRepository odontologoRepository,
                     SecretariaRepository secretariaRepository,
                     TurnoRepository turnoRepository) {

        this.persistencia          = persistencia;
        this.pacienteRepository    = pacienteRepository;
        this.odontologoRepository  = odontologoRepository;
        this.secretariaRepository  = secretariaRepository;
        this.turnoRepository       = turnoRepository;

        PacientePanel   panelPacientes   = new PacientePanel(pacienteController);
        OdontologoPanel panelOdontologos = new OdontologoPanel(odontologoController);
        SecretariaPanel panelSecretarias = new SecretariaPanel(secretariaController);
        this.panelTurnos = new TurnoPanel(turnoController, pacienteController, odontologoController, secretariaController);

        setTitle("Clínica Odontológica");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(950, 680);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Pacientes",   panelPacientes);
        tabs.addTab("Odontólogos", panelOdontologos);
        tabs.addTab("Secretarias", panelSecretarias);
        tabs.addTab("Turnos",      panelTurnos);

        // Actualizar combos de turnos al cambiar de pestaña
        tabs.addChangeListener(e -> {
            if (tabs.getSelectedComponent() == panelTurnos) {
                panelTurnos.actualizarCombos();
            }
        });

        add(tabs);

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
                    persistencia.guardar(pacienteRepository, odontologoRepository,
                            secretariaRepository, turnoRepository);
                    dispose();
                } else if (opcion == JOptionPane.NO_OPTION) {
                    dispose();
                }
            }
        });
    }
}
