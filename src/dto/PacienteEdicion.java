package dto;


// DTO para la edición de un paciente.

public class PacienteEdicion {

    private final Long idPaciente;
    private final PacienteRegistro datos;

    public PacienteEdicion(Long idPaciente, PacienteRegistro datos) {
        this.idPaciente = idPaciente;
        this.datos = datos;
    }

    public Long getIdPaciente() {
        return idPaciente;
    }

    public PacienteRegistro getDatos() {
        return datos;
    }
}
