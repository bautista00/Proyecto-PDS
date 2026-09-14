package service;

import dto.TurnoEdicion;
import dto.TurnoRegistro;
import entity.EstadoTurno;
import entity.Turno;

import java.time.LocalDate;
import java.util.List;

public interface TurnoService {

    Turno registrarTurno(TurnoRegistro datos);

    Turno buscarPorId(Long id);

    List<Turno> listarTodos();

    List<Turno> listarPorPaciente(Long idPaciente);

    List<Turno> listarPorOdontologo(Long idOdontologo);

    List<Turno> listarPorSecretaria(Long idSecretaria);

    List<Turno> listarPorEstado(EstadoTurno estado);

    List<Turno> buscarPorRangoFechas(LocalDate desde, LocalDate hasta);

    List<Turno> buscarPorOdontologoYPaciente(Long idOdontologo, Long idPaciente);

    Turno modificarTurno(TurnoEdicion datos);

    Turno cambiarEstado(Long idTurno, EstadoTurno nuevoEstado);

    Double calcularMonto(Long idTurno);

    boolean eliminar(Long idTurno);
}
