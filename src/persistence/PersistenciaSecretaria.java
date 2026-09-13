package persistence;

import entity.Secretaria;
import repository.SecretariaRepository;

import java.util.ArrayList;
import java.util.List;

final class PersistenciaSecretaria {

    private static final String RUTA = "datos/secretarias.txt";
    private static final String DESCRIPCION = "secretarias";

    private final ArchivoTexto archivoTexto;

    PersistenciaSecretaria(ArchivoTexto archivoTexto) {
        this.archivoTexto = archivoTexto;
    }

    void guardar(SecretariaRepository repository) {
        List<String> lineas = new ArrayList<>();
        for (Secretaria secretaria : repository.listarTodos()) {
            lineas.add(FormatoLinea.unir(
                    secretaria.getId(),
                    secretaria.getNombre(),
                    secretaria.getApellido(),
                    secretaria.getDni()));
        }
        archivoTexto.escribirLineas(RUTA, DESCRIPCION, lineas);
    }

    SecretariaRepository cargar() {
        SecretariaRepository repository = new SecretariaRepository();
        long maxId = 0;

        for (String linea : archivoTexto.leerLineas(RUTA, DESCRIPCION)) {
            if (linea.trim().isEmpty()) {
                continue;
            }
            try {
                long id = cargarDesdeLinea(linea, repository);
                if (id > maxId) {
                    maxId = id;
                }
            } catch (RuntimeException excepcion) {
                System.err.println("Secretaria ignorada, linea invalida: " + linea);
            }
        }

        Secretaria.setContadorId(maxId);
        return repository;
    }

    private long cargarDesdeLinea(String linea, SecretariaRepository repository) {
        List<String> campos = FormatoLinea.parsear(linea);
        long id = Long.parseLong(campos.get(0));
        Secretaria.setContadorId(id - 1);

        Secretaria secretaria = new Secretaria(
                campos.get(1),
                campos.get(2),
                Integer.parseInt(campos.get(3)));
        repository.guardar(secretaria);
        return id;
    }
}
