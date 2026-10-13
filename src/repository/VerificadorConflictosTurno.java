package repository;

import java.time.LocalDate;
import java.time.LocalTime;

// Indica si un odontólogo ya tiene un turno que ocupa la agenda en una fecha y hora.
// La usa DisponibilidadTurnos sin depender del repositorio de turnos completo (ISP).
public interface VerificadorConflictosTurno {

    boolean existeConflictoHorario(Long idOdontologo, LocalDate fecha, LocalTime hora);

    boolean existeConflictoHorarioExcluyendoTurno(Long idTurno,
                                                   Long idOdontologo,
                                                   LocalDate fecha,
                                                   LocalTime hora);
}
