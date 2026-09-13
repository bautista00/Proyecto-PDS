package entity;

public final class OdontologoFactory {

    private OdontologoFactory() {
    }

    public static Odontologo crear(EspecialidadOdontologica especialidad,
                                   String nombre,
                                   String apellido,
                                   Integer dni,
                                   String matricula) {
        if (especialidad == null) {
            throw new IllegalArgumentException("La especialidad no puede ser nula.");
        }

        switch (especialidad) {
            case GENERAL:
                return new OdontologoGeneral(nombre, apellido, dni, matricula);
            case ORTODONCIA:
                return new Ortodoncista(nombre, apellido, dni, matricula);
            case ENDODONCIA:
                return new Endodoncista(nombre, apellido, dni, matricula);
            default:
                throw new IllegalArgumentException("Especialidad no soportada: " + especialidad);
        }
    }
}
