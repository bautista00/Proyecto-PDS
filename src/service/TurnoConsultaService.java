package service;

import entity.EstadoTurno;
import entity.Turno;
import exception.DatoInvalidoException;
import repository.ITurnoRepository;

import java.time.LocalDate;
import java.util.List;

// Clase que se encarga de consultar turnos y sus detalles, incluyendo la búsqueda por diferentes criterios y 
// el cálculo del monto de un turno.    
// Utiliza TurnoResolutor para obtener los detalles de los turnos, pacientes, odontólogos y secretarias.
// Utiliza Facturador para calcular el monto de un turno.


public class TurnoConsultaService {

    private final ITurnoRepository turnoRepository;
    private final TurnoResolutor turnoResolutor;
    private final Facturador facturador;

    public TurnoConsultaService(ITurnoRepository turnoRepository,
                                TurnoResolutor turnoResolutor,
                                Facturador facturador) {
        this.turnoRepository = turnoRepository;
        this.turnoResolutor = turnoResolutor;
        this.facturador = facturador;
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

    public Double calcularMonto(Long idTurno) {
        return facturador.calcularMonto(turnoResolutor.obtenerTurno(idTurno));
    }
}
