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

    public void validarTurnoNuevo(Turno turno,
                                  PacienteRepository pacienteRepository,
                                  OdontologoRepository odontologoRepository,
                                  SecretariaRepository secretariaRepository) {
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

        obtenerPacienteExistente(turno.getPaciente().getId(), pacienteRepository);
        obtenerOdontologoExistente(turno.getOdontologo().getId(), odontologoRepository);
        obtenerSecretariaExistente(turno.getSecretaria().getId(), secretariaRepository);
        validarFechaHora(turno.getFecha(), turno.getHora());
        validarMotivoConsulta(turno.getMotivoConsulta());

        if (!turno.getOdontologo().puedeAtender(turno.getMotivoConsulta())) {
            throw new DatoInvalidoException("El odontologo seleccionado no puede atender ese motivo de consulta.");
        }
    }

    public void validarTurnoActualizacion(Turno turno,
                                          PacienteRepository pacienteRepository,
                                          OdontologoRepository odontologoRepository,
                                          SecretariaRepository secretariaRepository) {
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

        obtenerPacienteExistente(turno.getPaciente().getId(), pacienteRepository);
        obtenerOdontologoExistente(turno.getOdontologo().getId(), odontologoRepository);
        obtenerSecretariaExistente(turno.getSecretaria().getId(), secretariaRepository);
        validarFechaHora(turno.getFecha(), turno.getHora());
        validarMotivoConsulta(turno.getMotivoConsulta());
        validarEstadoTurno(turno.getEstado());

        if (!turno.getOdontologo().puedeAtender(turno.getMotivoConsulta())) {
            throw new DatoInvalidoException("El odontologo seleccionado no puede atender ese motivo de consulta.");
        }
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

    public Paciente obtenerPacienteExistente(Long idPaciente, PacienteRepository pacienteRepository) {
        ValidacionesClinica.validarIdPacientePositivo(idPaciente);
        Paciente paciente = pacienteRepository.buscarPorId(idPaciente);
        if (paciente == null) {
            throw new PacienteNoEncontradoException("No existe un paciente con ID " + idPaciente + ".");
        }
        return paciente;
    }

    public Odontologo obtenerOdontologoExistente(Long idOdontologo, OdontologoRepository odontologoRepository) {
        ValidacionesClinica.validarIdOdontologoPositivo(idOdontologo);
        Odontologo odontologo = odontologoRepository.buscarPorId(idOdontologo);
        if (odontologo == null) {
            throw new OdontologoNoEncontradoException("No existe un odontologo con ID " + idOdontologo + ".");
        }
        return odontologo;
    }

    public Secretaria obtenerSecretariaExistente(Long idSecretaria, SecretariaRepository secretariaRepository) {
        ValidacionesClinica.validarIdSecretariaPositivo(idSecretaria);
        Secretaria secretaria = secretariaRepository.buscarPorId(idSecretaria);
        if (secretaria == null) {
            throw new DatoInvalidoException("No existe una secretaria con ID " + idSecretaria + ".");
        }
        return secretaria;
    }

    public Turno obtenerTurnoExistente(Long idTurno, TurnoRepository turnoRepository) {
        ValidacionesClinica.validarIdTurnoPositivo(idTurno);
        Turno turno = turnoRepository.buscarPorId(idTurno);
        if (turno == null) {
            throw new DatoInvalidoException("No existe un turno con ID " + idTurno + ".");
        }
        return turno;
    }

    public void validarConflictoHorario(Long idOdontologo,
                                        LocalDate fecha,
                                        LocalTime hora,
                                        TurnoRepository turnoRepository) {
        if (turnoRepository.existeConflictoHorario(idOdontologo, fecha, hora)) {
            throw new TurnoYaReservadoException("El odontologo ya tiene un turno reservado el "
                    + fecha + " a las " + hora + ".");
        }
    }

    public void validarConflictoHorarioExcluyendoTurno(Long idTurno,
                                                       Long idOdontologo,
                                                       LocalDate fecha,
                                                       LocalTime hora,
                                                       TurnoRepository turnoRepository) {
        if (turnoRepository.existeConflictoHorarioExcluyendoTurno(idTurno, idOdontologo, fecha, hora)) {
            throw new TurnoYaReservadoException("El odontologo ya tiene otro turno reservado el "
                    + fecha + " a las " + hora + ".");
        }
    }
}
