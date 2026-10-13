package view;

// Envoltorio para mostrar un paciente, odontólogo o secretaria dentro de un combo.
// Guarda el ID junto con la etiqueta que se ve en pantalla, y devuelve esa etiqueta
// desde toString(), que es lo que Swing usa para dibujar cada ítem.
// Así el combo muestra texto legible pero sigue teniendo el ID a mano para armar los DTOs.
final class OpcionEntidad {

    private final Long id;
    private final String etiqueta;

    OpcionEntidad(Long id, String etiqueta) {
        this.id = id;
        this.etiqueta = etiqueta;
    }

    Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
