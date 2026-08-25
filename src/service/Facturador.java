package service;

import entity.Odontologo;
import entity.Paciente;
import exception.DatoInvalidoException;

public class Facturador {

    private static final Double COPAGO_OBRA_SOCIAL = 10000.0;

    public Double calcularMonto(Paciente paciente, Odontologo odontologo) {
        if (paciente == null) {
            throw new DatoInvalidoException("No se puede calcular el monto: el paciente es nulo.");
        }

        if (odontologo == null) {
            throw new DatoInvalidoException("No se puede calcular el monto: el odontologo es nulo.");
        }

        if (Boolean.TRUE.equals(paciente.getObraSocial())) {
            return COPAGO_OBRA_SOCIAL;
        }

        return odontologo.getTarifaBase();
    }
}