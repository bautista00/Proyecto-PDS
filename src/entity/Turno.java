package entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Turno implements Serializable {

    private static final long serialVersionUID = 1L;

    private static long contadorId = 0L;

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
        this(siguienteId(), paciente, odontologo, secretaria, fecha, hora, motivoConsulta, EstadoTurno.PENDIENTE);
    }

    private Turno(Long id,
                  Paciente paciente,
                  Odontologo odontologo,
                  Secretaria secretaria,
                  LocalDate fecha,
                  LocalTime hora,
                  String motivoConsulta,
                  EstadoTurno estado) {
        validar(id, paciente, odontologo, secretaria, fecha, hora, motivoConsulta, estado);
        this.id = id;
        this.paciente = paciente;
        this.odontologo = odontologo;
        this.secretaria = secretaria;
        this.fecha = fecha;
        this.hora = hora;
        this.motivoConsulta = motivoConsulta;
        this.estado = estado;
        registrarIdExistente(id);
    }

    public static Turno rehidratar(Long id,
                                   Paciente paciente,
                                   Odontologo odontologo,
                                   Secretaria secretaria,
                                   LocalDate fecha,
                                   LocalTime hora,
                                   String motivoConsulta,
                                   EstadoTurno estado) {
        return new Turno(id, paciente, odontologo, secretaria, fecha, hora, motivoConsulta, estado);
    }

    private static synchronized Long siguienteId() {
        return ++contadorId;
    }

    private static synchronized void registrarIdExistente(Long id) {
        contadorId = Math.max(contadorId, id);
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

    public EspecialidadOdontologica getEspecialidadOdontologo() {
        return odontologo.getEspecialidad();
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

    public LocalTime getHora() {
        return hora;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public EstadoTurno getEstado() {
        return estado;
    }

    public boolean estaActivoDesde(LocalDate fechaReferencia) {
        if (fechaReferencia == null) {
            throw new IllegalArgumentException("La fecha de referencia no puede ser nula.");
        }
        return !fecha.isBefore(fechaReferencia) && estado.estaActivo();
    }

    public boolean ocupaAgenda() {
        return estado.ocupaAgenda();
    }

    public Double calcularMonto() {
        return paciente.calcularMonto(odontologo);
    }

    public void actualizar(Odontologo nuevoOdontologo,
                           Secretaria nuevaSecretaria,
                           LocalDate nuevaFecha,
                           LocalTime nuevaHora,
                           String nuevoMotivo,
                           EstadoTurno nuevoEstado) {
        validar(id, paciente, nuevoOdontologo, nuevaSecretaria,
                nuevaFecha, nuevaHora, nuevoMotivo, nuevoEstado);
        cambiarOdontologo(nuevoOdontologo);
        cambiarSecretaria(nuevaSecretaria);
        this.fecha = nuevaFecha;
        this.hora = nuevaHora;
        this.motivoConsulta = nuevoMotivo;
        this.estado = nuevoEstado;
    }

    public void cambiarEstado(EstadoTurno estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado del turno no puede ser nulo.");
        }
        this.estado = estado;
    }

    private void validar(Long id,
                         Paciente paciente,
                         Odontologo odontologo,
                         Secretaria secretaria,
                         LocalDate fecha,
                         LocalTime hora,
                         String motivoConsulta,
                         EstadoTurno estado) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID del turno debe ser positivo.");
        }
        if (paciente == null || odontologo == null || secretaria == null) {
            throw new IllegalArgumentException("El turno debe tener paciente, odontologo y secretaria.");
        }
        if (fecha == null || hora == null) {
            throw new IllegalArgumentException("La fecha y la hora del turno son obligatorias.");
        }
        if (motivoConsulta == null || motivoConsulta.trim().isEmpty()) {
            throw new IllegalArgumentException("El motivo de consulta no puede estar vacio.");
        }
        if (estado == null) {
            throw new IllegalArgumentException("El estado del turno no puede ser nulo.");
        }
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
