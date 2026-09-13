package entity;

import java.time.LocalDate;

public class Paciente extends Persona implements Comparable<Paciente> {

    private static final long serialVersionUID = 1L;

    private static Long contadorId = 0L;

    public static void setContadorId(Long id) {
        contadorId = id;
    }

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
        super(++contadorId, nombre, apellido, dni);
        this.email = email;
        this.fechaAlta = LocalDate.now();
        this.domicilio = domicilio;
        this.cobertura = cobertura;
        this.historialPaciente = new HistorialTurnos();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
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

    public void setDomicilio(Domicilio domicilio) {
        this.domicilio = domicilio;
    }

    public CoberturaPaciente getCobertura() {
        return cobertura;
    }

    public void setCobertura(CoberturaPaciente cobertura) {
        this.cobertura = cobertura;
    }

    public boolean tieneTurnosFuturos() {
        return historialPaciente.tieneTurnosFuturos();
    }

    void agregarTurno(Turno turno) {
        historialPaciente.agregar(turno);
    }

    void removerTurno(Turno turno) {
        historialPaciente.remover(turno);
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
