package persistence;

import entity.EspecialidadOdontologica;
import entity.Odontologo;
import entity.OdontologoFactory;
import repository.RepositorioEscritura;
import repository.RepositorioLectura;

import java.util.ArrayList;
import java.util.List;


// Clase para manejar la persistencia de los odontólogos en un archivo de texto.
// Se encarga de guardar y cargar los odontólogos desde un archivo,
//  utilizando la abstraccion AlmacenamientoLineas para leer y escribir,
//  y la clase FormatoLinea para unir y parsear los campos de cada odontólogo


final class PersistenciaOdontologo {

    private static final String RUTA = "datos/odontologos.txt";
    private static final String DESCRIPCION = "odontologos";

    private final AlmacenamientoLineas almacenamiento;
    private final OdontologoFactory odontologoFactory;

    PersistenciaOdontologo(AlmacenamientoLineas almacenamiento,
                           OdontologoFactory odontologoFactory) {
        this.almacenamiento = almacenamiento;
        this.odontologoFactory = odontologoFactory;
    }

    void guardar(RepositorioLectura<Odontologo> repository) {
        List<String> lineas = new ArrayList<>();
        for (Odontologo odontologo : repository.listarTodos()) {
            lineas.add(FormatoLinea.unir(
                    odontologo.getId(),
                    odontologo.getNombre(),
                    odontologo.getApellido(),
                    odontologo.getDni(),
                    odontologo.getMatricula(),
                    odontologo.getEspecialidad().getCodigo()));
        }
        almacenamiento.escribirLineas(RUTA, DESCRIPCION, lineas);
    }

    void cargar(RepositorioEscritura<Odontologo> repository) {
        for (String linea : almacenamiento.leerLineas(RUTA, DESCRIPCION)) {
            if (linea.trim().isEmpty()) {
                continue;
            }
            try {
                cargarDesdeLinea(linea, repository);
            } catch (RuntimeException excepcion) {
                System.err.println("Odontologo ignorado, linea invalida: " + linea);
            }
        }
    }

    private void cargarDesdeLinea(String linea, RepositorioEscritura<Odontologo> repository) {
        List<String> campos = FormatoLinea.parsear(linea);
        long id = Long.parseLong(campos.get(0));

        EspecialidadOdontologica especialidad = convertirEspecialidadPersistida(campos.get(5));
        Odontologo odontologo = odontologoFactory.rehidratar(
                id,
                especialidad,
                campos.get(1),
                campos.get(2),
                Integer.parseInt(campos.get(3)),
                campos.get(4));
        repository.guardar(odontologo);
    }

    private EspecialidadOdontologica convertirEspecialidadPersistida(String valor) {
        if (valor == null) {
            throw new IllegalArgumentException("La especialidad persistida no puede ser nula.");
        }

        return odontologoFactory.buscarEspecialidad(valor.trim());
    }
}
