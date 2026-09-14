package service;

import dto.OdontologoEdicion;
import dto.OdontologoRegistro;
import entity.Odontologo;

import java.util.List;

public interface OdontologoService {

    Odontologo registrar(OdontologoRegistro datos);

    Odontologo buscarPorId(Long id);

    Odontologo buscarPorMatricula(String matricula);

    List<Odontologo> listarTodos();

    Odontologo actualizar(OdontologoEdicion edicion);

    boolean eliminar(Long id);
}
