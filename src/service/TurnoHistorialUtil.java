package service;

import entity.EstadoTurno;
import entity.Turno;

import java.time.LocalDate;
import java.util.List;

public class TurnoHistorialUtil {

    private TurnoHistorialUtil() {
    }

    // Hay turno a futuro si su fecha es hoy o posterior y sigue activo (PENDIENTE o CONFIRMADO)
    public static boolean tieneTurnosFuturos(List<Turno> turnos) {
        if (turnos == null) {
            return false;
        }

        LocalDate hoy = LocalDate.now();
        return turnos.stream().anyMatch(t ->
                !t.getFecha().isBefore(hoy)
                        && (t.getEstado() == EstadoTurno.PENDIENTE || t.getEstado() == EstadoTurno.CONFIRMADO));
    }
}
