package service;

// Calcula el monto de un turno a partir de su ID: lo busca con TurnoResolutor y delega en Facturador.
// Se separó de TurnoServiceImpl para que la facturación tenga su propia clase (SRP).
public class FacturacionTurnoService implements FacturacionTurnos {

    private final TurnoResolutor turnoResolutor;
    private final Facturador facturador;

    public FacturacionTurnoService(TurnoResolutor turnoResolutor, Facturador facturador) {
        this.turnoResolutor = turnoResolutor;
        this.facturador = facturador;
    }

    @Override
    public Double calcularMonto(Long idTurno) {
        return facturador.calcularMonto(turnoResolutor.obtenerTurno(idTurno));
    }
}
