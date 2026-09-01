package entity;

import java.util.ArrayList;
import java.util.List;

public abstract class Odontologo extends Persona {

    private static final long serialVersionUID = 1L;

    private static Long contadorId = 0L;

    public static void setContadorId(Long id) {
        contadorId = id;
    }

    private String matricula;
    private List<Turno> historialOdontologo;

    protected Odontologo(String nombre, String apellido, Integer dni, String matricula) {
        super(generarId(), nombre, apellido, dni);
        this.matricula = matricula;
        this.historialOdontologo = new ArrayList<>();
    }

    private static Long generarId() {
        return ++contadorId;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public List<Turno> getHistorialOdontologo() {
        return historialOdontologo;
    }

    public void agregarTurno(Turno turno) {
        if (turno != null && !historialOdontologo.contains(turno)) {
            historialOdontologo.add(turno);
        }
    }

    public void removerTurno(Turno turno) {
        historialOdontologo.remove(turno);
    }

    public abstract String getEspecialidad();

    public abstract Double getTarifaBase();

    public abstract boolean puedeAtender(String motivo);

    protected boolean motivoContieneAlgunaPalabra(String motivo, String... palabrasClave) {
        if (motivo == null || motivo.trim().isEmpty()) {
            return false;
        }

        String motivoNormalizado = motivo.toLowerCase();
        for (String palabraClave : palabrasClave) {
            if (motivoNormalizado.contains(palabraClave)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "\n=== Informacion del Odontologo ===" +
                "\nID: " + getId() +
                "\nNombre: " + getNombre() + " " + getApellido() +
                "\nDNI: " + getDni() +
                "\nMatricula: " + matricula +
                "\nEspecialidad: " + getEspecialidad();
    }
}