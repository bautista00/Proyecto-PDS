package config;

import controller.OdontologoController;
import controller.PacienteController;
import controller.SecretariaController;
import controller.TurnoController;
import entity.CatalogoCoberturas;
import entity.CoberturaPaciente;
import entity.CreadorEndodoncista;
import entity.CreadorOdontologoGeneral;
import entity.CreadorOrtodoncista;
import entity.EspecialidadOdontologica;
import entity.OdontologoFactory;
import persistence.ArchivoTexto;
import persistence.PersistenciaServicio;
import repository.OdontologoRepository;
import repository.PacienteRepository;
import repository.SecretariaRepository;
import repository.TurnoRepository;
import service.Facturador;
import service.FacturacionTurnoService;
import service.DisponibilidadTurnos;
import service.OdontologoServiceImpl;
import service.PacienteServiceImpl;
import service.SecretariaServiceImpl;
import service.TurnoConsultaService;
import service.TurnoResolutor;
import service.TurnoServiceImpl;
import service.TurnoValidador;

import java.util.Arrays;
import java.util.List;

//
 //Raíz de composición: único lugar donde se crean y conectan repositorios,
 //servicios y controladores. Carga los datos al construirse y los guarda con guardarDatos().
 //
public class DependenciasClinica {

    private final PersistenciaServicio persistencia;
    private final CatalogoCoberturas catalogoCoberturas;
    private final OdontologoFactory odontologoFactory;
    private final PacienteRepository pacienteRepository;
    private final OdontologoRepository odontologoRepository;
    private final SecretariaRepository secretariaRepository;
    private final TurnoRepository turnoRepository;
    private final PacienteController pacienteController;
    private final OdontologoController odontologoController;
    private final SecretariaController secretariaController;
    private final TurnoController turnoController;

    // Arma en orden: persistencia -> repositorios -> servicios -> controladores.
    // Los turnos se cargan al final porque referencian pacientes, odontólogos y secretarias.
    public DependenciasClinica() {
        catalogoCoberturas = new CatalogoCoberturas(Arrays.asList(
                CoberturaPaciente.PARTICULAR,
                CoberturaPaciente.OBRA_SOCIAL));
        odontologoFactory = new OdontologoFactory(Arrays.asList(
                new CreadorOdontologoGeneral(),
                new CreadorOrtodoncista(),
                new CreadorEndodoncista()));
        persistencia = new PersistenciaServicio(
                new ArchivoTexto(), catalogoCoberturas, odontologoFactory);

        pacienteRepository = new PacienteRepository();
        odontologoRepository = new OdontologoRepository();
        secretariaRepository = new SecretariaRepository();
        turnoRepository = new TurnoRepository();

        persistencia.cargarPacientes(pacienteRepository);
        persistencia.cargarOdontologos(odontologoRepository);
        persistencia.cargarSecretarias(secretariaRepository);
        persistencia.cargarTurnos(
                pacienteRepository,
                odontologoRepository,
                secretariaRepository,
                turnoRepository);

        PacienteServiceImpl pacienteService = new PacienteServiceImpl(pacienteRepository);
        OdontologoServiceImpl odontologoService = new OdontologoServiceImpl(
                odontologoRepository, odontologoFactory);
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
                turnoResolutor);
        FacturacionTurnoService facturacionTurnoService = new FacturacionTurnoService(
                turnoResolutor,
                facturador);
        TurnoServiceImpl turnoService = new TurnoServiceImpl(
                turnoRepository,
                turnoValidador,
                turnoResolutor,
                disponibilidadTurnos,
                turnoConsultaService,
                facturacionTurnoService);

        pacienteController = new PacienteController(pacienteService);
        odontologoController = new OdontologoController(odontologoService);
        secretariaController = new SecretariaController(secretariaService);
        turnoController = new TurnoController(turnoService);
    }

    // Guarda los datos de pacientes, odontólogos, secretarias y turnos en la persistencia.
    // Se llama al cerrar la aplicación para no perder los datos.

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

    public List<CoberturaPaciente> getCoberturas() {
        return catalogoCoberturas.listar();
    }

    public List<EspecialidadOdontologica> getEspecialidades() {
        return odontologoFactory.listarEspecialidades();
    }
}
