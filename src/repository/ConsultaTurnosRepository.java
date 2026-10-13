package repository;

import entity.EstadoTurno;
import entity.Turno;

import java.time.LocalDate;
import java.util.List;

// Consultas de turnos por paciente, odontólogo, secretaria, estado y rango de fechas.
// La usa TurnoConsultaService, que solo necesita leer turnos (ISP).
public interface ConsultaTurnosRepository extends RepositorioLectura<Turno> {

    List<Turno> listarPorPaciente(Long idPaciente);

    List<Turno> listarPorOdontologo(Long idOdontologo);

    List<Turno> listarPorSecretaria(Long idSecretaria);

    List<Turno> listarPorEstado(EstadoTurno estado);

    List<Turno> buscarPorRangoFechas(LocalDate desde, LocalDate hasta);

    List<Turno> buscarPorOdontologoYPaciente(Long idOdontologo, Long idPaciente);
}
