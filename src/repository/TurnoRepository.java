package repository;

import entity.EstadoTurno;
import entity.Turno;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TurnoRepository implements ITurnoRepository {

    private static final Comparator<Turno> POR_FECHA_HORA =
            Comparator.comparing(Turno::getFecha).thenComparing(Turno::getHora);

    private Map<Long, Turno> turnos;

    public TurnoRepository() {
        this.turnos = new HashMap<>();
    }

    @Override
    public void guardar(Turno turno) {
        turnos.put(turno.getId(), turno);
    }

    @Override
    public Turno buscarPorId(Long id) {
        return turnos.get(id);
    }

    @Override
    public List<Turno> listarTodos() {
        return new ArrayList<>(turnos.values());
    }

    @Override
    public void actualizar(Turno turno) {
        turnos.put(turno.getId(), turno);
    }

    @Override
    public void eliminar(Long id) {
        turnos.remove(id);
    }

    @Override
    public List<Turno> listarPorPaciente(Long idPaciente) {
        return filtrarYOrdenar(turno -> turno.getIdPaciente().equals(idPaciente));
    }

    @Override
    public List<Turno> listarPorOdontologo(Long idOdontologo) {
        return filtrarYOrdenar(turno -> turno.getIdOdontologo().equals(idOdontologo));
    }

    @Override
    public List<Turno> listarPorSecretaria(Long idSecretaria) {
        return filtrarYOrdenar(turno -> turno.getIdSecretaria().equals(idSecretaria));
    }

    @Override
    public List<Turno> listarPorEstado(EstadoTurno estado) {
        return filtrarYOrdenar(turno -> turno.getEstado() == estado);
    }

    @Override
    public List<Turno> buscarPorRangoFechas(LocalDate desde, LocalDate hasta) {
        return filtrarYOrdenar(turno -> !turno.getFecha().isBefore(desde)
                && !turno.getFecha().isAfter(hasta));
    }

    @Override
    public List<Turno> buscarPorOdontologoYPaciente(Long idOdontologo, Long idPaciente) {
        return filtrarYOrdenar(turno -> turno.getIdOdontologo().equals(idOdontologo)
                && turno.getIdPaciente().equals(idPaciente));
    }

    public boolean existeConflictoHorario(Long idOdontologo, LocalDate fecha, LocalTime hora) {
        for (Turno turno : turnos.values()) {
            if (turno.getIdOdontologo().equals(idOdontologo)
                    && turno.getFecha().equals(fecha)
                    && turno.getHora().equals(hora)
                    && turno.ocupaAgenda()) {
                return true;
            }
        }
        return false;
    }

    public boolean existeConflictoHorarioExcluyendoTurno(Long idTurno, Long idOdontologo, LocalDate fecha, LocalTime hora) {
        for (Turno turno : turnos.values()) {
            if (!turno.getId().equals(idTurno)
                    && turno.getIdOdontologo().equals(idOdontologo)
                    && turno.getFecha().equals(fecha)
                    && turno.getHora().equals(hora)
                    && turno.ocupaAgenda()) {
                return true;
            }
        }
        return false;
    }

    private List<Turno> filtrarYOrdenar(Predicate<Turno> criterio) {
        return turnos.values().stream()
                .filter(criterio)
                .sorted(POR_FECHA_HORA)
                .collect(Collectors.toList());
    }

}
