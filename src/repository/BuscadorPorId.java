package repository;

// Interfaz mínima para buscar una entidad por su ID.
// La usan TurnoResolutor y PersistenciaTurno, que no necesitan guardar ni listar (ISP).
public interface BuscadorPorId<T> {

    T buscarPorId(Long id);
}
