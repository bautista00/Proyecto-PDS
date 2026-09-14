package service;

import exception.TurnoYaReservadoException;
import repository.ITurnoRepository;

import java.time.LocalDate;
import java.time.LocalTime;


// Clase que valida la disponibilidad de turnos para un odontólogo en una fecha y hora específica.
// Sera utilizada luego en el servicio de TurnoServiceImpl para validar la disponibilidad antes de crear o actualizar un turno.

public class DisponibilidadTurnos {

    private final ITurnoRepository turnoRepository;

    public DisponibilidadTurnos(ITurnoRepository turnoRepository) {
        this.turnoRepository = turnoRepository;
    }

    public void validarDisponible(Long idOdontologo, LocalDate fecha, LocalTime hora) {
        if (turnoRepository.existeConflictoHorario(idOdontologo, fecha, hora)) {
            throw new TurnoYaReservadoException("El odontologo ya tiene un turno reservado el "
                    + fecha + " a las " + hora + ".");
        }
    }

    public void validarDisponibleExcluyendo(Long idTurno,
                                             Long idOdontologo,
                                             LocalDate fecha,
                                             LocalTime hora) {
        if (turnoRepository.existeConflictoHorarioExcluyendoTurno(
                idTurno, idOdontologo, fecha, hora)) {
            throw new TurnoYaReservadoException("El odontologo ya tiene otro turno reservado el "
                    + fecha + " a las " + hora + ".");
        }
    }
}
