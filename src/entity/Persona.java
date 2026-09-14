package entity;

import java.io.Serializable;

public abstract class Persona implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String nombre;
    private String apellido;
    private Integer dni;

    public Persona(Long id, String nombre, String apellido, Integer dni) {
        validarId(id);
        validarDatosPersonales(nombre, apellido, dni);
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
    }

    public Long getId() {
        return id;
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

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    protected final void actualizarDatosPersonales(String nombre, String apellido, Integer dni) {
        validarDatosPersonales(nombre, apellido, dni);
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
    }

    protected final void validarDatosPersonales(String nombre, String apellido, Integer dni) {
        validarNombre(nombre, "El nombre no puede estar vacio.");
        validarNombre(apellido, "El apellido no puede estar vacio.");
        if (dni == null || dni <= 0) {
            throw new IllegalArgumentException("El DNI debe ser un numero positivo.");
        }
    }

    private void validarNombre(String valor, String mensajeVacio) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(mensajeVacio);
        }
        if (!valor.matches("[\\p{L}' ]+")) {
            throw new IllegalArgumentException("El nombre y el apellido solo pueden contener letras.");
        }
    }

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un numero positivo.");
        }
    }

    @Override
    public String toString() {
        return "\n=== Informacion General ===" +
                "\nID: " + id +
                "\nNombre: " + nombre +
                "\nApellido: " + apellido +
                "\nDNI: " + dni;
    }
}
