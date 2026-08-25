package service;

import entity.EstadoTurno;
import entity.Turno;
import repository.OdontologoRepository;
import repository.PacienteRepository;
import repository.SecretariaRepository;
import repository.TurnoRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TurnoServiceImpl implements IService<Turno> {

    private final TurnoAgendaService turnoAgendaService;
    private final TurnoConsultaService turnoConsultaService;

    public TurnoServiceImpl(TurnoRepository turnoRepository,
                           PacienteRepository pacienteRepository,
                           OdontologoRepository odontologoRepository,
                           SecretariaRepository secretariaRepository,
                           Facturador facturador) {
       TurnoValidador turnoValidador = new TurnoValidador();
       TurnoRelacionador turnoRelacionador = new TurnoRelacionador();
       this.turnoAgendaService = new TurnoAgendaService(
               turnoRepository,
               pacienteRepository,
               odontologoRepository,
               secretariaRepository,
               turnoValidador,
               turnoRelacionador);
       this.turnoConsultaService = new TurnoConsultaService(
               turnoRepository,
               pacienteRepository,
               odontologoRepository,
               secretariaRepository,
               turnoValidador,
               facturador);
    }

    @Override
    public Turno registrar(Turno turno) {
       return turnoAgendaService.registrar(turno);
    }

    public Turno registrarTurno(Long idPaciente,
                               Long idOdontologo,
                               Long idSecretaria,
                               LocalDate fecha,
                               LocalTime hora,
                               String motivoConsulta) {
       return turnoAgendaService.registrarTurno(
               idPaciente,
               idOdontologo,
               idSecretaria,
               fecha,
               hora,
               motivoConsulta);
    }

    @Override
    public Turno buscarPorId(Long id) {
       return turnoConsultaService.buscarPorId(id);
    }

    @Override
    public List<Turno> listarTodos() {
       return turnoConsultaService.listarTodos();
    }

    public List<Turno> listarPorPaciente(Long idPaciente) {
       return turnoConsultaService.listarPorPaciente(idPaciente);
    }

    public List<Turno> listarPorOdontologo(Long idOdontologo) {
       return turnoConsultaService.listarPorOdontologo(idOdontologo);
    }

    public List<Turno> listarPorSecretaria(Long idSecretaria) {
       return turnoConsultaService.listarPorSecretaria(idSecretaria);
    }

    public List<Turno> listarPorEstado(EstadoTurno estado) {
       return turnoConsultaService.listarPorEstado(estado);
    }

    public List<Turno> buscarPorRangoFechas(LocalDate desde, LocalDate hasta) {
       return turnoConsultaService.buscarPorRangoFechas(desde, hasta);
    }

    public List<Turno> buscarPorOdontologoYPaciente(Long idOdontologo, Long idPaciente) {
       return turnoConsultaService.buscarPorOdontologoYPaciente(idOdontologo, idPaciente);
    }

    @Override
    public Turno actualizar(Turno turno) {
       return turnoAgendaService.actualizar(turno);
    }

    public Turno modificarTurno(Long idTurno,
                               Long idOdontologo,
                               Long idSecretaria,
                               LocalDate fecha,
                               LocalTime hora,
                               String motivoConsulta,
                               EstadoTurno estado) {
       return turnoAgendaService.modificarTurno(
               idTurno,
               idOdontologo,
               idSecretaria,
               fecha,
               hora,
               motivoConsulta,
               estado);
    }

    public Turno cambiarEstado(Long idTurno, EstadoTurno nuevoEstado) {
       return turnoAgendaService.cambiarEstado(idTurno, nuevoEstado);
    }

    public Double calcularMonto(Long idTurno) {
       return turnoConsultaService.calcularMonto(idTurno);
    }

    @Override
    public boolean eliminar(Long id) {
       return turnoAgendaService.eliminar(id);
    }
}
