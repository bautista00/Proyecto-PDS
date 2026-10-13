package service;

import dto.TurnoEdicion;
import dto.TurnoRegistro;
import entity.EstadoTurno;
import entity.Turno;

// Operaciones que modifican turnos: registrar, modificar, cambiar estado y eliminar.
// Es una de las tres partes en que se dividió TurnoService (ISP).
public interface GestionTurnos {

    Turno registrarTurno(TurnoRegistro datos);

    Turno modificarTurno(TurnoEdicion datos);

    Turno cambiarEstado(Long idTurno, EstadoTurno nuevoEstado);

    boolean eliminar(Long idTurno);
}
