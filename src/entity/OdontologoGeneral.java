package entity;

public class OdontologoGeneral extends Odontologo {

    private static final long serialVersionUID = 1L;

    private static final Double TARIFA_BASE = 50000.0;

    public OdontologoGeneral(String nombre, String apellido, Integer dni, String matricula) {
        super(nombre, apellido, dni, matricula, EspecialidadOdontologica.GENERAL);
    }

    OdontologoGeneral(Long id, String nombre, String apellido, Integer dni, String matricula) {
        super(id, nombre, apellido, dni, matricula, EspecialidadOdontologica.GENERAL);
    }

    @Override
    public Double getTarifaBase() {
        return TARIFA_BASE;
    }

    @Override
    public boolean puedeAtender(String motivo) {
        return motivoContieneAlgunaPalabra(
                motivo,
                "consulta",
                "control",
                "limpieza",
                "caries",
                "dolor"
        );
    }
}
