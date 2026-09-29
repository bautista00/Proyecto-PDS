package entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class CatalogoCoberturas {

    private final Map<String, CoberturaPaciente> coberturasPorCodigo = new LinkedHashMap<>();

    public CatalogoCoberturas(Collection<? extends CoberturaPaciente> coberturas) {
        if (coberturas == null || coberturas.isEmpty()) {
            throw new IllegalArgumentException("Debe registrarse al menos una cobertura.");
        }
        for (CoberturaPaciente cobertura : coberturas) {
            registrar(cobertura);
        }
    }

    public List<CoberturaPaciente> listar() {
        return Collections.unmodifiableList(new ArrayList<>(coberturasPorCodigo.values()));
    }

    public CoberturaPaciente buscar(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("La cobertura persistida no puede estar vacia.");
        }
        String normalizado = normalizar(valor);
        CoberturaPaciente cobertura = coberturasPorCodigo.get(normalizado);
        if (cobertura != null) {
            return cobertura;
        }
        for (CoberturaPaciente candidata : coberturasPorCodigo.values()) {
            if (normalizar(candidata.getDescripcion()).equals(normalizado)) {
                return candidata;
            }
        }
        throw new IllegalArgumentException("Cobertura desconocida: " + valor);
    }

    private void registrar(CoberturaPaciente cobertura) {
        if (cobertura == null) {
            throw new IllegalArgumentException("No se puede registrar una cobertura nula.");
        }
        String codigo = normalizar(cobertura.getCodigo());
        if (coberturasPorCodigo.putIfAbsent(codigo, cobertura) != null) {
            throw new IllegalArgumentException("Ya existe una cobertura con codigo " + codigo + ".");
        }
    }

    private String normalizar(String valor) {
        return valor.trim().toUpperCase(Locale.ROOT);
    }
}
