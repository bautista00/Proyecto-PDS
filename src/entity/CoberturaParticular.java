package entity;

public class CoberturaParticular implements CoberturaPaciente {

    @Override
    public Double calcularMonto(Odontologo odontologo) {
        return odontologo.getTarifaBase();
    }
}
