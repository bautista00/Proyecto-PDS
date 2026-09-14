package controller;

import dto.PacienteEdicion;
import dto.PacienteRegistro;
import entity.Paciente;
import service.PacienteService;

import java.util.List;

public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    public Paciente registrarPaciente(PacienteRegistro datos) {
        return pacienteService.registrar(datos);
    }

    public PacienteRegistro buscarDatosPacientePorId(Long id) {
        return pacienteService.buscarDatosPorId(id);
    }

    public Paciente buscarPacientePorDni(Integer dni) {
        return pacienteService.buscarPorDni(dni);
    }

    public List<Paciente> listarPacientesOrdenadosPorApellido() {
        return pacienteService.listarOrdenadosPorApellido();
    }

    public Paciente actualizarPaciente(PacienteEdicion edicion) {
        return pacienteService.actualizar(edicion);
    }

    public boolean eliminarPaciente(Long id) {
        return pacienteService.eliminar(id);
    }
}
