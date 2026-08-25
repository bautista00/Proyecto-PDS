package service;

import entity.EstadoTurno;
import entity.Turno;
import exception.DatoInvalidoException;
import repository.OdontologoRepository;
import repository.PacienteRepository;
import repository.SecretariaRepository;
import repository.TurnoRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TurnoConsultaService {

    private static final Comparator<Turno> POR_FECHA_HORA =
            Comparator.comparing(Turno::getFecha).thenComparing(Turno::getHora);

    private final TurnoRepository turnoRepository;
    private final PacienteRepository pacienteRepository;
    private final OdontologoRepository odontologoRepository;
    private final SecretariaRepository secretariaRepository;
    private final TurnoValidador turnoValidador;
    private final Facturador facturador;

    public TurnoConsultaService(TurnoRepository turnoRepository,
                                PacienteRepository pacienteRepository,
                                OdontologoRepository odontologoRepository,
                                SecretariaRepository secretariaRepository,
                                TurnoValidador turnoValidador,
                                Facturador facturador) {
        this.turnoRepository = turnoRepository;
        this.pacienteRepository = pacienteRepository;
        this.odontologoRepository = odontologoRepository;
        this.secretariaRepository = secretariaRepository;
        this.turnoValidador = turnoValidador;
        this.facturador = facturador;
    }

    public Turno buscarPorId(Long idTurno) {
        return turnoValidador.obtenerTurnoExistente(idTurno, turnoRepository);
    }

    public List<Turno> listarTodos() {
        return turnoRepository.listarTodos();
    }

    public List<Turno> listarPorPaciente(Long idPaciente) {
        turnoValidador.obtenerPacienteExistente(idPaciente, pacienteRepository);
        return filtrarYOrdenar(t -> t.getPaciente().getId().equals(idPaciente));
    }

    public List<Turno> listarPorOdontologo(Long idOdontologo) {
        turnoValidador.obtenerOdontologoExistente(idOdontologo, odontologoRepository);
        return filtrarYOrdenar(t -> t.getOdontologo().getId().equals(idOdontologo));
    }

    public List<Turno> listarPorSecretaria(Long idSecretaria) {
        turnoValidador.obtenerSecretariaExistente(idSecretaria, secretariaRepository);
        return filtrarYOrdenar(t -> t.getSecretaria().getId().equals(idSecretaria));
    }

    public List<Turno> listarPorEstado(EstadoTurno estado) {
        if (estado == null) {
            throw new DatoInvalidoException("El estado a filtrar no puede ser nulo.");
        }
        return filtrarYOrdenar(t -> t.getEstado() == estado);
    }

    public List<Turno> buscarPorRangoFechas(LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null) {
            throw new DatoInvalidoException("Las fechas 'desde' y 'hasta' no pueden ser nulas.");
        }
        if (desde.isAfter(hasta)) {
            throw new DatoInvalidoException("La fecha 'desde' no puede ser posterior a 'hasta'.");
        }
        return filtrarYOrdenar(t -> !t.getFecha().isBefore(desde) && !t.getFecha().isAfter(hasta));
    }

    public List<Turno> buscarPorOdontologoYPaciente(Long idOdontologo, Long idPaciente) {
        turnoValidador.obtenerOdontologoExistente(idOdontologo, odontologoRepository);
        turnoValidador.obtenerPacienteExistente(idPaciente, pacienteRepository);
        return filtrarYOrdenar(t -> t.getOdontologo().getId().equals(idOdontologo)
                && t.getPaciente().getId().equals(idPaciente));
    }

    public Double calcularMonto(Long idTurno) {
        Turno turno = turnoValidador.obtenerTurnoExistente(idTurno, turnoRepository);
        return facturador.calcularMonto(turno.getPaciente(), turno.getOdontologo());
    }

    private List<Turno> filtrarYOrdenar(java.util.function.Predicate<Turno> criterio) {
        return turnoRepository.listarTodos().stream()
                .filter(criterio)
                .sorted(POR_FECHA_HORA)
                .collect(Collectors.toList());
    }
}
