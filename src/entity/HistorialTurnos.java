package entity;

import java.io.Serializable;
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

    boolean tieneTurnos() {
        return !turnos.isEmpty();
    }
}
