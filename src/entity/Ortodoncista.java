package entity;

public class Ortodoncista extends Odontologo {

    private static final long serialVersionUID = 1L;

    private static final Double TARIFA_BASE = 70000.0;

    public Ortodoncista(String nombre, String apellido, Integer dni, String matricula) {
        super(nombre, apellido, dni, matricula);
    }

    @Override
    public String getEspecialidad() {
        return "Ortodoncia";
    }

    @Override
    public Double getTarifaBase() {
        return TARIFA_BASE;
    }

    @Override
    public boolean puedeAtender(String motivo) {
        if (motivo == null || motivo.trim().isEmpty()) {
            return false;
        }

        String motivoNormalizado = motivo.toLowerCase();

        return motivoNormalizado.contains("ortodoncia")
                || motivoNormalizado.contains("brackets")
                || motivoNormalizado.contains("alineadores")
                || motivoNormalizado.contains("mordida")
                || motivoNormalizado.contains("apiñamiento");
    }
}