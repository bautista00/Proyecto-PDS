package entity;

public class CoberturaObraSocial implements CoberturaPaciente {

    private static final Double COPAGO = 10000.0;

    @Override
    public Double calcularMonto(Odontologo odontologo) {
        return COPAGO;
    }
}
