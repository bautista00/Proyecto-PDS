package service;

import dto.TurnoEdicion;
import dto.TurnoRegistro;
import entity.EstadoTurno;
import entity.Odontologo;
import entity.Paciente;
import entity.Secretaria;
import entity.Turno;
import exception.DatoInvalidoException;
import repository.OdontologoRepository;
import repository.PacienteRepository;
import repository.SecretariaRepository;
import repository.TurnoRepository;

import java.time.LocalDate;
import java.util.List;

public class TurnoServiceImpl implements IService<Turno> {

    private final TurnoRepository turnoRepository;
    private final TurnoValidador turnoValidador;
    private final TurnoConsultaService turnoConsultaService;

    public TurnoServiceImpl(TurnoRepository turnoRepository,
                            PacienteRepository pacienteRepository,
                            OdontologoRepository odontologoRepository,
                            SecretariaRepository secretariaRepository,
                            Facturador facturador) {
        this.turnoRepository = turnoRepository;
        this.turnoValidador = new TurnoValidador(
                turnoRepository,
                pacienteRepository,
                odontologoRepository,
                secretariaRepository);
        this.turnoConsultaService = new TurnoConsultaService(turnoRepository, turnoValidador, facturador);
    }

    @Override
    public Turno registrar(Turno turno) {
        turnoValidador.validarTurnoNuevo(turno);
        turnoValidador.validarConflictoHorario(
                turno.getIdOdontologo(),
                turno.getFecha(),
                turno.getHora());
        turnoRepository.guardar(turno);
        turno.vincularConActores();
        return turno;
    }

    public Turno registrarTurno(TurnoRegistro datos) {
        if (datos == null) {
            throw new DatoInvalidoException("Los datos del turno no pueden ser nulos.");
        }

        Paciente paciente = turnoValidador.obtenerPacienteExistente(datos.getIdPaciente());
        Odontologo odontologo = turnoValidador.obtenerOdontologoExistente(datos.getIdOdontologo());
        Secretaria secretaria = turnoValidador.obtenerSecretariaExistente(datos.getIdSecretaria());

        turnoValidador.validarFechaHora(datos.getFecha(), datos.getHora());
        turnoValidador.validarMotivoConsulta(datos.getMotivoConsulta());
        turnoValidador.validarPuedeAtender(odontologo, datos.getMotivoConsulta());
        turnoValidador.validarConflictoHorario(odontologo.getId(), datos.getFecha(), datos.getHora());

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

    public List<Turno> listarPorPaciente(Long idPaciente) {
        return turnoConsultaService.listarPorPaciente(idPaciente);
    }

    public List<Turno> listarPorOdontologo(Long idOdontologo) {
        return turnoConsultaService.listarPorOdontologo(idOdontologo);
    }

    public List<Turno> listarPorSecretaria(Long idSecretaria) {
        return turnoConsultaService.listarPorSecretaria(idSecretaria);
    }

    public List<Turno> listarPorEstado(EstadoTurno estado) {
        return turnoConsultaService.listarPorEstado(estado);
    }

    public List<Turno> buscarPorRangoFechas(LocalDate desde, LocalDate hasta) {
        return turnoConsultaService.buscarPorRangoFechas(desde, hasta);
    }

    public List<Turno> buscarPorOdontologoYPaciente(Long idOdontologo, Long idPaciente) {
        return turnoConsultaService.buscarPorOdontologoYPaciente(idOdontologo, idPaciente);
    }

    @Override
    public Turno actualizar(Turno turno) {
        if (turno == null) {
            throw new DatoInvalidoException("El turno no puede ser nulo.");
        }

        turnoValidador.obtenerTurnoExistente(turno.getId());
        turnoValidador.validarTurnoActualizacion(turno);
        turnoValidador.validarConflictoHorarioExcluyendoTurno(
                turno.getId(),
                turno.getIdOdontologo(),
                turno.getFecha(),
                turno.getHora());
        turnoRepository.actualizar(turno);
        return turno;
    }

    public Turno modificarTurno(TurnoEdicion datos) {
        if (datos == null) {
            throw new DatoInvalidoException("Los datos de edicion del turno no pueden ser nulos.");
        }

        Turno turno = turnoValidador.obtenerTurnoExistente(datos.getIdTurno());
        Odontologo nuevoOdontologo = turnoValidador.obtenerOdontologoExistente(datos.getIdOdontologo());
        Secretaria nuevaSecretaria = turnoValidador.obtenerSecretariaExistente(datos.getIdSecretaria());

        validarDatosEdicion(datos, nuevoOdontologo);
        turnoValidador.validarConflictoHorarioExcluyendoTurno(
                turno.getId(),
                nuevoOdontologo.getId(),
                datos.getFecha(),
                datos.getHora());

        aplicarCambiosAlTurno(turno, nuevoOdontologo, nuevaSecretaria, datos);
        turnoRepository.actualizar(turno);
        return turno;
    }

    private void aplicarCambiosAlTurno(Turno turno,
                                       Odontologo nuevoOdontologo,
                                       Secretaria nuevaSecretaria,
                                       TurnoEdicion datos) {
        turno.cambiarOdontologo(nuevoOdontologo);
        turno.cambiarSecretaria(nuevaSecretaria);
        turno.setFecha(datos.getFecha());
        turno.setHora(datos.getHora());
        turno.setMotivoConsulta(datos.getMotivoConsulta());
        turno.setEstado(datos.getEstado());
    }

    private void validarDatosEdicion(TurnoEdicion datos, Odontologo odontologo) {
        turnoValidador.validarFechaHora(datos.getFecha(), datos.getHora());
        turnoValidador.validarMotivoConsulta(datos.getMotivoConsulta());
        turnoValidador.validarEstadoTurno(datos.getEstado());
        turnoValidador.validarPuedeAtender(odontologo, datos.getMotivoConsulta());
    }

    public Turno cambiarEstado(Long idTurno, EstadoTurno nuevoEstado) {
        turnoValidador.validarEstadoTurno(nuevoEstado);
        Turno turno = turnoValidador.obtenerTurnoExistente(idTurno);
        turno.setEstado(nuevoEstado);
        turnoRepository.actualizar(turno);
        return turno;
    }

    public Double calcularMonto(Long idTurno) {
        return turnoConsultaService.calcularMonto(idTurno);
    }

    @Override
    public boolean eliminar(Long idTurno) {
        Turno turno = turnoValidador.obtenerTurnoExistente(idTurno);
        turno.desvincularDeActores();
        turnoRepository.eliminar(idTurno);
        return true;
    }
}
