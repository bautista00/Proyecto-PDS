package entity;

public enum EstadoTurno {
    PENDIENTE,
    CONFIRMADO,
    CANCELADO,
    COMPLETADO;

    public boolean estaActivo() {
        return this == PENDIENTE || this == CONFIRMADO;
    }
}
