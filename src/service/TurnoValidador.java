package service;

import entity.EstadoTurno;
import entity.Odontologo;
import entity.Paciente;
import entity.Secretaria;
import entity.Turno;
import exception.DatoInvalidoException;
import exception.OdontologoNoEncontradoException;
import exception.PacienteNoEncontradoException;
import exception.TurnoYaReservadoException;
import repository.OdontologoRepository;
import repository.PacienteRepository;
import repository.SecretariaRepository;
import repository.TurnoRepository;

import java.time.LocalDate;
import java.time.LocalTime;

public class TurnoValidador {

    private final TurnoRepository turnoRepository;
    private final PacienteRepository pacienteRepository;
    private final OdontologoRepository odontologoRepository;
    private final SecretariaRepository secretariaRepository;

    public TurnoValidador(TurnoRepository turnoRepository,
                          PacienteRepository pacienteRepository,
                          OdontologoRepository odontologoRepository,
                          SecretariaRepository secretariaRepository) {
        this.turnoRepository = turnoRepository;
        this.pacienteRepository = pacienteRepository;
        this.odontologoRepository = odontologoRepository;
        this.secretariaRepository = secretariaRepository;
    }

    public void validarTurnoNuevo(Turno turno) {
        validarDatosComunesTurno(turno);
    }

    public void validarTurnoActualizacion(Turno turno) {
        validarDatosComunesTurno(turno);
        validarEstadoTurno(turno.getEstado());
    }

    private void validarDatosComunesTurno(Turno turno) {
        if (turno == null) {
            throw new DatoInvalidoException("El turno no puede ser nulo.");
        }
        if (turno.getPaciente() == null) {
            throw new DatoInvalidoException("El paciente del turno no existe.");
        }
        if (turno.getOdontologo() == null) {
            throw new DatoInvalidoException("El odontologo del turno no existe.");
        }
        if (turno.getSecretaria() == null) {
            throw new DatoInvalidoException("La secretaria del turno no existe.");
        }

        obtenerPacienteExistente(turno.getIdPaciente());
        obtenerOdontologoExistente(turno.getIdOdontologo());
        obtenerSecretariaExistente(turno.getIdSecretaria());
        validarFechaHora(turno.getFecha(), turno.getHora());
        validarMotivoConsulta(turno.getMotivoConsulta());
        validarPuedeAtender(turno.getOdontologo(), turno.getMotivoConsulta());
    }

    public void validarFechaHora(LocalDate fecha, LocalTime hora) {
        if (fecha == null) {
            throw new DatoInvalidoException("La fecha no puede ser nula.");
        }
        if (hora == null) {
            throw new DatoInvalidoException("La hora no puede ser nula.");
        }
    }

    public void validarMotivoConsulta(String motivoConsulta) {
        ValidacionesClinica.validarMotivoConsultaNoVacio(motivoConsulta);
    }

    public void validarEstadoTurno(EstadoTurno estado) {
        if (estado == null) {
            throw new DatoInvalidoException("El estado del turno no puede ser nulo.");
        }
    }

    public void validarPuedeAtender(Odontologo odontologo, String motivoConsulta) {
        if (!odontologo.puedeAtender(motivoConsulta)) {
            throw new DatoInvalidoException("El odontologo seleccionado no puede atender ese motivo de consulta.");
        }
    }

    public Paciente obtenerPacienteExistente(Long idPaciente) {
        ValidacionesClinica.validarIdPacientePositivo(idPaciente);
        Paciente paciente = pacienteRepository.buscarPorId(idPaciente);
        if (paciente == null) {
            throw new PacienteNoEncontradoException("No existe un paciente con ID " + idPaciente + ".");
        }
        return paciente;
    }

    public Odontologo obtenerOdontologoExistente(Long idOdontologo) {
        ValidacionesClinica.validarIdOdontologoPositivo(idOdontologo);
        Odontologo odontologo = odontologoRepository.buscarPorId(idOdontologo);
        if (odontologo == null) {
            throw new OdontologoNoEncontradoException("No existe un odontologo con ID " + idOdontologo + ".");
        }
        return odontologo;
    }

    public Secretaria obtenerSecretariaExistente(Long idSecretaria) {
        ValidacionesClinica.validarIdSecretariaPositivo(idSecretaria);
        Secretaria secretaria = secretariaRepository.buscarPorId(idSecretaria);
        if (secretaria == null) {
            throw new DatoInvalidoException("No existe una secretaria con ID " + idSecretaria + ".");
        }
        return secretaria;
    }

    public Turno obtenerTurnoExistente(Long idTurno) {
        ValidacionesClinica.validarIdTurnoPositivo(idTurno);
        Turno turno = turnoRepository.buscarPorId(idTurno);
        if (turno == null) {
            throw new DatoInvalidoException("No existe un turno con ID " + idTurno + ".");
        }
        return turno;
    }

    public void validarConflictoHorario(Long idOdontologo, LocalDate fecha, LocalTime hora) {
        if (turnoRepository.existeConflictoHorario(idOdontologo, fecha, hora)) {
            throw new TurnoYaReservadoException("El odontologo ya tiene un turno reservado el "
                    + fecha + " a las " + hora + ".");
        }
    }

    public void validarConflictoHorarioExcluyendoTurno(Long idTurno,
                                                       Long idOdontologo,
                                                       LocalDate fecha,
                                                       LocalTime hora) {
        if (turnoRepository.existeConflictoHorarioExcluyendoTurno(idTurno, idOdontologo, fecha, hora)) {
            throw new TurnoYaReservadoException("El odontologo ya tiene otro turno reservado el "
                    + fecha + " a las " + hora + ".");
        }
    }
}
