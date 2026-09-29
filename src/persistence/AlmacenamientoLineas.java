package persistence;

import java.util.List;

public interface AlmacenamientoLineas {

    List<String> leerLineas(String ruta, String descripcion);

    void escribirLineas(String ruta, String descripcion, List<String> lineas);
}
