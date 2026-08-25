import controller.OdontologoController;
import controller.PacienteController;
import controller.SecretariaController;
import controller.TurnoController;
import persistence.PersistenciaServicio;
import repository.OdontologoRepository;
import repository.PacienteRepository;
import repository.SecretariaRepository;
import repository.TurnoRepository;
import service.Facturador;
import service.OdontologoServiceImpl;
import service.PacienteServiceImpl;
import service.SecretariaServiceImpl;
import service.TurnoServiceImpl;
import view.MainFrame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            // Cargar datos persistidos (o crear repositorios vacíos en la primera ejecución)
            PersistenciaServicio persistencia = new PersistenciaServicio();

            PacienteRepository   pacienteRepository   = persistencia.cargarPacientes();
            OdontologoRepository odontologoRepository = persistencia.cargarOdontologos();
            SecretariaRepository secretariaRepository = persistencia.cargarSecretarias();
            TurnoRepository      turnoRepository      = persistencia.cargarTurnos(
                    pacienteRepository, odontologoRepository, secretariaRepository);

            // Servicios
            Facturador facturador = new Facturador();

            PacienteServiceImpl  pacienteService  = new PacienteServiceImpl(pacienteRepository);
            OdontologoServiceImpl odontologoService = new OdontologoServiceImpl(odontologoRepository);
            SecretariaServiceImpl secretariaService = new SecretariaServiceImpl(secretariaRepository);
            TurnoServiceImpl turnoService = new TurnoServiceImpl(
                    turnoRepository, pacienteRepository, odontologoRepository, secretariaRepository, facturador);

            // Controllers
            PacienteController   pacienteController   = new PacienteController(pacienteService);
            OdontologoController odontologoController = new OdontologoController(odontologoService);
            SecretariaController secretariaController = new SecretariaController(secretariaService);
            TurnoController      turnoController      = new TurnoController(turnoService);

            // Ventana principal
            MainFrame frame = new MainFrame(
                    persistencia,
                    pacienteController,
                    odontologoController,
                    secretariaController,
                    turnoController,
                    pacienteRepository,
                    odontologoRepository,
                    secretariaRepository,
                    turnoRepository
            );
            frame.setVisible(true);
        });
    }
}
