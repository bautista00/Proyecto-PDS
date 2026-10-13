package dto;

// DTO de salida: los datos de un turno tal como los recibe la vista (el registro más su ID,
// su estado y los nombres de los actores). Lo arma TurnoController para que la vista no trabaje con la entidad Turno (MVC).
public class TurnoDatos extends TurnoRegistro {

    private Long id;
    private String nombrePaciente;
    private String nombreOdontologo;
    private String especialidad;
    private String nombreSecretaria;
    private String estado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getNombreOdontologo() {
        return nombreOdontologo;
    }

    public void setNombreOdontologo(String nombreOdontologo) {
        this.nombreOdontologo = nombreOdontologo;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getNombreSecretaria() {
        return nombreSecretaria;
    }

    public void setNombreSecretaria(String nombreSecretaria) {
        this.nombreSecretaria = nombreSecretaria;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
