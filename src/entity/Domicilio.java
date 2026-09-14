package entity;

import java.io.Serializable;

public class Domicilio implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String calle;
    private final Integer numero;
    private final String localidad;
    private final String provincia;

    public Domicilio(String calle, Integer numero, String localidad, String provincia) {
        validar(calle, numero, localidad, provincia);
        this.calle = calle;
        this.numero = numero;
        this.localidad = localidad;
        this.provincia = provincia;
    }

    private void validar(String calle, Integer numero, String localidad, String provincia) {
        validarTexto(calle, "La calle no puede estar vacia.");
        if (numero == null || numero <= 0) {
            throw new IllegalArgumentException("El numero del domicilio debe ser positivo.");
        }
        validarTexto(localidad, "La localidad no puede estar vacia.");
        validarTexto(provincia, "La provincia no puede estar vacia.");
    }

    private void validarTexto(String valor, String mensaje) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(mensaje);
        }
    }

    public String getCalle() {
        return calle;
    }

    public Integer getNumero() {
        return numero;
    }

    public String getLocalidad() {
        return localidad;
    }

    public String getProvincia() {
        return provincia;
    }
    @Override
    public String toString() {
        return "\n === Informacion del Domicilio ===" +
                "\n Calle: " + calle +
                "\n Numero: " + numero +
                "\n Localidad: " + localidad +
                "\n Provincia: " + provincia ;
    }
}
