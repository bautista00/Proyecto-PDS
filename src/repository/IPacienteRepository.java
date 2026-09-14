package repository;

import entity.Paciente;

public interface IPacienteRepository extends IRepository<Paciente> {

    Paciente buscarPorDni(Integer dni);

    boolean existeDni(Integer dni);
}
