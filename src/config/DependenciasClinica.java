package config;

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
import service.DisponibilidadTurnos;
import service.OdontologoServiceImpl;
import service.PacienteServiceImpl;
import service.SecretariaServiceImpl;
import service.TurnoConsultaService;
import service.TurnoResolutor;
import service.TurnoServiceImpl;
import service.TurnoValidador;

public class DependenciasClinica {

    private final PersistenciaServicio persistencia;
    private final PacienteRepository pacienteRepository;
    private final OdontologoRepository odontologoRepository;
    private final SecretariaRepository secretariaRepository;
    private final TurnoRepository turnoRepository;
    private final PacienteController pacienteController;
    private final OdontologoController odontologoController;
    private final SecretariaController secretariaController;
    private final TurnoController turnoController;

    public DependenciasClinica() {
        persistencia = new PersistenciaServicio();

        pacienteRepository = persistencia.cargarPacientes();
        odontologoRepository = persistencia.cargarOdontologos();
        secretariaRepository = persistencia.cargarSecretarias();
        turnoRepository = persistencia.cargarTurnos(
                pacienteRepository,
                odontologoRepository,
                secretariaRepository);

        PacienteServiceImpl pacienteService = new PacienteServiceImpl(pacienteRepository);
        OdontologoServiceImpl odontologoService = new OdontologoServiceImpl(odontologoRepository);
        SecretariaServiceImpl secretariaService = new SecretariaServiceImpl(secretariaRepository);
        Facturador facturador = new Facturador();
        TurnoValidador turnoValidador = new TurnoValidador();
        TurnoResolutor turnoResolutor = new TurnoResolutor(
                turnoRepository,
                pacienteRepository,
                odontologoRepository,
                secretariaRepository);
        DisponibilidadTurnos disponibilidadTurnos = new DisponibilidadTurnos(turnoRepository);
        TurnoConsultaService turnoConsultaService = new TurnoConsultaService(
                turnoRepository,
                turnoResolutor,
                facturador);
        TurnoServiceImpl turnoService = new TurnoServiceImpl(
                turnoRepository,
                turnoValidador,
                turnoResolutor,
                disponibilidadTurnos,
                turnoConsultaService);

        pacienteController = new PacienteController(pacienteService);
        odontologoController = new OdontologoController(odontologoService);
        secretariaController = new SecretariaController(secretariaService);
        turnoController = new TurnoController(turnoService);
    }

    public void guardarDatos() {
        persistencia.guardar(
                pacienteRepository,
                odontologoRepository,
                secretariaRepository,
                turnoRepository);
    }

    public PacienteController getPacienteController() {
        return pacienteController;
    }

    public OdontologoController getOdontologoController() {
        return odontologoController;
    }

    public SecretariaController getSecretariaController() {
        return secretariaController;
    }

    public TurnoController getTurnoController() {
        return turnoController;
    }
}
