package persistence;

import entity.EspecialidadOdontologica;
import entity.Odontologo;
import entity.OdontologoFactory;
import repository.OdontologoRepository;

import java.util.ArrayList;
import java.util.List;

final class PersistenciaOdontologo {

    private static final String RUTA = "datos/odontologos.txt";
    private static final String DESCRIPCION = "odontologos";

    private final ArchivoTexto archivoTexto;

    PersistenciaOdontologo(ArchivoTexto archivoTexto) {
        this.archivoTexto = archivoTexto;
    }

    void guardar(OdontologoRepository repository) {
        List<String> lineas = new ArrayList<>();
        for (Odontologo odontologo : repository.listarTodos()) {
            lineas.add(FormatoLinea.unir(
                    odontologo.getId(),
                    odontologo.getNombre(),
                    odontologo.getApellido(),
                    odontologo.getDni(),
                    odontologo.getMatricula(),
                    odontologo.getEspecialidad().name()));
        }
        archivoTexto.escribirLineas(RUTA, DESCRIPCION, lineas);
    }

    OdontologoRepository cargar() {
        OdontologoRepository repository = new OdontologoRepository();
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
                System.err.println("Odontologo ignorado, linea invalida: " + linea);
            }
        }

        Odontologo.setContadorId(maxId);
        return repository;
    }

    private long cargarDesdeLinea(String linea, OdontologoRepository repository) {
        List<String> campos = FormatoLinea.parsear(linea);
        long id = Long.parseLong(campos.get(0));
        Odontologo.setContadorId(id - 1);

        EspecialidadOdontologica especialidad = convertirEspecialidadPersistida(campos.get(5));
        Odontologo odontologo = OdontologoFactory.crear(
                especialidad,
                campos.get(1),
                campos.get(2),
                Integer.parseInt(campos.get(3)),
                campos.get(4));
        repository.guardar(odontologo);
        return id;
    }

    private EspecialidadOdontologica convertirEspecialidadPersistida(String valor) {
        if (valor == null) {
            throw new IllegalArgumentException("La especialidad persistida no puede ser nula.");
        }

        String valorNormalizado = valor.trim();
        for (EspecialidadOdontologica especialidad : EspecialidadOdontologica.values()) {
            if (especialidad.name().equalsIgnoreCase(valorNormalizado)
                    || especialidad.getDescripcion().equalsIgnoreCase(valorNormalizado)) {
                return especialidad;
            }
        }

        throw new IllegalArgumentException("Especialidad persistida desconocida: " + valor);
    }
}
