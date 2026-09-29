package service;

import dto.TurnoEdicion;
import dto.TurnoRegistro;
import entity.EstadoTurno;
import entity.Turno;

public interface GestionTurnos {

    Turno registrarTurno(TurnoRegistro datos);

    Turno modificarTurno(TurnoEdicion datos);

    Turno cambiarEstado(Long idTurno, EstadoTurno nuevoEstado);

    boolean eliminar(Long idTurno);
}
