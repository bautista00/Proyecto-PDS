package entity;

public interface CreadorOdontologo {

    EspecialidadOdontologica getEspecialidad();

    Odontologo crear(String nombre, String apellido, Integer dni, String matricula);

    Odontologo rehidratar(Long id,
                          String nombre,
                          String apellido,
                          Integer dni,
                          String matricula);
}
