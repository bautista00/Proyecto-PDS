package entity;

public enum EstadoTurno {
    PENDIENTE,
    CONFIRMADO,
    CANCELADO,
    COMPLETADO;

    public boolean ocupaAgenda() {
        return this == PENDIENTE || this == CONFIRMADO;
    }

}
