package entity;

import java.time.LocalDate;

public class Paciente extends Persona implements Comparable<Paciente> {

    private static final long serialVersionUID = 1L;

    private static long contadorId = 0L;

    private String email;
    private LocalDate fechaAlta;
    private Domicilio domicilio;
    private CoberturaPaciente cobertura;
    private final HistorialTurnos historialPaciente;

    public Paciente(String nombre,
                    String apellido,
                    Integer dni,
                    String email,
                    Domicilio domicilio,
                    CoberturaPaciente cobertura) {
        this(siguienteId(), nombre, apellido, dni, email, LocalDate.now(), domicilio, cobertura);
    }

    private Paciente(Long id,
                     String nombre,
                     String apellido,
                     Integer dni,
                     String email,
                     LocalDate fechaAlta,
                     Domicilio domicilio,
                     CoberturaPaciente cobertura) {
        super(id, nombre, apellido, dni);
        validarDatosPaciente(email, fechaAlta, domicilio, cobertura);
        this.email = email;
        this.fechaAlta = fechaAlta;
        this.domicilio = domicilio;
        this.cobertura = cobertura;
        this.historialPaciente = new HistorialTurnos();
        registrarIdExistente(id);
    }

    public static Paciente rehidratar(Long id,
                                      String nombre,
                                      String apellido,
                                      Integer dni,
                                      String email,
                                      LocalDate fechaAlta,
                                      Domicilio domicilio,
                                      CoberturaPaciente cobertura) {
        return new Paciente(id, nombre, apellido, dni, email, fechaAlta, domicilio, cobertura);
    }

    private static synchronized Long siguienteId() {
        return ++contadorId;
    }

    private static synchronized void registrarIdExistente(Long id) {
        contadorId = Math.max(contadorId, id);
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public Domicilio getDomicilio() {
        return domicilio;
    }

    public String getCalleDomicilio() {
        return domicilio.getCalle();
    }

    public Integer getNumeroDomicilio() {
        return domicilio.getNumero();
    }

    public String getLocalidadDomicilio() {
        return domicilio.getLocalidad();
    }

    public String getProvinciaDomicilio() {
        return domicilio.getProvincia();
    }

    public CoberturaPaciente getCobertura() {
        return cobertura;
    }

    public void actualizarDatos(String nombre,
                                String apellido,
                                Integer dni,
                                String email,
                                Domicilio domicilio,
                                CoberturaPaciente cobertura) {
        validarDatosPersonales(nombre, apellido, dni);
        validarDatosPaciente(email, fechaAlta, domicilio, cobertura);
        actualizarDatosPersonales(nombre, apellido, dni);
        this.email = email;
        this.domicilio = domicilio;
        this.cobertura = cobertura;
    }

    public Double calcularMonto(Odontologo odontologo) {
        if (odontologo == null) {
            throw new IllegalArgumentException("El odontologo no puede ser nulo.");
        }
        return cobertura.calcularMonto(odontologo);
    }

    public boolean tieneTurnos() {
        return historialPaciente.tieneTurnos();
    }

    void agregarTurno(Turno turno) {
        historialPaciente.agregar(turno);
    }

    void removerTurno(Turno turno) {
        historialPaciente.remover(turno);
    }

    private void validarDatosPaciente(String email,
                                      LocalDate fechaAlta,
                                      Domicilio domicilio,
                                      CoberturaPaciente cobertura) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacio.");
        }
        if (fechaAlta == null) {
            throw new IllegalArgumentException("La fecha de alta no puede ser nula.");
        }
        if (domicilio == null) {
            throw new IllegalArgumentException("El domicilio no puede ser nulo.");
        }
        if (cobertura == null) {
            throw new IllegalArgumentException("La cobertura no puede ser nula.");
        }
    }

    @Override
    public int compareTo(Paciente otro) {
        int cmpApellido = this.getApellido().compareToIgnoreCase(otro.getApellido());
        if (cmpApellido != 0) {
            return cmpApellido;
        }
        return this.getNombre().compareToIgnoreCase(otro.getNombre());
    }

    @Override
    public String toString() {
        return "\n=== Informacion del Paciente ===\n" +
                super.toString() +
                "\n Email: " + email +
                "\n Fecha Alta: " + fechaAlta +
                "\n Domicilio: " + domicilio +
                "\n Cobertura: " + cobertura;
    }
}
