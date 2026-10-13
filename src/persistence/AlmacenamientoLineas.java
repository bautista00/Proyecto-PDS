package persistence;

import java.util.List;

// Abstracción para leer y escribir líneas de texto.
// Las clases de persistencia dependen de esta interfaz y no del archivo concreto; ArchivoTexto la implementa (DIP).
public interface AlmacenamientoLineas {

    List<String> leerLineas(String ruta, String descripcion);

    void escribirLineas(String ruta, String descripcion, List<String> lineas);
}
