package view;

// Representa un botón de filtro de turnos: el texto que se muestra y la acción que ejecuta.
// TurnoFiltrosPanel arma sus botones con una lista de estas, así agregar un filtro no cambia el panel (OCP).
public final class AccionFiltroTurno {

    private final String texto;
    private final Runnable accion;

    public AccionFiltroTurno(String texto, Runnable accion) {
        if (texto == null || texto.trim().isEmpty() || accion == null) {
            throw new IllegalArgumentException("El texto y la accion del filtro son obligatorios.");
        }
        this.texto = texto;
        this.accion = accion;
    }

    public String getTexto() {
        return texto;
    }

    public void ejecutar() {
        accion.run();
    }
}
