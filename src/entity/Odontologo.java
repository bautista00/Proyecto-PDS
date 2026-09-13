package entity;

public abstract class Odontologo extends Persona {

    private static final long serialVersionUID = 1L;

    private static Long contadorId = 0L;

    public static void setContadorId(Long id) {
        contadorId = id;
    }

    private String matricula;
    private final EspecialidadOdontologica especialidad;
    private final HistorialTurnos historialOdontologo;

    protected Odontologo(String nombre,
                         String apellido,
                         Integer dni,
                         String matricula,
                         EspecialidadOdontologica especialidad) {
        super(generarId(), nombre, apellido, dni);
        this.matricula = matricula;
        if (especialidad == null) {
            throw new IllegalArgumentException("La especialidad no puede ser nula.");
        }
        this.especialidad = especialidad;
        this.historialOdontologo = new HistorialTurnos();
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

    public boolean tieneTurnosFuturos() {
        return historialOdontologo.tieneTurnosFuturos();
    }

    void agregarTurno(Turno turno) {
        historialOdontologo.agregar(turno);
    }

    void removerTurno(Turno turno) {
        historialOdontologo.remover(turno);
    }

    public EspecialidadOdontologica getEspecialidad() {
        return especialidad;
    }

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
