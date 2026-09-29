package repository;

import java.util.List;

public interface RepositorioLectura<T> extends BuscadorPorId<T> {

    List<T> listarTodos();
}
