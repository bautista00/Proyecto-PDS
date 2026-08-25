package service;

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
import java.time.LocalTime;

public class TurnoAgendaService {

    private final TurnoRepository turnoRepository;
    private final PacienteRepository pacienteRepository;
    private final OdontologoRepository odontologoRepository;
    private final SecretariaRepository secretariaRepository;
    private final TurnoValidador turnoValidador;
    private final TurnoRelacionador turnoRelacionador;

    public TurnoAgendaService(TurnoRepository turnoRepository,
                              PacienteRepository pacienteRepository,
                              OdontologoRepository odontologoRepository,
                              SecretariaRepository secretariaRepository,
                              TurnoValidador turnoValidador,
                              TurnoRelacionador turnoRelacionador) {
        this.turnoRepository = turnoRepository;
        this.pacienteRepository = pacienteRepository;
        this.odontologoRepository = odontologoRepository;
        this.secretariaRepository = secretariaRepository;
        this.turnoValidador = turnoValidador;
        this.turnoRelacionador = turnoRelacionador;
    }

    public Turno registrar(Turno turno) {
        turnoValidador.validarTurnoNuevo(turno, pacienteRepository, odontologoRepository, secretariaRepository);
        turnoValidador.validarConflictoHorario(turno.getOdontologo().getId(), turno.getFecha(), turno.getHora(), turnoRepository);
        turnoRepository.guardar(turno);
        turnoRelacionador.sincronizarAlta(turno);
        return turno;
    }

    public Turno registrarTurno(Long idPaciente,
                                Long idOdontologo,
                                Long idSecretaria,
                                LocalDate fecha,
                                LocalTime hora,
                                String motivoConsulta) {
        Paciente paciente = turnoValidador.obtenerPacienteExistente(idPaciente, pacienteRepository);
        Odontologo odontologo = turnoValidador.obtenerOdontologoExistente(idOdontologo, odontologoRepository);
        Secretaria secretaria = turnoValidador.obtenerSecretariaExistente(idSecretaria, secretariaRepository);

        turnoValidador.validarFechaHora(fecha, hora);
        turnoValidador.validarMotivoConsulta(motivoConsulta);

        if (!odontologo.puedeAtender(motivoConsulta)) {
            throw new DatoInvalidoException("El odontologo seleccionado no puede atender ese motivo de consulta.");
        }

        turnoValidador.validarConflictoHorario(odontologo.getId(), fecha, hora, turnoRepository);

        Turno turno = new Turno(paciente, odontologo, secretaria, fecha, hora, motivoConsulta);
        turnoRepository.guardar(turno);
        turnoRelacionador.sincronizarAlta(turno);
        return turno;
    }

    public Turno actualizar(Turno turno) {
        turnoValidador.validarTurnoActualizacion(turno, pacienteRepository, odontologoRepository, secretariaRepository);
        turnoValidador.validarConflictoHorarioExcluyendoTurno(
                turno.getId(),
                turno.getOdontologo().getId(),
                turno.getFecha(),
                turno.getHora(),
                turnoRepository);
        turnoRepository.actualizar(turno);
        return turno;
    }

    public Turno modificarTurno(Long idTurno,
                                Long idOdontologo,
                                Long idSecretaria,
                                LocalDate fecha,
                                LocalTime hora,
                                String motivoConsulta,
                                EstadoTurno estado) {

        Turno turno = turnoValidador.obtenerTurnoExistente(idTurno, turnoRepository);
        Odontologo odontologoAnterior = turno.getOdontologo();
        Secretaria secretariaAnterior = turno.getSecretaria();

        Odontologo nuevoOdontologo = turnoValidador.obtenerOdontologoExistente(idOdontologo, odontologoRepository);
        Secretaria nuevaSecretaria = turnoValidador.obtenerSecretariaExistente(idSecretaria, secretariaRepository);

        turnoValidador.validarFechaHora(fecha, hora);
        turnoValidador.validarMotivoConsulta(motivoConsulta);
        turnoValidador.validarEstadoTurno(estado);

        if (!nuevoOdontologo.puedeAtender(motivoConsulta)) {
            throw new DatoInvalidoException("El odontologo seleccionado no puede atender ese motivo de consulta.");
        }

        turnoValidador.validarConflictoHorarioExcluyendoTurno(
                turno.getId(),
                nuevoOdontologo.getId(),
                fecha,
                hora,
                turnoRepository);

        turnoRelacionador.aplicarCambiosAlTurno(turno, nuevoOdontologo, nuevaSecretaria, fecha, hora, motivoConsulta, estado);
        turnoRelacionador.sincronizarActoresTurno(turno, odontologoAnterior, secretariaAnterior, nuevoOdontologo, nuevaSecretaria);
        turnoRepository.actualizar(turno);
        return turno;
    }

    public Turno cambiarEstado(Long idTurno, EstadoTurno nuevoEstado) {
        turnoValidador.validarEstadoTurno(nuevoEstado);
        Turno turno = turnoValidador.obtenerTurnoExistente(idTurno, turnoRepository);
        turno.setEstado(nuevoEstado);
        turnoRepository.actualizar(turno);
        return turno;
    }

    public boolean eliminar(Long idTurno) {
        Turno turno = turnoValidador.obtenerTurnoExistente(idTurno, turnoRepository);
        turnoRelacionador.sincronizarBaja(turno);
        turnoRepository.eliminar(idTurno);
        return true;
    }
}
