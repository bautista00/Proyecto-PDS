package persistence;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;

final class ArchivoTexto {

    List<String> leerLineas(String ruta, String descripcion) {
        Path archivo = Path.of(ruta);
        if (!Files.exists(archivo)) {
            return Collections.emptyList();
        }

        try {
            return Files.readAllLines(archivo, Charset.defaultCharset());
        } catch (IOException excepcion) {
            System.err.println("Error al cargar " + descripcion + ": " + excepcion.getMessage());
            return Collections.emptyList();
        }
    }

    void escribirLineas(String ruta, String descripcion, List<String> lineas) {
        Path archivo = Path.of(ruta);
        try {
            Path directorio = archivo.getParent();
            if (directorio != null) {
                Files.createDirectories(directorio);
            }
            Files.write(
                    archivo,
                    lineas,
                    Charset.defaultCharset(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE);
        } catch (IOException excepcion) {
            System.err.println("Error al guardar " + descripcion + ": " + excepcion.getMessage());
        }
    }
}
