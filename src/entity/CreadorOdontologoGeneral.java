package entity;

// Creador que usa OdontologoFactory para la especialidad GENERAL: crea instancias de OdontologoGeneral.
public final class CreadorOdontologoGeneral implements CreadorOdontologo {

    @Override
    public EspecialidadOdontologica getEspecialidad() {
        return EspecialidadOdontologica.GENERAL;
    }

    @Override
    public Odontologo crear(String nombre, String apellido, Integer dni, String matricula) {
        return new OdontologoGeneral(nombre, apellido, dni, matricula);
    }

    @Override
    public Odontologo rehidratar(Long id, String nombre, String apellido, Integer dni, String matricula) {
        return new OdontologoGeneral(id, nombre, apellido, dni, matricula);
    }
}
