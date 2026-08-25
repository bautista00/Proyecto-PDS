package repository;

import entity.Odontologo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OdontologoRepository implements IRepository<Odontologo> {

    private Map<Long, Odontologo> odontologos;

    public OdontologoRepository() {
        this.odontologos = new HashMap<>();
    }

    @Override
    public void guardar(Odontologo odontologo) {
        odontologos.put(odontologo.getId(), odontologo);
    }

    @Override
    public Odontologo buscarPorId(Long id) {
        return odontologos.get(id);
    }

    public Odontologo buscarPorMatricula(String matricula) {
        for (Odontologo odontologo : odontologos.values()) {
            if (odontologo.getMatricula().equalsIgnoreCase(matricula)) {
                return odontologo;
            }
        }
        return null;
    }

    public boolean existeMatricula(String matricula) {
        return buscarPorMatricula(matricula) != null;
    }

    @Override
    public List<Odontologo> listarTodos() {
        return new ArrayList<>(odontologos.values());
    }

    @Override
    public void actualizar(Odontologo odontologo) {
        odontologos.put(odontologo.getId(), odontologo);
    }

    @Override
    public void eliminar(Long id) {
        odontologos.remove(id);
    }
}