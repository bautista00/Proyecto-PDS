package entity;

import java.util.ArrayList;
import java.util.List;

public class Secretaria extends Persona {

    private static final long serialVersionUID = 1L;

    private static Long contadorId = 0L;

    private List<Turno> historialSecretaria;

    public Secretaria(String nombre, String apellido, Integer dni) {
        super(++contadorId, nombre, apellido, dni);
        this.historialSecretaria = new ArrayList<>();
    }


    public static void setContadorId(Long contadorId) {
        Secretaria.contadorId = contadorId;
    }

    public List<Turno> getHistorialSecretaria() {
        return historialSecretaria;
    }

    public void setHistorialSecretaria(List<Turno> historialSecretaria) {
        this.historialSecretaria = historialSecretaria;
    }

    public void agregarTurno(Turno turno) {
        if (turno != null && !historialSecretaria.contains(turno)) {
            historialSecretaria.add(turno);
        }
    }

    public void removerTurno(Turno turno) {
        historialSecretaria.remove(turno);
    }

    @Override
    public String toString() {
        return "\n=== Informacion de la Secretaria ===" +
                "\n ID: " + id +
                "\n Nombre: " + nombre + " " + apellido +
                "\n DNI: " + dni;
    }
}