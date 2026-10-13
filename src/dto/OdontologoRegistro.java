package dto;

 //DTO para el registro de un odontólogo.
  

public class OdontologoRegistro {

    private final String nombre;
    private final String apellido;
    private final Integer dni;
    private final String matricula;
    private final String especialidad;

    public OdontologoRegistro(String nombre,
                              String apellido,
                              Integer dni,
                              String matricula,
                              String especialidad) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.matricula = matricula;
        this.especialidad = especialidad;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public Integer getDni() {
        return dni;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getEspecialidad() {
        return especialidad;
    }
}
