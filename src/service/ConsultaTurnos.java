package service;

import entity.EstadoTurno;
import entity.Turno;

import java.time.LocalDate;
import java.util.List;

// Consultas de turnos que ofrece la capa de servicio.
// Es una de las partes de TurnoService; la implementa TurnoConsultaService (ISP).
public interface ConsultaTurnos {

    Turno buscarPorId(Long id);

    List<Turno> listarTodos();

    List<Turno> listarPorPaciente(Long idPaciente);

    List<Turno> listarPorOdontologo(Long idOdontologo);

    List<Turno> listarPorSecretaria(Long idSecretaria);

    List<Turno> listarPorEstado(EstadoTurno estado);

    List<Turno> buscarPorRangoFechas(LocalDate desde, LocalDate hasta);

    List<Turno> buscarPorOdontologoYPaciente(Long idOdontologo, Long idPaciente);
}
