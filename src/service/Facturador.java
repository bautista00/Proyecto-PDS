package service;

import entity.Turno;
import exception.DatoInvalidoException;

public class Facturador {

    public Double calcularMonto(Turno turno) {
        if (turno == null) {
            throw new DatoInvalidoException("No se puede calcular el monto: el turno es nulo.");
        }
        return turno.calcularMonto();
    }
}
