package entity;

// Estrategia concreta (Strategy): el paciente paga la tarifa base completa del odontólogo.
// Implementa CoberturaPaciente en lugar del antiguo booleano de obra social (OCP).
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
