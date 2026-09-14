package repository;

import entity.Odontologo;

public interface IOdontologoRepository extends IRepository<Odontologo> {

    Odontologo buscarPorMatricula(String matricula);

    boolean existeMatricula(String matricula);
}
