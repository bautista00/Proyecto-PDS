package service;

import dto.TurnoEdicion;
import dto.TurnoRegistro;
import entity.EstadoTurno;
import entity.Odontologo;
import entity.Paciente;
import entity.Secretaria;
import entity.Turno;
import exception.DatoInvalidoException;
import repository.ITurnoRepository;

import java.time.LocalDate;
import java.util.List;


// Implementación del servicio de turnos, que se encarga de manejar la lógica de negocio relacionada con los turnos.
// Utiliza un repositorio de turnos para almacenar y recuperar los turnos, un validador de turnos para validar los datos de los turnos,
// un resolutor de turnos para obtener los detalles de los turnos, un validador de disponibilidad de turnos para verificar si un turno está disponible, 
// y un servicio de consulta de turnos para buscar y recuperar información sobre los turnos.

public class TurnoServiceImpl implements TurnoService {

    private final ITurnoRepository turnoRepository;
    private final TurnoValidador turnoValidador;
    private final TurnoResolutor turnoResolutor;
    private final DisponibilidadTurnos disponibilidadTurnos;
    private final TurnoConsultaService turnoConsultaService;

    public TurnoServiceImpl(ITurnoRepository turnoRepository,
                            TurnoValidador turnoValidador,
                            TurnoResolutor turnoResolutor,
                            DisponibilidadTurnos disponibilidadTurnos,
                            TurnoConsultaService turnoConsultaService) {
        this.turnoRepository = turnoRepository;
        this.turnoValidador = turnoValidador;
        this.turnoResolutor = turnoResolutor;
        this.disponibilidadTurnos = disponibilidadTurnos;
        this.turnoConsultaService = turnoConsultaService;
    }

    @Override
    public Turno registrarTurno(TurnoRegistro datos) {
        if (datos == null) {
            throw new DatoInvalidoException("Los datos del turno no pueden ser nulos.");
        }
        Paciente paciente = turnoResolutor.obtenerPaciente(datos.getIdPaciente());
        Odontologo odontologo = turnoResolutor.obtenerOdontologo(datos.getIdOdontologo());
        Secretaria secretaria = turnoResolutor.obtenerSecretaria(datos.getIdSecretaria());

        turnoValidador.validarDatos(
                datos.getFecha(),
                datos.getHora(),
                datos.getMotivoConsulta(),
                EstadoTurno.PENDIENTE,
                odontologo);
        disponibilidadTurnos.validarDisponible(
                odontologo.getId(), datos.getFecha(), datos.getHora());

        Turno turno = new Turno(
                paciente,
                odontologo,
                secretaria,
                datos.getFecha(),
                datos.getHora(),
                datos.getMotivoConsulta());
        turnoRepository.guardar(turno);
        turno.vincularConActores();
        return turno;
    }

    @Override
    public Turno buscarPorId(Long id) {
        return turnoConsultaService.buscarPorId(id);
    }

    @Override
    public List<Turno> listarTodos() {
        return turnoConsultaService.listarTodos();
    }

    @Override
    public List<Turno> listarPorPaciente(Long idPaciente) {
        return turnoConsultaService.listarPorPaciente(idPaciente);
    }

    @Override
    public List<Turno> listarPorOdontologo(Long idOdontologo) {
        return turnoConsultaService.listarPorOdontologo(idOdontologo);
    }

    @Override
    public List<Turno> listarPorSecretaria(Long idSecretaria) {
        return turnoConsultaService.listarPorSecretaria(idSecretaria);
    }

    @Override
    public List<Turno> listarPorEstado(EstadoTurno estado) {
        return turnoConsultaService.listarPorEstado(estado);
    }

    @Override
    public List<Turno> buscarPorRangoFechas(LocalDate desde, LocalDate hasta) {
        return turnoConsultaService.buscarPorRangoFechas(desde, hasta);
    }

    @Override
    public List<Turno> buscarPorOdontologoYPaciente(Long idOdontologo, Long idPaciente) {
        return turnoConsultaService.buscarPorOdontologoYPaciente(idOdontologo, idPaciente);
    }

    @Override
    public Turno modificarTurno(TurnoEdicion datos) {
        if (datos == null) {
            throw new DatoInvalidoException("Los datos de edicion del turno no pueden ser nulos.");
        }
        Turno turno = turnoResolutor.obtenerTurno(datos.getIdTurno());
        Odontologo odontologo = turnoResolutor.obtenerOdontologo(datos.getIdOdontologo());
        Secretaria secretaria = turnoResolutor.obtenerSecretaria(datos.getIdSecretaria());

        turnoValidador.validarDatos(
                datos.getFecha(),
                datos.getHora(),
                datos.getMotivoConsulta(),
                datos.getEstado(),
                odontologo);
        if (datos.getEstado().ocupaAgenda()) {
            disponibilidadTurnos.validarDisponibleExcluyendo(
                    turno.getId(), odontologo.getId(), datos.getFecha(), datos.getHora());
        }

        turno.actualizar(
                odontologo,
                secretaria,
                datos.getFecha(),
                datos.getHora(),
                datos.getMotivoConsulta(),
                datos.getEstado());
        turnoRepository.actualizar(turno);
        return turno;
    }

    @Override
    public Turno cambiarEstado(Long idTurno, EstadoTurno nuevoEstado) {
        turnoValidador.validarEstado(nuevoEstado);
        Turno turno = turnoResolutor.obtenerTurno(idTurno);
        if (nuevoEstado.ocupaAgenda()) {
            disponibilidadTurnos.validarDisponibleExcluyendo(
                    turno.getId(),
                    turno.getIdOdontologo(),
                    turno.getFecha(),
                    turno.getHora());
        }
        turno.cambiarEstado(nuevoEstado);
        turnoRepository.actualizar(turno);
        return turno;
    }

    @Override
    public Double calcularMonto(Long idTurno) {
        return turnoConsultaService.calcularMonto(idTurno);
    }

    @Override
    public boolean eliminar(Long idTurno) {
        Turno turno = turnoResolutor.obtenerTurno(idTurno);
        turno.desvincularDeActores();
        turnoRepository.eliminar(idTurno);
        return true;
    }
}
