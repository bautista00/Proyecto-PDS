package controller;

import dto.TurnoEdicion;
import dto.TurnoRegistro;
import entity.EstadoTurno;
import entity.Turno;
import service.TurnoService;

import java.time.LocalDate;
import java.util.List;


//Controller para manejar las operaciones relacionadas con los turnos.
// No se encarga de la lógica de negocio, sino que delega las operaciones al servicio correspondiente.

public class TurnoController {

    private final TurnoService turnoService;

    public TurnoController(TurnoService turnoService) {
        this.turnoService = turnoService;
    }

    public Turno registrarTurno(TurnoRegistro datos) {
        return turnoService.registrarTurno(datos);
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

    public Turno actualizarTurno(TurnoEdicion datos) {
        return turnoService.modificarTurno(datos);
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
