package dto;

import entity.EspecialidadOdontologica;

 //DTO para el registro de un odontólogo.
  

public class OdontologoRegistro {

    private final String nombre;
    private final String apellido;
    private final Integer dni;
    private final String matricula;
    private final EspecialidadOdontologica especialidad;

    public OdontologoRegistro(String nombre,
                              String apellido,
                              Integer dni,
                              String matricula,
                              EspecialidadOdontologica especialidad) {
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

    public EspecialidadOdontologica getEspecialidad() {
        return especialidad;
    }
}
