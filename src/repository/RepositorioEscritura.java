package repository;

public interface RepositorioEscritura<T> {

    void guardar(T entidad);

    void actualizar(T entidad);

    void eliminar(Long id);
}
