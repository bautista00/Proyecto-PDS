package entity;

public class OdontologoGeneral extends Odontologo {

    private static final long serialVersionUID = 1L;

    private static final Double TARIFA_BASE = 50000.0;

    public OdontologoGeneral(String nombre, String apellido, Integer dni, String matricula) {
        super(nombre, apellido, dni, matricula);
    }

    @Override
    public String getEspecialidad() {
        return "Odontologia General";
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

        return motivoConsulta.contains("consulta")
                || motivoConsulta.contains("control")
                || motivoConsulta.contains("limpieza")
                || motivoConsulta.contains("caries")
                || motivoConsulta.contains("dolor");
    }
}