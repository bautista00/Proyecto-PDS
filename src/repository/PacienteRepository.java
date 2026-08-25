package repository;

import entity.Paciente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PacienteRepository implements IRepository<Paciente> {

    private Map<Long, Paciente> pacientes;

    public PacienteRepository() {
        this.pacientes = new HashMap<>();
    }

    @Override
    public void guardar(Paciente paciente) {
        pacientes.put(paciente.getId(), paciente);
    }

    @Override
    public Paciente buscarPorId(Long id) {
        return pacientes.get(id);
    }

    public Paciente buscarPorDni(Integer dni) {
        for (Paciente paciente : pacientes.values()) {
            if (paciente.getDni().equals(dni)) {
                return paciente;
            }
        }
        return null;
    }

    public boolean existeDni(Integer dni) {
        return buscarPorDni(dni) != null;
    }

    @Override
    public List<Paciente> listarTodos() {
        return new ArrayList<>(pacientes.values());
    }

    @Override
    public void actualizar(Paciente paciente) {
        pacientes.put(paciente.getId(), paciente);
    }

    @Override
    public void eliminar(Long id) {
        pacientes.remove(id);
    }
}