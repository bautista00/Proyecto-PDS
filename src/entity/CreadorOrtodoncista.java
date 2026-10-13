package entity;

// Creador que usa OdontologoFactory para la especialidad ORTODONCIA: crea instancias de Ortodoncista.
public final class CreadorOrtodoncista implements CreadorOdontologo {

    @Override
    public EspecialidadOdontologica getEspecialidad() {
        return EspecialidadOdontologica.ORTODONCIA;
    }

    @Override
    public Odontologo crear(String nombre, String apellido, Integer dni, String matricula) {
        return new Ortodoncista(nombre, apellido, dni, matricula);
    }

    @Override
    public Odontologo rehidratar(Long id, String nombre, String apellido, Integer dni, String matricula) {
        return new Ortodoncista(id, nombre, apellido, dni, matricula);
    }
}
