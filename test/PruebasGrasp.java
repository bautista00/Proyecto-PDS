package test;

import dto.PacienteEdicion;
import dto.PacienteRegistro;
import dto.SecretariaRegistro;
import dto.TurnoRegistro;
import entity.CoberturaPaciente;
import entity.Domicilio;
import entity.EspecialidadOdontologica;
import entity.EstadoTurno;
import entity.Odontologo;
import entity.OdontologoFactory;
import entity.Paciente;
import entity.Secretaria;
import entity.Turno;
import exception.DatoInvalidoException;
import exception.TurnoYaReservadoException;
import repository.OdontologoRepository;
import repository.PacienteRepository;
import repository.SecretariaRepository;
import repository.TurnoRepository;
import service.DisponibilidadTurnos;
import service.Facturador;
import service.PacienteServiceImpl;
import service.SecretariaServiceImpl;
import service.TurnoConsultaService;
import service.TurnoResolutor;
import service.TurnoServiceImpl;
import service.TurnoValidador;

import java.time.LocalDate;
import java.time.LocalTime;

public class PruebasGrasp {

    public static void main(String[] args) {
        PacienteRepository pacientes = new PacienteRepository();
        OdontologoRepository odontologos = new OdontologoRepository();
        SecretariaRepository secretarias = new SecretariaRepository();
        TurnoRepository turnos = new TurnoRepository();

        PacienteServiceImpl pacienteService = new PacienteServiceImpl(pacientes);
        SecretariaServiceImpl secretariaService = new SecretariaServiceImpl(secretarias);

        Paciente paciente = pacienteService.registrar(datosPaciente("Ana", 30111222));
        Paciente otroPaciente = pacienteService.registrar(datosPaciente("Belen", 30222333));
        comprobarActualizacionAtomica(pacienteService, paciente, otroPaciente);

        Odontologo odontologo = OdontologoFactory.crear(
                EspecialidadOdontologica.GENERAL,
                "Carlos",
                "Perez",
                25111222,
                "MAT-1");
        odontologos.guardar(odontologo);
        Secretaria secretaria = secretariaService.registrar(
                new SecretariaRegistro("Diana", "Lopez", 28111222));

        TurnoResolutor resolutor = new TurnoResolutor(turnos, pacientes, odontologos, secretarias);
        Facturador facturador = new Facturador();
        TurnoConsultaService consultas = new TurnoConsultaService(turnos, resolutor, facturador);
        TurnoServiceImpl turnoService = new TurnoServiceImpl(
                turnos,
                new TurnoValidador(),
                resolutor,
                new DisponibilidadTurnos(turnos),
                consultas);

        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime hora = LocalTime.of(10, 0);
        Turno primerTurno = turnoService.registrarTurno(
                datosTurno(paciente.getId(), odontologo.getId(), secretaria.getId(), fecha, hora));

        comprobarIntegridadAlEliminar(secretariaService, secretaria);
        comprobarConsultas(turnoService, paciente, odontologo, secretaria);
        comprobarFacturacion(turnoService, primerTurno);
        comprobarHorarioLiberado(turnoService, primerTurno, paciente, odontologo, secretaria, fecha, hora);
        comprobarRehidratacionDeIds();

        System.out.println("Pruebas GRASP superadas.");
    }

    private static void comprobarActualizacionAtomica(PacienteServiceImpl service,
                                                       Paciente paciente,
                                                       Paciente otroPaciente) {
        String nombreOriginal = paciente.getNombre();
        PacienteRegistro datosInvalidos = datosPaciente("Nombre Modificado", otroPaciente.getDni());
        try {
            service.actualizar(new PacienteEdicion(paciente.getId(), datosInvalidos));
            throw new AssertionError("La actualizacion con DNI duplicado debio fallar.");
        } catch (DatoInvalidoException esperada) {
            comprobar(nombreOriginal.equals(paciente.getNombre()),
                    "La entidad fue modificada antes de completar la validacion.");
        }
    }

    private static void comprobarIntegridadAlEliminar(SecretariaServiceImpl service,
                                                       Secretaria secretaria) {
        try {
            service.eliminar(secretaria.getId());
            throw new AssertionError("No se debio eliminar una secretaria con turnos.");
        } catch (DatoInvalidoException esperada) {
            comprobar(true, "La eliminacion fue bloqueada.");
        }
    }

    private static void comprobarConsultas(TurnoServiceImpl service,
                                            Paciente paciente,
                                            Odontologo odontologo,
                                            Secretaria secretaria) {
        comprobar(service.listarPorPaciente(paciente.getId()).size() == 1,
                "La consulta por paciente no devolvio el turno.");
        comprobar(service.listarPorOdontologo(odontologo.getId()).size() == 1,
                "La consulta por odontologo no devolvio el turno.");
        comprobar(service.listarPorSecretaria(secretaria.getId()).size() == 1,
                "La consulta por secretaria no devolvio el turno.");
        comprobar(service.listarPorEstado(EstadoTurno.PENDIENTE).size() == 1,
                "La consulta por estado no devolvio el turno.");
    }

    private static void comprobarFacturacion(TurnoServiceImpl service, Turno turno) {
        double monto = service.calcularMonto(turno.getId());
        comprobar(monto == turno.getOdontologo().getTarifaBase(),
                "La facturacion del turno particular es incorrecta.");
    }

    private static void comprobarHorarioLiberado(TurnoServiceImpl service,
                                                  Turno primerTurno,
                                                  Paciente paciente,
                                                  Odontologo odontologo,
                                                  Secretaria secretaria,
                                                  LocalDate fecha,
                                                  LocalTime hora) {
        service.cambiarEstado(primerTurno.getId(), EstadoTurno.CANCELADO);
        Turno segundoTurno = service.registrarTurno(
                datosTurno(paciente.getId(), odontologo.getId(), secretaria.getId(), fecha, hora));
        comprobar(segundoTurno.getId() != null,
                "Un turno cancelado continuo bloqueando el horario.");
        try {
            service.cambiarEstado(primerTurno.getId(), EstadoTurno.CONFIRMADO);
            throw new AssertionError("No se debio reactivar un turno sobre un horario ocupado.");
        } catch (TurnoYaReservadoException esperada) {
            comprobar(true, "La reactivacion conflictiva fue bloqueada.");
        }
    }

    private static void comprobarRehidratacionDeIds() {
        Paciente.rehidratar(
                500L,
                "Elena",
                "Suarez",
                33111222,
                "elena@mail.com",
                LocalDate.now(),
                new Domicilio("Belgrano", 500, "CABA", "Buenos Aires"),
                CoberturaPaciente.PARTICULAR);
        Paciente nuevo = new Paciente(
                "Fabian",
                "Torres",
                34111222,
                "fabian@mail.com",
                new Domicilio("Rivadavia", 600, "CABA", "Buenos Aires"),
                CoberturaPaciente.PARTICULAR);
        comprobar(nuevo.getId() > 500L,
                "La rehidratacion no actualizo la secuencia de IDs.");
    }

    private static PacienteRegistro datosPaciente(String nombre, Integer dni) {
        PacienteRegistro datos = new PacienteRegistro();
        datos.setNombre(nombre);
        datos.setApellido("Gomez");
        datos.setDni(dni);
        datos.setEmail(nombre.toLowerCase().replace(" ", "") + "@mail.com");
        datos.setCalle("Corrientes");
        datos.setNumero(123);
        datos.setLocalidad("CABA");
        datos.setProvincia("Buenos Aires");
        datos.setCobertura(CoberturaPaciente.PARTICULAR);
        return datos;
    }

    private static TurnoRegistro datosTurno(Long idPaciente,
                                             Long idOdontologo,
                                             Long idSecretaria,
                                             LocalDate fecha,
                                             LocalTime hora) {
        TurnoRegistro datos = new TurnoRegistro();
        datos.setIdPaciente(idPaciente);
        datos.setIdOdontologo(idOdontologo);
        datos.setIdSecretaria(idSecretaria);
        datos.setFecha(fecha);
        datos.setHora(hora);
        datos.setMotivoConsulta("control general");
        return datos;
    }

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
    }
}
