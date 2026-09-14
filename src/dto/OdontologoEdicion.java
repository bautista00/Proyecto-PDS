package dto;


//DTO para la edición de un odontólogo.
public class OdontologoEdicion {

    private final Long idOdontologo;
    private final OdontologoRegistro datos;

    public OdontologoEdicion(Long idOdontologo, OdontologoRegistro datos) {
        this.idOdontologo = idOdontologo;
        this.datos = datos;
    }

    public Long getIdOdontologo() {
        return idOdontologo;
    }

    public OdontologoRegistro getDatos() {
        return datos;
    }
}
