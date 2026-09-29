package service;

import entity.EstadoTurno;
import entity.Turno;
import exception.DatoInvalidoException;
import repository.ConsultaTurnosRepository;

import java.time.LocalDate;
import java.util.List;

// Servicio dedicado a consultar turnos por los distintos criterios disponibles.
// La facturacion se resuelve por separado en FacturacionTurnoService.


public class TurnoConsultaService implements ConsultaTurnos {

    private final ConsultaTurnosRepository turnoRepository;
    private final TurnoResolutor turnoResolutor;

    public TurnoConsultaService(ConsultaTurnosRepository turnoRepository,
                                TurnoResolutor turnoResolutor) {
        this.turnoRepository = turnoRepository;
        this.turnoResolutor = turnoResolutor;
    }

    public Turno buscarPorId(Long idTurno) {
        return turnoResolutor.obtenerTurno(idTurno);
    }

    public List<Turno> listarTodos() {
        return turnoRepository.listarTodos();
    }

    public List<Turno> listarPorPaciente(Long idPaciente) {
        turnoResolutor.obtenerPaciente(idPaciente);
        return turnoRepository.listarPorPaciente(idPaciente);
    }

    public List<Turno> listarPorOdontologo(Long idOdontologo) {
        turnoResolutor.obtenerOdontologo(idOdontologo);
        return turnoRepository.listarPorOdontologo(idOdontologo);
    }

    public List<Turno> listarPorSecretaria(Long idSecretaria) {
        turnoResolutor.obtenerSecretaria(idSecretaria);
        return turnoRepository.listarPorSecretaria(idSecretaria);
    }

    public List<Turno> listarPorEstado(EstadoTurno estado) {
        if (estado == null) {
            throw new DatoInvalidoException("El estado a filtrar no puede ser nulo.");
        }
        return turnoRepository.listarPorEstado(estado);
    }

    public List<Turno> buscarPorRangoFechas(LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null) {
            throw new DatoInvalidoException("Las fechas 'desde' y 'hasta' no pueden ser nulas.");
        }
        if (desde.isAfter(hasta)) {
            throw new DatoInvalidoException("La fecha 'desde' no puede ser posterior a 'hasta'.");
        }
        return turnoRepository.buscarPorRangoFechas(desde, hasta);
    }

    public List<Turno> buscarPorOdontologoYPaciente(Long idOdontologo, Long idPaciente) {
        turnoResolutor.obtenerOdontologo(idOdontologo);
        turnoResolutor.obtenerPaciente(idPaciente);
        return turnoRepository.buscarPorOdontologoYPaciente(idOdontologo, idPaciente);
    }

}
