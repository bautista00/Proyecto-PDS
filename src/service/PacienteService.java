package service;

import dto.PacienteEdicion;
import dto.PacienteRegistro;
import entity.CoberturaPaciente;
import entity.Paciente;

import java.util.List;

public interface PacienteService {

    Paciente registrar(PacienteRegistro datos);

    Paciente buscarPorId(Long id);

    Paciente buscarPorDni(Integer dni);

    List<Paciente> listarOrdenadosPorApellido();

    List<CoberturaPaciente> listarCoberturas();

    Paciente actualizar(PacienteEdicion edicion);

    boolean eliminar(Long id);
}
