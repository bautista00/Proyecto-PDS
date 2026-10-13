package controller;

import dto.TurnoDatos;
import dto.TurnoEdicion;
import dto.TurnoRegistro;
import entity.EstadoTurno;
import entity.Turno;
import exception.DatoInvalidoException;
import service.TurnoService;
import service.ValidacionesClinica;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


//Controller para manejar las operaciones relacionadas con los turnos.
// Valida los campos que llegan de la vista, delega la lógica de negocio en el servicio
// y devuelve TurnoDatos para que la vista no reciba entidades del modelo (MVC).

public class TurnoController {

    private final TurnoService turnoService;

    public TurnoController(TurnoService turnoService) {
        this.turnoService = turnoService;
    }

    public TurnoDatos registrarTurno(TurnoRegistro datos) {
        if (datos == null) {
            throw new DatoInvalidoException("Los datos del turno no pueden ser nulos.");
        }
        validarCampos(datos.getFecha(), datos.getHora(), datos.getMotivoConsulta());
        return aDatos(turnoService.registrarTurno(datos));
    }

    public TurnoDatos buscarTurnoPorId(Long id) {
        return aDatos(turnoService.buscarPorId(id));
    }

    public List<TurnoDatos> listarTurnos() {
        return aDatos(turnoService.listarTodos());
    }

    public List<TurnoDatos> buscarTurnosPorRangoFechas(LocalDate desde, LocalDate hasta) {
        return aDatos(turnoService.buscarPorRangoFechas(desde, hasta));
    }

    public List<TurnoDatos> buscarTurnosPorOdontologoYPaciente(Long idOdontologo, Long idPaciente) {
        return aDatos(turnoService.buscarPorOdontologoYPaciente(idOdontologo, idPaciente));
    }

    public List<TurnoDatos> listarTurnosPorPaciente(Long idPaciente) {
        return aDatos(turnoService.listarPorPaciente(idPaciente));
    }

    public List<TurnoDatos> listarTurnosPorOdontologo(Long idOdontologo) {
        return aDatos(turnoService.listarPorOdontologo(idOdontologo));
    }

    public List<TurnoDatos> listarTurnosPorSecretaria(Long idSecretaria) {
        return aDatos(turnoService.listarPorSecretaria(idSecretaria));
    }

    public List<TurnoDatos> listarTurnosPorEstado(String estado) {
        return aDatos(turnoService.listarPorEstado(aEstado(estado)));
    }

    public List<String> listarEstados() {
        return Arrays.stream(EstadoTurno.values())
                .map(EstadoTurno::name)
                .collect(Collectors.toList());
    }

    public TurnoDatos actualizarTurno(TurnoEdicion datos) {
        if (datos == null) {
            throw new DatoInvalidoException("Los datos de edicion del turno no pueden ser nulos.");
        }
        validarCampos(datos.getFecha(), datos.getHora(), datos.getMotivoConsulta());
        aEstado(datos.getEstado()); // solo valida: el servicio hace la conversión
        return aDatos(turnoService.modificarTurno(datos));
    }

    public TurnoDatos cambiarEstadoTurno(Long idTurno, String nuevoEstado) {
        return aDatos(turnoService.cambiarEstado(idTurno, aEstado(nuevoEstado)));
    }

    public Double calcularMontoTurno(Long idTurno) {
        return turnoService.calcularMonto(idTurno);
    }

    public boolean eliminarTurno(Long idTurno) {
        return turnoService.eliminar(idTurno);
    }

    private void validarCampos(LocalDate fecha, LocalTime hora, String motivoConsulta) {
        if (fecha == null) {
            throw new DatoInvalidoException("La fecha no puede ser nula.");
        }
        if (hora == null) {
            throw new DatoInvalidoException("La hora no puede ser nula.");
        }
        ValidacionesClinica.validarMotivoConsultaNoVacio(motivoConsulta);
    }

    // Traduce el texto elegido en la vista al estado del modelo.
    private EstadoTurno aEstado(String estado) {
        ValidacionesClinica.validarTextoNoVacio(estado, "El estado del turno no puede ser nulo.");
        try {
            return EstadoTurno.valueOf(estado.trim());
        } catch (IllegalArgumentException excepcion) {
            throw new DatoInvalidoException("Estado de turno desconocido: " + estado + ".");
        }
    }

    private List<TurnoDatos> aDatos(List<Turno> turnos) {
        return turnos.stream().map(this::aDatos).collect(Collectors.toList());
    }

    private TurnoDatos aDatos(Turno turno) {
        TurnoDatos datos = new TurnoDatos();
        datos.setId(turno.getId());
        datos.setIdPaciente(turno.getIdPaciente());
        datos.setIdOdontologo(turno.getIdOdontologo());
        datos.setIdSecretaria(turno.getIdSecretaria());
        datos.setNombrePaciente(turno.getNombrePaciente());
        datos.setNombreOdontologo(turno.getNombreOdontologo());
        datos.setEspecialidad(turno.getEspecialidadOdontologo().getDescripcion());
        datos.setNombreSecretaria(turno.getNombreSecretaria());
        datos.setFecha(turno.getFecha());
        datos.setHora(turno.getHora());
        datos.setMotivoConsulta(turno.getMotivoConsulta());
        datos.setEstado(turno.getEstado().name());
        return datos;
    }
}
