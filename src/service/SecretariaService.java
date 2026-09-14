package service;

import dto.SecretariaEdicion;
import dto.SecretariaRegistro;
import entity.Secretaria;

import java.util.List;

public interface SecretariaService {

    Secretaria registrar(SecretariaRegistro datos);

    Secretaria buscarPorId(Long id);

    Secretaria buscarPorDni(Integer dni);

    List<Secretaria> listarTodos();

    Secretaria actualizar(SecretariaEdicion edicion);

    boolean eliminar(Long id);
}
