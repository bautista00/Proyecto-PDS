package entity;

public class Endodoncista extends Odontologo {

    private static final long serialVersionUID = 1L;

    private static final Double TARIFA_BASE = 80000.0;

    public Endodoncista(String nombre, String apellido, Integer dni, String matricula) {
        super(nombre, apellido, dni, matricula);
    }

    @Override
    public String getEspecialidad() {
        return "Endodoncia";
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

        String motivoConsulta = motivo.toLowerCase();

        return motivoConsulta.contains("endodoncia")
                || motivoConsulta.contains("conducto")
                || motivoConsulta.contains("pulpa")
                || motivoConsulta.contains("infeccion")
                || motivoConsulta.contains("dolor intenso");
    }
}
