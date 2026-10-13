package repository;

import entity.Turno;

// Operaciones de escritura sobre turnos (guardar, actualizar y eliminar).
// La usa TurnoServiceImpl, que no necesita las consultas (ISP).
public interface EscrituraTurnosRepository extends RepositorioEscritura<Turno> {
}
