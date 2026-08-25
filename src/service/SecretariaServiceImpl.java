package service;

import entity.Secretaria;
import exception.DatoInvalidoException;
import repository.SecretariaRepository;

import java.util.List;

public class SecretariaServiceImpl implements IService<Secretaria> {

    private SecretariaRepository secretariaRepository;

    public SecretariaServiceImpl(SecretariaRepository secretariaRepository) {
        this.secretariaRepository = secretariaRepository;
    }

    @Override
    public Secretaria registrar(Secretaria secretaria) {
        validarSecretaria(secretaria);

        if (secretariaRepository.existeDni(secretaria.getDni())) {
            throw new DatoInvalidoException("Ya existe una secretaria con el DNI " + secretaria.getDni() + ".");
        }

        secretariaRepository.guardar(secretaria);
        return secretaria;
    }

    @Override
    public Secretaria buscarPorId(Long id) {
        ValidacionesClinica.validarIdSecretariaPositivo(id);

        Secretaria secretaria = secretariaRepository.buscarPorId(id);
        if (secretaria == null) {
            throw new DatoInvalidoException("No existe una secretaria con ID " + id + ".");
        }

        return secretaria;
    }

    public Secretaria buscarPorDni(Integer dni) {
        ValidacionesClinica.validarDniPositivo(dni);

        Secretaria secretaria = secretariaRepository.buscarPorDni(dni);
        if (secretaria == null) {
            throw new DatoInvalidoException("No existe una secretaria con DNI " + dni + ".");
        }

        return secretaria;
    }

    @Override
    public List<Secretaria> listarTodos() {
        return secretariaRepository.listarTodos();
    }

    @Override
    public Secretaria actualizar(Secretaria secretaria) {
        validarSecretaria(secretaria);

        Secretaria secretariaExistente = secretariaRepository.buscarPorId(secretaria.getId());
        if (secretariaExistente == null) {
            throw new DatoInvalidoException("No existe una secretaria con ID " + secretaria.getId() + ".");
        }

        Secretaria secretariaConMismoDni = secretariaRepository.buscarPorDni(secretaria.getDni());
        if (secretariaConMismoDni != null && !secretariaConMismoDni.getId().equals(secretaria.getId())) {
            throw new DatoInvalidoException("Ya existe otra secretaria con el DNI " + secretaria.getDni() + ".");
        }

        secretariaRepository.actualizar(secretaria);
        return secretaria;
    }

    @Override
    public boolean eliminar(Long id) {
        ValidacionesClinica.validarIdSecretariaPositivo(id);

        if (secretariaRepository.buscarPorId(id) == null) {
            throw new DatoInvalidoException("No existe una secretaria con ID " + id + ".");
        }

        secretariaRepository.eliminar(id);
        return true;
    }

    private void validarSecretaria(Secretaria secretaria) {
        if (secretaria == null) {
            throw new DatoInvalidoException("La secretaria no puede ser nula.");
        }
        ValidacionesClinica.validarNombreNoVacio(secretaria.getNombre());
        ValidacionesClinica.validarNombreSoloLetras(secretaria.getNombre());
        ValidacionesClinica.validarApellidoNoVacio(secretaria.getApellido());
        ValidacionesClinica.validarApellidoSoloLetras(secretaria.getApellido());
        ValidacionesClinica.validarDniPositivo(secretaria.getDni());
    }
}
