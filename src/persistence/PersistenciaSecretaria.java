package persistence;

import entity.Secretaria;
import repository.SecretariaRepository;

import java.util.ArrayList;
import java.util.List;


// Clase para manejar la persistencia de las secretarias en un archivo de texto.
// Se encarga de guardar y cargar las secretarias desde un archivo,
//  utilizando la clase ArchivoTexto para la lectura y escritura de líneas,
//  y la clase FormatoLinea para unir y parsear los campos de cada secretaria.

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

        for (String linea : archivoTexto.leerLineas(RUTA, DESCRIPCION)) {
            if (linea.trim().isEmpty()) {
                continue;
            }
            try {
                cargarDesdeLinea(linea, repository);
            } catch (RuntimeException excepcion) {
                System.err.println("Secretaria ignorada, linea invalida: " + linea);
            }
        }
        return repository;
    }

    private void cargarDesdeLinea(String linea, SecretariaRepository repository) {
        List<String> campos = FormatoLinea.parsear(linea);
        long id = Long.parseLong(campos.get(0));

        Secretaria secretaria = Secretaria.rehidratar(
                id,
                campos.get(1),
                campos.get(2),
                Integer.parseInt(campos.get(3)));
        repository.guardar(secretaria);
    }
}
