package repository;

import java.util.List;

public interface IRepository<T> {

    void guardar(T entidad);

    T buscarPorId(Long id);

    List<T> listarTodos();

    void actualizar(T entidad);

    void eliminar(Long id);
}