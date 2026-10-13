package entity;

// Cobertura de obra social: el paciente paga solo un copago fijo, sin importar el odontólogo.
// Es otra implementación de CoberturaPaciente; una cobertura nueva sería otra clase (OCP).
public final class CoberturaObraSocial implements CoberturaPaciente {

    private static final Double COPAGO = 10000.0;

    @Override
    public String getCodigo() {
        return "OBRA_SOCIAL";
    }

    @Override
    public String getDescripcion() {
        return "Obra Social";
    }

    @Override
    public Double calcularMonto(Odontologo odontologo) {
        return COPAGO;
    }

    @Override
    public String toString() {
        return getDescripcion();
    }
}
