package entity;

public class Secretaria extends Persona {

    private static final long serialVersionUID = 1L;

    private static Long contadorId = 0L;

    private final HistorialTurnos historialSecretaria;

    public Secretaria(String nombre, String apellido, Integer dni) {
        super(++contadorId, nombre, apellido, dni);
        this.historialSecretaria = new HistorialTurnos();
    }


    public static void setContadorId(Long contadorId) {
        Secretaria.contadorId = contadorId;
    }

    void agregarTurno(Turno turno) {
        historialSecretaria.agregar(turno);
    }

    void removerTurno(Turno turno) {
        historialSecretaria.remover(turno);
    }

    @Override
    public String toString() {
        return "\n=== Informacion de la Secretaria ===" +
                "\n ID: " + getId() +
                "\n Nombre: " + getNombre() + " " + getApellido() +
                "\n DNI: " + getDni();
    }
}
