package dto;

// DTO para el registro de una secretaria.

public class SecretariaRegistro {

    private final String nombre;
    private final String apellido;
    private final Integer dni;

    public SecretariaRegistro(String nombre, String apellido, Integer dni) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
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
}
