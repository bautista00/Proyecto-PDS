package service;

// Cálculo del monto a cobrar de un turno.
// Es una de las partes de TurnoService; la implementa FacturacionTurnoService (ISP).
public interface FacturacionTurnos {

    Double calcularMonto(Long idTurno);
}
