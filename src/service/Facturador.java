package service;

import entity.Odontologo;
import entity.Paciente;
import exception.DatoInvalidoException;

public class Facturador {

    public Double calcularMonto(Paciente paciente, Odontologo odontologo) {
        if (paciente == null) {
            throw new DatoInvalidoException("No se puede calcular el monto: el paciente es nulo.");
        }

        if (odontologo == null) {
            throw new DatoInvalidoException("No se puede calcular el monto: el odontologo es nulo.");
        }

        return paciente.getCobertura().calcularMonto(odontologo);
    }
}