package controller;

import dto.SecretariaEdicion;
import dto.SecretariaRegistro;
import entity.Secretaria;
import service.SecretariaService;

import java.util.List;


//Controller para manejar las operaciones relacionadas con las secretarias.
// No se encarga de la lógica de negocio, sino que delega las operaciones al servicio correspondiente.

public class SecretariaController {

    private final SecretariaService secretariaService;

    public SecretariaController(SecretariaService secretariaService) {
        this.secretariaService = secretariaService;
    }

    public Secretaria registrarSecretaria(SecretariaRegistro datos) {
        return secretariaService.registrar(datos);
    }

    public Secretaria buscarSecretariaPorId(Long id) {
        return secretariaService.buscarPorId(id);
    }

    public Secretaria buscarSecretariaPorDni(Integer dni) {
        return secretariaService.buscarPorDni(dni);
    }

    public List<Secretaria> listarSecretarias() {
        return secretariaService.listarTodos();
    }

    public Secretaria actualizarSecretaria(SecretariaEdicion edicion) {
        return secretariaService.actualizar(edicion);
    }

    public boolean eliminarSecretaria(Long id) {
        return secretariaService.eliminar(id);
    }
}
