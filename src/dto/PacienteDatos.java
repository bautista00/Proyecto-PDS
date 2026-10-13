package dto;

// DTO de salida: los datos de un paciente tal como los recibe la vista (el registro más su ID).
// Lo arma PacienteController para que la vista no trabaje con la entidad Paciente (MVC).
public class PacienteDatos extends PacienteRegistro {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
