package dto;

// DTO de salida: los datos de una secretaria tal como los recibe la vista (el registro más su ID).
// Lo arma SecretariaController para que la vista no trabaje con la entidad Secretaria (MVC).
public class SecretariaDatos extends SecretariaRegistro {

    private final Long id;

    public SecretariaDatos(Long id, String nombre, String apellido, Integer dni) {
        super(nombre, apellido, dni);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
