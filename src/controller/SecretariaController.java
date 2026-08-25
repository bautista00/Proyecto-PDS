package controller;

import entity.Secretaria;
import service.SecretariaServiceImpl;

import java.util.List;

public class SecretariaController {

    private SecretariaServiceImpl secretariaService;

    public SecretariaController(SecretariaServiceImpl secretariaService) {
        this.secretariaService = secretariaService;
    }

    public Secretaria registrarSecretaria(String nombre, String apellido, Integer dni) {
        Secretaria secretaria = new Secretaria(nombre, apellido, dni);
        return secretariaService.registrar(secretaria);
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

    public Secretaria actualizarSecretaria(Long id, String nombre, String apellido, Integer dni) {
        Secretaria secretariaExistente = secretariaService.buscarPorId(id);

        secretariaExistente.setNombre(nombre);
        secretariaExistente.setApellido(apellido);
        secretariaExistente.setDni(dni);

        return secretariaService.actualizar(secretariaExistente);
    }

    public boolean eliminarSecretaria(Long id) {
        return secretariaService.eliminar(id);
    }
}