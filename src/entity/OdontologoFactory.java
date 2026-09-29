package entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class OdontologoFactory {

    private final Map<String, CreadorOdontologo> creadores = new LinkedHashMap<>();

    public OdontologoFactory(Collection<? extends CreadorOdontologo> creadores) {
        if (creadores == null || creadores.isEmpty()) {
            throw new IllegalArgumentException("Debe registrarse al menos un creador de odontologos.");
        }
        for (CreadorOdontologo creador : creadores) {
            registrar(creador);
        }
    }

    public Odontologo crear(EspecialidadOdontologica especialidad,
                            String nombre,
                            String apellido,
                            Integer dni,
                            String matricula) {
        return buscarCreador(especialidad).crear(nombre, apellido, dni, matricula);
    }

    public Odontologo rehidratar(Long id,
                                 EspecialidadOdontologica especialidad,
                                 String nombre,
                                 String apellido,
                                 Integer dni,
                                 String matricula) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID del odontologo debe ser positivo.");
        }
        return buscarCreador(especialidad).rehidratar(id, nombre, apellido, dni, matricula);
    }

    public EspecialidadOdontologica buscarEspecialidad(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("La especialidad persistida no puede estar vacia.");
        }
        String normalizado = normalizar(valor);
        CreadorOdontologo creador = creadores.get(normalizado);
        if (creador != null) {
            return creador.getEspecialidad();
        }
        for (CreadorOdontologo candidato : creadores.values()) {
            if (normalizar(candidato.getEspecialidad().getDescripcion()).equals(normalizado)) {
                return candidato.getEspecialidad();
            }
        }
        throw new IllegalArgumentException("Especialidad desconocida: " + valor);
    }

    public List<EspecialidadOdontologica> listarEspecialidades() {
        List<EspecialidadOdontologica> especialidades = new ArrayList<>();
        for (CreadorOdontologo creador : creadores.values()) {
            especialidades.add(creador.getEspecialidad());
        }
        return Collections.unmodifiableList(especialidades);
    }

    private void registrar(CreadorOdontologo creador) {
        if (creador == null || creador.getEspecialidad() == null) {
            throw new IllegalArgumentException("No se puede registrar un creador sin especialidad.");
        }
        String codigo = normalizar(creador.getEspecialidad().getCodigo());
        if (creadores.putIfAbsent(codigo, creador) != null) {
            throw new IllegalArgumentException("Ya existe un creador para " + codigo + ".");
        }
    }

    private CreadorOdontologo buscarCreador(EspecialidadOdontologica especialidad) {
        if (especialidad == null) {
            throw new IllegalArgumentException("La especialidad no puede ser nula.");
        }
        CreadorOdontologo creador = creadores.get(normalizar(especialidad.getCodigo()));
        if (creador == null) {
            throw new IllegalArgumentException("Especialidad no soportada: " + especialidad);
        }
        return creador;
    }

    private String normalizar(String valor) {
        return valor.trim().toUpperCase(Locale.ROOT);
    }
}
