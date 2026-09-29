package entity;

import java.util.Locale;
import java.util.Objects;

public final class EspecialidadOdontologica {

    public static final EspecialidadOdontologica GENERAL =
            new EspecialidadOdontologica("GENERAL", "Odontologia General");
    public static final EspecialidadOdontologica ORTODONCIA =
            new EspecialidadOdontologica("ORTODONCIA", "Ortodoncia");
    public static final EspecialidadOdontologica ENDODONCIA =
            new EspecialidadOdontologica("ENDODONCIA", "Endodoncia");

    private final String codigo;
    private final String descripcion;

    public EspecialidadOdontologica(String codigo, String descripcion) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El codigo de especialidad no puede estar vacio.");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripcion de especialidad no puede estar vacia.");
        }
        this.codigo = codigo.trim().toUpperCase(Locale.ROOT);
        this.descripcion = descripcion.trim();
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) {
            return true;
        }
        if (!(otro instanceof EspecialidadOdontologica)) {
            return false;
        }
        EspecialidadOdontologica especialidad = (EspecialidadOdontologica) otro;
        return codigo.equals(especialidad.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
