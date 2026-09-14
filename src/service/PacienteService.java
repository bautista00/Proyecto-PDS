package service;

import dto.PacienteEdicion;
import dto.PacienteRegistro;
import entity.Paciente;

import java.util.List;

public interface PacienteService {

    Paciente registrar(PacienteRegistro datos);

    PacienteRegistro buscarDatosPorId(Long id);

    Paciente buscarPorDni(Integer dni);

    List<Paciente> listarOrdenadosPorApellido();

    Paciente actualizar(PacienteEdicion edicion);

    boolean eliminar(Long id);
}
