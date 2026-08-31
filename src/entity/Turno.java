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

    public Long getIdPaciente() {
        return paciente.getId();
    }

    public String getNombrePaciente() {
        return paciente.getNombreCompleto();
    }

    public Odontologo getOdontologo() {
        return odontologo;
    }

    public Long getIdOdontologo() {
        return odontologo.getId();
    }

    public String getNombreOdontologo() {
        return odontologo.getNombreCompleto();
    }

    public String getEspecialidadOdontologo() {
        return odontologo.getEspecialidad();
    }

    public void setOdontologo(Odontologo odontologo) {
        this.odontologo = odontologo;
    }

    public Secretaria getSecretaria() {
        return secretaria;
    }

    public Long getIdSecretaria() {
        return secretaria.getId();
    }

    public String getNombreSecretaria() {
        return secretaria.getNombreCompleto();
    }

    public void setSecretaria(Secretaria secretaria) {
        this.secretaria = secretaria;
    }

    public void vincularConActores() {
        paciente.agregarTurno(this);
        odontologo.agregarTurno(this);
        secretaria.agregarTurno(this);
    }

    public void desvincularDeActores() {
        paciente.removerTurno(this);
        odontologo.removerTurno(this);
        secretaria.removerTurno(this);
    }

    public void cambiarOdontologo(Odontologo nuevoOdontologo) {
        if (!odontologo.getId().equals(nuevoOdontologo.getId())) {
            odontologo.removerTurno(this);
            nuevoOdontologo.agregarTurno(this);
        }
        this.odontologo = nuevoOdontologo;
    }

    public void cambiarSecretaria(Secretaria nuevaSecretaria) {
        if (!secretaria.getId().equals(nuevaSecretaria.getId())) {
            secretaria.removerTurno(this);
            nuevaSecretaria.agregarTurno(this);
        }
        this.secretaria = nuevaSecretaria;
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
        return "Recordatorio de turno: Paciente " + getNombrePaciente() +
                ", Odontologo " + getNombreOdontologo() +
                ", fecha " + fecha +
                ", hora " + hora +
                ", motivo " + motivoConsulta + ".";
    }

    @Override
    public String toString() {
        return "\n=== Informacion del Turno ===" +
                "\nID: " + id +
                "\nPaciente: " + getNombrePaciente() +
                "\nOdontologo: " + getNombreOdontologo() +
                "\nSecretaria: " + getNombreSecretaria() +
                "\nFecha: " + fecha +
                "\nHora: " + hora +
                "\nMotivo: " + motivoConsulta +
                "\nEstado: " + estado;
    }
}