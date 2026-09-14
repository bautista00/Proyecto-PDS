package service;

import entity.Turno;
import exception.DatoInvalidoException;


//Punto de entrada para el cobro de un turno: valida y delega el cálculo a Turno.calcularMonto(),
//que es el Experto en Información sobre su propio precio.
//La usa TurnoConsultaService.

public class Facturador {

    public Double calcularMonto(Turno turno) {
        if (turno == null) {
            throw new DatoInvalidoException("No se puede calcular el monto: el turno es nulo.");
        }
        return turno.calcularMonto();
    }
}
