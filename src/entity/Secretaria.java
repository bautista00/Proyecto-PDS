package entity;

public class Secretaria extends Persona {

    private static final long serialVersionUID = 1L;

    private static long contadorId = 0L;

    private final HistorialTurnos historialSecretaria;

    public Secretaria(String nombre, String apellido, Integer dni) {
        this(siguienteId(), nombre, apellido, dni);
    }

    private Secretaria(Long id, String nombre, String apellido, Integer dni) {
        super(id, nombre, apellido, dni);
        this.historialSecretaria = new HistorialTurnos();
        registrarIdExistente(id);
    }

    public static Secretaria rehidratar(Long id, String nombre, String apellido, Integer dni) {
        return new Secretaria(id, nombre, apellido, dni);
    }

    private static synchronized Long siguienteId() {
        return ++contadorId;
    }

    private static synchronized void registrarIdExistente(Long id) {
        contadorId = Math.max(contadorId, id);
    }

    public void actualizarDatos(String nombre, String apellido, Integer dni) {
        actualizarDatosPersonales(nombre, apellido, dni);
    }

    public boolean tieneTurnos() {
        return historialSecretaria.tieneTurnos();
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
