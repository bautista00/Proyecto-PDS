package repository;

import java.time.LocalDate;
import java.time.LocalTime;

public interface VerificadorConflictosTurno {

    boolean existeConflictoHorario(Long idOdontologo, LocalDate fecha, LocalTime hora);

    boolean existeConflictoHorarioExcluyendoTurno(Long idTurno,
                                                   Long idOdontologo,
                                                   LocalDate fecha,
                                                   LocalTime hora);
}
