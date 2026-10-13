package repository;

// Operaciones de escritura de un repositorio: guardar, actualizar y eliminar.
// Se separó de la lectura; la usa, por ejemplo, la persistencia al cargar los datos (ISP).
public interface RepositorioEscritura<T> {

    void guardar(T entidad);

    void actualizar(T entidad);

    void eliminar(Long id);
}
