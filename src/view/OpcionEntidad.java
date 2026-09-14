package view;

import entity.Persona;

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
