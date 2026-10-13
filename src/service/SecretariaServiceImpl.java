package service;

import dto.SecretariaEdicion;
import dto.SecretariaRegistro;
import entity.Secretaria;
import exception.DatoInvalidoException;
import repository.ISecretariaRepository;

import java.util.List;


// Implementación del servicio de secretarias, que se encarga de la lógica de negocio relacionada con las secretarias.
// Tiene la responsabilidad de registrar, buscar, listar, actualizar y eliminar secretarias, aplicando
// las reglas de negocio correspondientes (los campos ya llegan validados por SecretariaController).
// Utiliza el repositorio de secretarias para persistir y recuperar los datos de las secretarias.


public class SecretariaServiceImpl implements SecretariaService {

    private final ISecretariaRepository secretariaRepository;

    public SecretariaServiceImpl(ISecretariaRepository secretariaRepository) {
        this.secretariaRepository = secretariaRepository;
    }

    @Override
    public Secretaria registrar(SecretariaRegistro datos) {
        if (secretariaRepository.existeDni(datos.getDni())) {
            throw new DatoInvalidoException("Ya existe una secretaria con el DNI " + datos.getDni() + ".");
        }
        Secretaria secretaria = new Secretaria(datos.getNombre(), datos.getApellido(), datos.getDni());
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

    @Override
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
    public Secretaria actualizar(SecretariaEdicion edicion) {
        SecretariaRegistro datos = edicion.getDatos();
        Secretaria secretaria = buscarPorId(edicion.getIdSecretaria());
        Secretaria mismoDni = secretariaRepository.buscarPorDni(datos.getDni());
        if (mismoDni != null && !mismoDni.getId().equals(secretaria.getId())) {
            throw new DatoInvalidoException("Ya existe otra secretaria con el DNI " + datos.getDni() + ".");
        }

        secretaria.actualizarDatos(datos.getNombre(), datos.getApellido(), datos.getDni());
        secretariaRepository.actualizar(secretaria);
        return secretaria;
    }

    @Override
    public boolean eliminar(Long id) {
        Secretaria secretaria = buscarPorId(id);
        if (secretaria.tieneTurnos()) {
            throw new DatoInvalidoException(
                    "No se puede eliminar la secretaria porque posee turnos asociados. " +
                    "El historial clinico debe conservarse.");
        }
        secretariaRepository.eliminar(id);
        return true;
    }
}
