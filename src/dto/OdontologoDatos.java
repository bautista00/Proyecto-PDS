package dto;

// DTO de salida: los datos de un odontólogo tal como los recibe la vista (el registro más su ID y su tarifa).
// Lo arma OdontologoController para que la vista no trabaje con la entidad Odontologo (MVC).
public class OdontologoDatos extends OdontologoRegistro {

    private final Long id;
    private final Double tarifaBase;

    public OdontologoDatos(Long id,
                           String nombre,
                           String apellido,
                           Integer dni,
                           String matricula,
                           String especialidad,
                           Double tarifaBase) {
        super(nombre, apellido, dni, matricula, especialidad);
        this.id = id;
        this.tarifaBase = tarifaBase;
    }

    public Long getId() {
        return id;
    }

    public Double getTarifaBase() {
        return tarifaBase;
    }
}
