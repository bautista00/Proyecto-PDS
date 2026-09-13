package entity;

public enum CoberturaPaciente {
    PARTICULAR("Particular") {
        @Override
        public Double calcularMonto(Odontologo odontologo) {
            return odontologo.getTarifaBase();
        }
    },
    OBRA_SOCIAL("Obra Social") {
        @Override
        public Double calcularMonto(Odontologo odontologo) {
            return COPAGO_OBRA_SOCIAL;
        }
    };

    private static final Double COPAGO_OBRA_SOCIAL = 10000.0;

    private final String descripcion;

    CoberturaPaciente(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public abstract Double calcularMonto(Odontologo odontologo);

    @Override
    public String toString() {
        return descripcion;
    }
}
