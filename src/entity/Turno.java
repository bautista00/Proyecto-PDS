package entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Turno implements Serializable {

    private static final long serialVersionUID = 1L;

    private static Long contadorId = 0L;

    public static void setContadorId(Long id) {
        contadorId = id;
    }

    private Long id;
    private Paciente paciente;
    private Odontologo odontologo;
    private Secretaria secretaria;
    private LocalDate fecha;
    private LocalTime hora;
    private String motivoConsulta;
    private EstadoTurno estado;

    public Turno(Paciente paciente, Odontologo odontologo, Secretaria secretaria,
                 LocalDate fecha, LocalTime hora, String motivoConsulta) {
        this.id = generarId();
        this.paciente = paciente;
        this.odontologo = odontologo;
        this.secretaria = secretaria;
        this.fecha = fecha;
        this.hora = hora;
        this.motivoConsulta = motivoConsulta;
        this.estado = EstadoTurno.PENDIENTE;
    }

    private static Long generarId() {
        return ++contadorId;
    }

    public Long getId() {
        return id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Odontologo getOdontologo() {
        return odontologo;
    }

    public void setOdontologo(Odontologo odontologo) {
        this.odontologo = odontologo;
    }

    public Secretaria getSecretaria() {
        return secretaria;
    }

    public void setSecretaria(Secretaria secretaria) {
        this.secretaria = secretaria;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    public EstadoTurno getEstado() {
        return estado;
    }

    public void setEstado(EstadoTurno estado) {
        this.estado = estado;
    }

    public String generarMensajeRecordatorio() {
        return "Recordatorio de turno: Paciente " + paciente.getNombre() + " " + paciente.getApellido() +
                ", Odontologo " + odontologo.getNombre() + " " + odontologo.getApellido() +
                ", fecha " + fecha +
                ", hora " + hora +
                ", motivo " + motivoConsulta + ".";
    }

    @Override
    public String toString() {
        return "\n=== Informacion del Turno ===" +
                "\nID: " + id +
                "\nPaciente: " + paciente.getNombre() + " " + paciente.getApellido() +
                "\nOdontologo: " + odontologo.getNombre() + " " + odontologo.getApellido() +
                "\nSecretaria: " + secretaria.getNombre() + " " + secretaria.getApellido() +
                "\nFecha: " + fecha +
                "\nHora: " + hora +
                "\nMotivo: " + motivoConsulta +
                "\nEstado: " + estado;
    }
}