package entity;

public class Ortodoncista extends Odontologo {

    private static final long serialVersionUID = 1L;

    private static final Double TARIFA_BASE = 70000.0;

    public Ortodoncista(String nombre, String apellido, Integer dni, String matricula) {
        super(nombre, apellido, dni, matricula, EspecialidadOdontologica.ORTODONCIA);
    }

    @Override
    public Double getTarifaBase() {
        return TARIFA_BASE;
    }

    @Override
    public boolean puedeAtender(String motivo) {
        return motivoContieneAlgunaPalabra(
                motivo,
                "ortodoncia",
                "brackets",
                "alineadores",
                "mordida",
                "apiñamiento"
        );
    }
}
