package persistence;

import entity.CoberturaPaciente;
import entity.Domicilio;
import entity.Paciente;
import repository.PacienteRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

final class PersistenciaPaciente {

    private static final String RUTA = "datos/pacientes.txt";
    private static final String DESCRIPCION = "pacientes";

    private final ArchivoTexto archivoTexto;

    PersistenciaPaciente(ArchivoTexto archivoTexto) {
        this.archivoTexto = archivoTexto;
    }

    void guardar(PacienteRepository repository) {
        List<String> lineas = new ArrayList<>();
        for (Paciente paciente : repository.listarTodos()) {
            lineas.add(FormatoLinea.unir(
                    paciente.getId(),
                    paciente.getNombre(),
                    paciente.getApellido(),
                    paciente.getDni(),
                    paciente.getEmail(),
                    paciente.getFechaAlta(),
                    paciente.getCalleDomicilio(),
                    paciente.getNumeroDomicilio(),
                    paciente.getLocalidadDomicilio(),
                    paciente.getProvinciaDomicilio(),
                    paciente.getCobertura().name()));
        }
        archivoTexto.escribirLineas(RUTA, DESCRIPCION, lineas);
    }

    PacienteRepository cargar() {
        PacienteRepository repository = new PacienteRepository();
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
                System.err.println("Paciente ignorado, linea invalida: " + linea);
            }
        }

        Paciente.setContadorId(maxId);
        return repository;
    }

    private long cargarDesdeLinea(String linea, PacienteRepository repository) {
        List<String> campos = FormatoLinea.parsear(linea);
        long id = Long.parseLong(campos.get(0));
        Paciente.setContadorId(id - 1);

        Domicilio domicilio = new Domicilio(
                campos.get(6),
                Integer.parseInt(campos.get(7)),
                campos.get(8),
                campos.get(9));
        Paciente paciente = new Paciente(
                campos.get(1),
                campos.get(2),
                Integer.parseInt(campos.get(3)),
                campos.get(4),
                domicilio,
                convertirCoberturaPersistida(campos.get(10)));
        paciente.setFechaAlta(LocalDate.parse(campos.get(5)));
        repository.guardar(paciente);
        return id;
    }

    private CoberturaPaciente convertirCoberturaPersistida(String valor) {
        if (valor == null) {
            throw new IllegalArgumentException("La cobertura persistida no puede ser nula.");
        }

        String valorNormalizado = valor.trim();
        if ("true".equalsIgnoreCase(valorNormalizado)) {
            return CoberturaPaciente.OBRA_SOCIAL;
        }
        if ("false".equalsIgnoreCase(valorNormalizado)) {
            return CoberturaPaciente.PARTICULAR;
        }

        for (CoberturaPaciente cobertura : CoberturaPaciente.values()) {
            if (cobertura.name().equalsIgnoreCase(valorNormalizado)
                    || cobertura.getDescripcion().equalsIgnoreCase(valorNormalizado)) {
                return cobertura;
            }
        }

        throw new IllegalArgumentException("Cobertura persistida desconocida: " + valor);
    }
}
