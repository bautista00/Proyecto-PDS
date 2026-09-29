package service;

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
