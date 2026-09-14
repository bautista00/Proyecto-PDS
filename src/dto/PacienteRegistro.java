package dto;

import entity.CoberturaPaciente;

// DTO para el registro de un paciente.

public class PacienteRegistro {

    private String nombre;
    private String apellido;
    private Integer dni;
    private String email;
    private String calle;
    private Integer numero;
    private String localidad;
    private String provincia;
    private CoberturaPaciente cobertura;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Integer getDni() {
        return dni;
    }

    public void setDni(Integer dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public CoberturaPaciente getCobertura() {
        return cobertura;
    }

    public void setCobertura(CoberturaPaciente cobertura) {
        this.cobertura = cobertura;
    }
}
