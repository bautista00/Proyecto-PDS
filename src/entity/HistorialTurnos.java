package entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

final class HistorialTurnos implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<Turno> turnos = new ArrayList<>();

    void agregar(Turno turno) {
        if (turno != null && !turnos.contains(turno)) {
            turnos.add(turno);
        }
    }

    void remover(Turno turno) {
        turnos.remove(turno);
    }

    boolean tieneTurnosFuturos() {
        return tieneTurnosActivosDesde(LocalDate.now());
    }

    boolean tieneTurnos() {
        return !turnos.isEmpty();
    }

    private boolean tieneTurnosActivosDesde(LocalDate fechaReferencia) {
        if (fechaReferencia == null) {
            throw new IllegalArgumentException("La fecha de referencia no puede ser nula.");
        }
        return turnos.stream()
                .anyMatch(turno -> turno.estaActivoDesde(fechaReferencia));
    }
}
