package service;

import entity.EstadoTurno;
import entity.Turno;
import exception.DatoInvalidoException;
import repository.TurnoRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TurnoConsultaService {

    private static final Comparator<Turno> POR_FECHA_HORA =
            Comparator.comparing(Turno::getFecha).thenComparing(Turno::getHora);

    private final TurnoRepository turnoRepository;
    private final TurnoValidador turnoValidador;
    private final Facturador facturador;

    public TurnoConsultaService(TurnoRepository turnoRepository,
                                TurnoValidador turnoValidador,
                                Facturador facturador) {
        this.turnoRepository = turnoRepository;
        this.turnoValidador = turnoValidador;
        this.facturador = facturador;
    }

    public Turno buscarPorId(Long idTurno) {
        return turnoValidador.obtenerTurnoExistente(idTurno);
    }

    public List<Turno> listarTodos() {
        return turnoRepository.listarTodos();
    }

    public List<Turno> listarPorPaciente(Long idPaciente) {
        turnoValidador.obtenerPacienteExistente(idPaciente);
        return filtrarYOrdenar(turno -> turno.getIdPaciente().equals(idPaciente));
    }

    public List<Turno> listarPorOdontologo(Long idOdontologo) {
        turnoValidador.obtenerOdontologoExistente(idOdontologo);
        return filtrarYOrdenar(turno -> turno.getIdOdontologo().equals(idOdontologo));
    }

    public List<Turno> listarPorSecretaria(Long idSecretaria) {
        turnoValidador.obtenerSecretariaExistente(idSecretaria);
        return filtrarYOrdenar(turno -> turno.getIdSecretaria().equals(idSecretaria));
    }

    public List<Turno> listarPorEstado(EstadoTurno estado) {
        if (estado == null) {
            throw new DatoInvalidoException("El estado a filtrar no puede ser nulo.");
        }
        return filtrarYOrdenar(turno -> turno.getEstado() == estado);
    }

    public List<Turno> buscarPorRangoFechas(LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null) {
            throw new DatoInvalidoException("Las fechas 'desde' y 'hasta' no pueden ser nulas.");
        }
        if (desde.isAfter(hasta)) {
            throw new DatoInvalidoException("La fecha 'desde' no puede ser posterior a 'hasta'.");
        }
        return filtrarYOrdenar(turno -> !turno.getFecha().isBefore(desde) && !turno.getFecha().isAfter(hasta));
    }

    public List<Turno> buscarPorOdontologoYPaciente(Long idOdontologo, Long idPaciente) {
        turnoValidador.obtenerOdontologoExistente(idOdontologo);
        turnoValidador.obtenerPacienteExistente(idPaciente);
        return filtrarYOrdenar(turno -> turno.getIdOdontologo().equals(idOdontologo)
                && turno.getIdPaciente().equals(idPaciente));
    }

    public Double calcularMonto(Long idTurno) {
        Turno turno = turnoValidador.obtenerTurnoExistente(idTurno);
        return facturador.calcularMonto(turno.getPaciente(), turno.getOdontologo());
    }

    private List<Turno> filtrarYOrdenar(Predicate<Turno> criterio) {
        return turnoRepository.listarTodos().stream()
                .filter(criterio)
                .sorted(POR_FECHA_HORA)
                .collect(Collectors.toList());
    }
}
