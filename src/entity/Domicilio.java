package entity;

import java.io.Serializable;

public class Domicilio implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String calle;
    private final Integer numero;
    private final String localidad;
    private final String provincia;

    public Domicilio(String calle, Integer numero, String localidad, String provincia) {
        this.calle = calle;
        this.numero = numero;
        this.localidad = localidad;
        this.provincia = provincia;
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
