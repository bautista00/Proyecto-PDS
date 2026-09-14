package entity;

public abstract class Odontologo extends Persona {

    private static final long serialVersionUID = 1L;

    private static long contadorId = 0L;

    private String matricula;
    private final EspecialidadOdontologica especialidad;
    private final HistorialTurnos historialOdontologo;

    protected Odontologo(String nombre,
                         String apellido,
                         Integer dni,
                         String matricula,
                         EspecialidadOdontologica especialidad) {
        this(siguienteId(), nombre, apellido, dni, matricula, especialidad);
    }

    protected Odontologo(Long id,
                         String nombre,
                         String apellido,
                         Integer dni,
                         String matricula,
                         EspecialidadOdontologica especialidad) {
        super(id, nombre, apellido, dni);
        validarDatosOdontologo(matricula, especialidad);
        this.matricula = matricula;
        this.especialidad = especialidad;
        this.historialOdontologo = new HistorialTurnos();
        registrarIdExistente(id);
    }

    private static synchronized Long siguienteId() {
        return ++contadorId;
    }

    private static synchronized void registrarIdExistente(Long id) {
        contadorId = Math.max(contadorId, id);
    }

    public String getMatricula() {
        return matricula;
    }

    public void actualizarDatos(String nombre, String apellido, Integer dni, String matricula) {
        validarDatosPersonales(nombre, apellido, dni);
        validarDatosOdontologo(matricula, especialidad);
        actualizarDatosPersonales(nombre, apellido, dni);
        this.matricula = matricula;
    }

    public boolean tieneTurnosFuturos() {
        return historialOdontologo.tieneTurnosFuturos();
    }

    public boolean tieneTurnos() {
        return historialOdontologo.tieneTurnos();
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

    private void validarDatosOdontologo(String matricula, EspecialidadOdontologica especialidad) {
        if (matricula == null || matricula.trim().isEmpty()) {
            throw new IllegalArgumentException("La matricula no puede estar vacia.");
        }
        if (especialidad == null) {
            throw new IllegalArgumentException("La especialidad no puede ser nula.");
        }
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
