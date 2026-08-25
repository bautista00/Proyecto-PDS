package controller;

import entity.EstadoTurno;
import entity.Turno;
import service.TurnoServiceImpl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TurnoController {

    private TurnoServiceImpl turnoService;

    public TurnoController(TurnoServiceImpl turnoService) {
        this.turnoService = turnoService;
    }

    public Turno registrarTurno(Long idPaciente,
                                Long idOdontologo,
                                Long idSecretaria,
                                LocalDate fecha,
                                LocalTime hora,
                                String motivoConsulta) {

        return turnoService.registrarTurno(
                idPaciente,
                idOdontologo,
                idSecretaria,
                fecha,
                hora,
                motivoConsulta
        );
    }

    public Turno buscarTurnoPorId(Long id) {
        return turnoService.buscarPorId(id);
    }

    public List<Turno> listarTurnos() {
        return turnoService.listarTodos();
    }

    public List<Turno> buscarTurnosPorRangoFechas(LocalDate desde, LocalDate hasta) {
        return turnoService.buscarPorRangoFechas(desde, hasta);
    }

    public List<Turno> buscarTurnosPorOdontologoYPaciente(Long idOdontologo, Long idPaciente) {
        return turnoService.buscarPorOdontologoYPaciente(idOdontologo, idPaciente);
    }

    public List<Turno> listarTurnosPorPaciente(Long idPaciente) {
        return turnoService.listarPorPaciente(idPaciente);
    }

    public List<Turno> listarTurnosPorOdontologo(Long idOdontologo) {
        return turnoService.listarPorOdontologo(idOdontologo);
    }

    public List<Turno> listarTurnosPorSecretaria(Long idSecretaria) {
        return turnoService.listarPorSecretaria(idSecretaria);
    }

    public List<Turno> listarTurnosPorEstado(EstadoTurno estado) {
        return turnoService.listarPorEstado(estado);
    }

    public Turno actualizarTurno(Long idTurno,
                                 Long idOdontologo,
                                 Long idSecretaria,
                                 LocalDate fecha,
                                 LocalTime hora,
                                 String motivoConsulta,
                                 EstadoTurno estado) {

        return turnoService.modificarTurno(
                idTurno,
                idOdontologo,
                idSecretaria,
                fecha,
                hora,
                motivoConsulta,
                estado
        );
    }

    public Turno cambiarEstadoTurno(Long idTurno, EstadoTurno nuevoEstado) {
        return turnoService.cambiarEstado(idTurno, nuevoEstado);
    }

    public Double calcularMontoTurno(Long idTurno) {
        return turnoService.calcularMonto(idTurno);
    }

    public boolean eliminarTurno(Long idTurno) {
        return turnoService.eliminar(idTurno);
    }
}