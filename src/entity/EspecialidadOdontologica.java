package entity;

public enum EspecialidadOdontologica {
    GENERAL("Odontologia General"),
    ORTODONCIA("Ortodoncia"),
    ENDODONCIA("Endodoncia");

    private final String descripcion;

    EspecialidadOdontologica(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
