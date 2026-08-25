package service;

import java.util.List;

public interface IService<T> {

    T registrar(T entidad);

    T buscarPorId(Long id);

    List<T> listarTodos();

    T actualizar(T entidad);

    boolean eliminar(Long id);
}