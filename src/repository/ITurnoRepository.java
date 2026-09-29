package repository;

import entity.Turno;

public interface ITurnoRepository extends IRepository<Turno>,
        ConsultaTurnosRepository,
        EscrituraTurnosRepository,
        VerificadorConflictosTurno {
}
