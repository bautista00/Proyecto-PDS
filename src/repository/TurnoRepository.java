package repository;

import entity.Turno;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TurnoRepository implements IRepository<Turno> {

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

    public boolean existeConflictoHorario(Long idOdontologo, LocalDate fecha, LocalTime hora) {
        for (Turno turno : turnos.values()) {
            if (turno.getOdontologo().getId().equals(idOdontologo)
                    && turno.getFecha().equals(fecha)
                    && turno.getHora().equals(hora)) {
                return true;
            }
        }
        return false;
    }

    public boolean existeConflictoHorarioExcluyendoTurno(Long idTurno, Long idOdontologo, LocalDate fecha, LocalTime hora) {
        for (Turno turno : turnos.values()) {
            if (!turno.getId().equals(idTurno)
                    && turno.getOdontologo().getId().equals(idOdontologo)
                    && turno.getFecha().equals(fecha)
                    && turno.getHora().equals(hora)) {
                return true;
            }
        }
        return false;
    }

}