package entity;

// Creador que usa OdontologoFactory para la especialidad ENDODONCIA: crea instancias de Endodoncista.
public final class CreadorEndodoncista implements CreadorOdontologo {

    @Override
    public EspecialidadOdontologica getEspecialidad() {
        return EspecialidadOdontologica.ENDODONCIA;
    }

    @Override
    public Odontologo crear(String nombre, String apellido, Integer dni, String matricula) {
        return new Endodoncista(nombre, apellido, dni, matricula);
    }

    @Override
    public Odontologo rehidratar(Long id, String nombre, String apellido, Integer dni, String matricula) {
        return new Endodoncista(id, nombre, apellido, dni, matricula);
    }
}
