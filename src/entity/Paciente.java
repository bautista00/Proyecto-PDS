package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Paciente extends Persona implements Comparable<Paciente> {

    private static final long serialVersionUID = 1L;

    private static Long contadorId = 0L;

    public static void setContadorId(Long id) {
        contadorId = id;
    }

    private String email;
    private LocalDate fechaAlta;
    private Domicilio domicilio;
    private Boolean obraSocial;
    private List<Turno> historialPaciente;

    public Paciente(String nombre, String apellido, Integer dni, String email, Domicilio domicilio, Boolean obraSocial) {
        super(++contadorId, nombre, apellido, dni);
        this.email = email;
        this.fechaAlta = LocalDate.now();
        this.domicilio = domicilio;
        this.obraSocial = obraSocial;
        this.historialPaciente = new ArrayList<>();
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

    public void setDomicilio(Domicilio domicilio) {
        this.domicilio = domicilio;
    }

    public Boolean getObraSocial() {
        return obraSocial;
    }

    public void setObraSocial(Boolean obraSocial) {
        this.obraSocial = obraSocial;
    }

    public List<Turno> getHistorialPaciente() {
        return historialPaciente;
    }

    public void setHistorialPaciente(List<Turno> historialPaciente) {
        this.historialPaciente = historialPaciente;
    }

    public void agregarTurno(Turno turno) {
        if (turno != null && !historialPaciente.contains(turno)) {
            historialPaciente.add(turno);
        }
    }

    public void removerTurno(Turno turno) {
        historialPaciente.remove(turno);
    }

    @Override
    public int compareTo(Paciente otro) {
        int cmpApellido = this.apellido.compareToIgnoreCase(otro.apellido);
        if (cmpApellido != 0) {
            return cmpApellido;
        }
        return this.nombre.compareToIgnoreCase(otro.nombre);
    }

    @Override
    public String toString() {
        return "\n=== Informacion del Paciente ===\n" +
                super.toString() +
                "\n Email: " + email +
                "\n Fecha Alta: " + fechaAlta +
                "\n Domicilio: " + domicilio +
                "\n Obra Social: " + (obraSocial ? "Si" : "No");
    }
}
