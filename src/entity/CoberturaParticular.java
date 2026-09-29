package entity;

public final class CoberturaParticular implements CoberturaPaciente {

    @Override
    public String getCodigo() {
        return "PARTICULAR";
    }

    @Override
    public String getDescripcion() {
        return "Particular";
    }

    @Override
    public Double calcularMonto(Odontologo odontologo) {
        return odontologo.getTarifaBase();
    }

    @Override
    public String toString() {
        return getDescripcion();
    }
}
