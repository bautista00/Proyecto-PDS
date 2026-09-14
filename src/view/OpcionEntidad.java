package view;

import entity.Persona;

// Envoltorio para mostrar una entidad (paciente, odontólogo o secretaria) dentro de un combo.
// Guarda la entidad junto con la etiqueta que se ve en pantalla, y devuelve esa etiqueta
// desde toString(), que es lo que Swing usa para dibujar cada ítem.
// Así el combo muestra texto legible pero sigue teniendo el ID a mano para armar los DTOs.
final class OpcionEntidad<T extends Persona> {

    private final T entidad;
    private final String etiqueta;

    OpcionEntidad(T entidad, String etiqueta) {
        this.entidad = entidad;
        this.etiqueta = etiqueta;
    }

    Long getId() {
        return entidad.getId();
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
