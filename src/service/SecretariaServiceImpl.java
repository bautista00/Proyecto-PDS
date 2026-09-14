package service;

import dto.SecretariaEdicion;
import dto.SecretariaRegistro;
import entity.Secretaria;
import exception.DatoInvalidoException;
import repository.ISecretariaRepository;

import java.util.List;

public class SecretariaServiceImpl implements SecretariaService {

    private final ISecretariaRepository secretariaRepository;

    public SecretariaServiceImpl(ISecretariaRepository secretariaRepository) {
        this.secretariaRepository = secretariaRepository;
    }

    @Override
    public Secretaria registrar(SecretariaRegistro datos) {
        validarDatos(datos);
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
        if (edicion == null) {
            throw new DatoInvalidoException("Los datos de edicion de la secretaria no pueden ser nulos.");
        }
        SecretariaRegistro datos = edicion.getDatos();
        validarDatos(datos);
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

    private void validarDatos(SecretariaRegistro datos) {
        if (datos == null) {
            throw new DatoInvalidoException("Los datos de la secretaria no pueden ser nulos.");
        }
        ValidacionesClinica.validarNombreNoVacio(datos.getNombre());
        ValidacionesClinica.validarNombreSoloLetras(datos.getNombre());
        ValidacionesClinica.validarApellidoNoVacio(datos.getApellido());
        ValidacionesClinica.validarApellidoSoloLetras(datos.getApellido());
        ValidacionesClinica.validarDniPositivo(datos.getDni());
    }
}
