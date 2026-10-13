package entity;

// Sabe crear un tipo de odontólogo, nuevo o rehidratado desde el archivo.
// OdontologoFactory usa uno por especialidad, así sumar una especialidad no obliga a modificar la fábrica (OCP).
public interface CreadorOdontologo {

    EspecialidadOdontologica getEspecialidad();

    Odontologo crear(String nombre, String apellido, Integer dni, String matricula);

    Odontologo rehidratar(Long id,
                          String nombre,
                          String apellido,
                          Integer dni,
                          String matricula);
}
