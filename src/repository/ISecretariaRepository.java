package repository;

import entity.Secretaria;

public interface ISecretariaRepository extends IRepository<Secretaria> {

    Secretaria buscarPorDni(Integer dni);

    boolean existeDni(Integer dni);
}
