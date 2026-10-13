package repository;

import java.util.List;

// Operaciones de solo lectura de un repositorio: buscar por ID y listar todos.
// Se separó de la escritura para que cada clase dependa solo de lo que usa (ISP).
public interface RepositorioLectura<T> extends BuscadorPorId<T> {

    List<T> listarTodos();
}
