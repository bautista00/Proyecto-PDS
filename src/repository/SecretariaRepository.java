package repository;

import entity.Secretaria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecretariaRepository implements IRepository<Secretaria> {

    private Map<Long, Secretaria> secretarias;

    public SecretariaRepository() {
        this.secretarias = new HashMap<>();
    }

    @Override
    public void guardar(Secretaria secretaria) {
        secretarias.put(secretaria.getId(), secretaria);
    }

    @Override
    public Secretaria buscarPorId(Long id) {
        return secretarias.get(id);
    }

    public Secretaria buscarPorDni(Integer dni) {
        for (Secretaria secretaria : secretarias.values()) {
            if (secretaria.getDni().equals(dni)) {
                return secretaria;
            }
        }
        return null;
    }

    public boolean existeDni(Integer dni) {
        return buscarPorDni(dni) != null;
    }

    @Override
    public List<Secretaria> listarTodos() {
        return new ArrayList<>(secretarias.values());
    }

    @Override
    public void actualizar(Secretaria secretaria) {
        secretarias.put(secretaria.getId(), secretaria);
    }

    @Override
    public void eliminar(Long id) {
        secretarias.remove(id);
    }
}