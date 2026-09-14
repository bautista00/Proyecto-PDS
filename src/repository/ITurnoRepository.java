package repository;

import entity.EstadoTurno;
import entity.Turno;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ITurnoRepository extends IRepository<Turno> {

    List<Turno> listarPorPaciente(Long idPaciente);

    List<Turno> listarPorOdontologo(Long idOdontologo);

    List<Turno> listarPorSecretaria(Long idSecretaria);

    List<Turno> listarPorEstado(EstadoTurno estado);

    List<Turno> buscarPorRangoFechas(LocalDate desde, LocalDate hasta);

    List<Turno> buscarPorOdontologoYPaciente(Long idOdontologo, Long idPaciente);

    boolean existeConflictoHorario(Long idOdontologo, LocalDate fecha, LocalTime hora);

    boolean existeConflictoHorarioExcluyendoTurno(Long idTurno,
                                                  Long idOdontologo,
                                                  LocalDate fecha,
                                                  LocalTime hora);
}
