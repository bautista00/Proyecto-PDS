package entity;

public class Endodoncista extends Odontologo {

    private static final long serialVersionUID = 1L;

    private static final Double TARIFA_BASE = 80000.0;

    public Endodoncista(String nombre, String apellido, Integer dni, String matricula) {
        super(nombre, apellido, dni, matricula, EspecialidadOdontologica.ENDODONCIA);
    }

    @Override
    public Double getTarifaBase() {
        return TARIFA_BASE;
    }

    @Override
    public boolean puedeAtender(String motivo) {
        return motivoContieneAlgunaPalabra(
                motivo,
                "endodoncia",
                "conducto",
                "pulpa",
                "infeccion",
                "dolor intenso"
        );
    }
}
