package persistence;

import entity.Domicilio;
import entity.Endodoncista;
import entity.EstadoTurno;
import entity.Odontologo;
import entity.OdontologoGeneral;
import entity.Ortodoncista;
import entity.Paciente;
import entity.Secretaria;
import entity.Turno;
import repository.OdontologoRepository;
import repository.PacienteRepository;
import repository.SecretariaRepository;
import repository.TurnoRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaServicio {

    private static final String DIRECTORIO_DATOS = "datos/";
    private static final String SEPARADOR = ";";
    private static final String PACIENTES_FILE = DIRECTORIO_DATOS + "pacientes.txt";
    private static final String ODONTOLOGOS_FILE = DIRECTORIO_DATOS + "odontologos.txt";
    private static final String SECRETARIAS_FILE = DIRECTORIO_DATOS + "secretarias.txt";
    private static final String TURNOS_FILE = DIRECTORIO_DATOS + "turnos.txt";

    private static String escapar(Object valor) {
        String texto = String.valueOf(valor);
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < texto.length(); i++) {
            char caracter = texto.charAt(i);
            switch (caracter) {
                case '\\': resultado.append("\\\\"); break;
                case ';':  resultado.append("\\;");  break;
                case '\n': resultado.append("\\n");  break;
                case '\r': resultado.append("\\r");  break;
                default:   resultado.append(caracter);
            }
        }
        return resultado.toString();
    }

    private static String unirLinea(Object... campos) {
        StringBuilder linea = new StringBuilder();
        for (int i = 0; i < campos.length; i++) {
            if (i > 0) linea.append(SEPARADOR);
            linea.append(escapar(campos[i]));
        }
        return linea.toString();
    }

    private static List<String> parsearLinea(String linea) {
        List<String> campos = new ArrayList<>();
        StringBuilder campoActual = new StringBuilder();
        int indice = 0;
        int longitud = linea.length();

        while (indice < longitud) {
            char caracter = linea.charAt(indice);
            if (caracter == '\\' && indice + 1 < longitud) {
                char caracterEscapado = linea.charAt(indice + 1);
                switch (caracterEscapado) {
                    case '\\': campoActual.append('\\'); break;
                    case ';':  campoActual.append(';');  break;
                    case 'n':  campoActual.append('\n'); break;
                    case 'r':  campoActual.append('\r'); break;
                    default:   campoActual.append(caracterEscapado);
                }
                indice += 2;
            } else if (caracter == ';') {
                campos.add(campoActual.toString());
                campoActual.setLength(0);
                indice++;
            } else {
                campoActual.append(caracter);
                indice++;
            }
        }
        campos.add(campoActual.toString());
        return campos;
    }

    public void guardar(PacienteRepository pacienteRepository,
                        OdontologoRepository odontologoRepository,
                        SecretariaRepository secretariaRepository,
                        TurnoRepository turnoRepository) {
        new File(DIRECTORIO_DATOS).mkdirs();
        guardarPacientes(pacienteRepository);
        guardarOdontologos(odontologoRepository);
        guardarSecretarias(secretariaRepository);
        guardarTurnos(turnoRepository);
    }

    private void guardarPacientes(PacienteRepository repository) {
        try (PrintWriter escritor = new PrintWriter(new FileWriter(PACIENTES_FILE))) {
            for (Paciente paciente : repository.listarTodos()) {
                escritor.println(unirLinea(
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
                        paciente.getObraSocial()));
            }
        } catch (IOException excepcion) {
            System.err.println("Error al guardar pacientes: " + excepcion.getMessage());
        }
    }

    private void guardarOdontologos(OdontologoRepository repository) {
        try (PrintWriter escritor = new PrintWriter(new FileWriter(ODONTOLOGOS_FILE))) {
            for (Odontologo odontologo : repository.listarTodos()) {
                escritor.println(unirLinea(
                        odontologo.getId(),
                        odontologo.getNombre(),
                        odontologo.getApellido(),
                        odontologo.getDni(),
                        odontologo.getMatricula(),
                        odontologo.getEspecialidad()));
            }
        } catch (IOException excepcion) {
            System.err.println("Error al guardar odontologos: " + excepcion.getMessage());
        }
    }

    private void guardarSecretarias(SecretariaRepository repository) {
        try (PrintWriter escritor = new PrintWriter(new FileWriter(SECRETARIAS_FILE))) {
            for (Secretaria secretaria : repository.listarTodos()) {
                escritor.println(unirLinea(
                        secretaria.getId(),
                        secretaria.getNombre(),
                        secretaria.getApellido(),
                        secretaria.getDni()));
            }
        } catch (IOException excepcion) {
            System.err.println("Error al guardar secretarias: " + excepcion.getMessage());
        }
    }

    private void guardarTurnos(TurnoRepository repository) {
        try (PrintWriter escritor = new PrintWriter(new FileWriter(TURNOS_FILE))) {
            for (Turno turno : repository.listarTodos()) {
                escritor.println(unirLinea(
                        turno.getId(),
                        turno.getIdPaciente(),
                        turno.getIdOdontologo(),
                        turno.getIdSecretaria(),
                        turno.getFecha(),
                        turno.getHora(),
                        turno.getMotivoConsulta(),
                        turno.getEstado()));
            }
        } catch (IOException excepcion) {
            System.err.println("Error al guardar turnos: " + excepcion.getMessage());
        }
    }

    public PacienteRepository cargarPacientes() {
        PacienteRepository repository = new PacienteRepository();
        File archivo = new File(PACIENTES_FILE);
        if (!archivo.exists()) return repository;

        long maxId = 0;
        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                try {
                    long id = cargarPacienteDesdeLinea(linea, repository);
                    if (id > maxId) maxId = id;
                } catch (RuntimeException excepcion) {
                    System.err.println("Paciente ignorado, linea invalida: " + linea);
                }
            }
        } catch (IOException excepcion) {
            System.err.println("Error al cargar pacientes: " + excepcion.getMessage());
        }
        Paciente.setContadorId(maxId);
        return repository;
    }


    private long cargarPacienteDesdeLinea(String linea, PacienteRepository repository) {
        List<String> campos = parsearLinea(linea);
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
                Boolean.parseBoolean(campos.get(10)));
        paciente.setFechaAlta(LocalDate.parse(campos.get(5)));
        repository.guardar(paciente);
        return id;
    }

    public OdontologoRepository cargarOdontologos() {
        OdontologoRepository repository = new OdontologoRepository();
        File archivo = new File(ODONTOLOGOS_FILE);
        if (!archivo.exists()) return repository;

        long maxId = 0;
        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                try {
                    List<String> campos = parsearLinea(linea);
                    long id = Long.parseLong(campos.get(0));
                    Odontologo.setContadorId(id - 1);
                    Odontologo odontologo = crearOdontologo(campos);
                    repository.guardar(odontologo);
                    if (id > maxId) maxId = id;
                } catch (RuntimeException excepcion) {
                    System.err.println("Odontologo ignorado, linea invalida: " + linea);
                }
            }
        } catch (IOException excepcion) {
            System.err.println("Error al cargar odontologos: " + excepcion.getMessage());
        }
        Odontologo.setContadorId(maxId);
        return repository;
    }

    private Odontologo crearOdontologo(List<String> campos) {
        String nombre = campos.get(1);
        String apellido = campos.get(2);
        Integer dni = Integer.parseInt(campos.get(3));
        String matricula = campos.get(4);

        switch (campos.get(5)) {
            case "Ortodoncia":
                return new Ortodoncista(nombre, apellido, dni, matricula);
            case "Endodoncia":
                return new Endodoncista(nombre, apellido, dni, matricula);
            default:
                return new OdontologoGeneral(nombre, apellido, dni, matricula);
        }
    }

    public SecretariaRepository cargarSecretarias() {
        SecretariaRepository repository = new SecretariaRepository();
        File archivo = new File(SECRETARIAS_FILE);
        if (!archivo.exists()) return repository;

        long maxId = 0;
        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                try {
                    List<String> campos = parsearLinea(linea);
                    long id = Long.parseLong(campos.get(0));
                    Secretaria.setContadorId(id - 1);
                    Secretaria secretaria = new Secretaria(
                            campos.get(1),
                            campos.get(2),
                            Integer.parseInt(campos.get(3)));
                    repository.guardar(secretaria);
                    if (id > maxId) maxId = id;
                } catch (RuntimeException excepcion) {
                    System.err.println("Secretaria ignorada, linea invalida: " + linea);
                }
            }
        } catch (IOException excepcion) {
            System.err.println("Error al cargar secretarias: " + excepcion.getMessage());
        }
        Secretaria.setContadorId(maxId);
        return repository;
    }

    public TurnoRepository cargarTurnos(PacienteRepository pacienteRepository,
                                        OdontologoRepository odontologoRepository,
                                        SecretariaRepository secretariaRepository) {
        TurnoRepository repository = new TurnoRepository();
        File archivo = new File(TURNOS_FILE);
        if (!archivo.exists()) return repository;

        long maxId = 0;
        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                try {
                    Long id = cargarTurnoDesdeLinea(
                            linea,
                            pacienteRepository,
                            odontologoRepository,
                            secretariaRepository,
                            repository);
                    if (id != null && id > maxId) maxId = id;
                } catch (RuntimeException excepcion) {
                    System.err.println("Turno ignorado, linea invalida: " + linea);
                }
            }
        } catch (IOException excepcion) {
            System.err.println("Error al cargar turnos: " + excepcion.getMessage());
        }
        Turno.setContadorId(maxId);
        return repository;
    }
    private Long cargarTurnoDesdeLinea(String linea,
                                       PacienteRepository pacienteRepository,
                                       OdontologoRepository odontologoRepository,
                                       SecretariaRepository secretariaRepository,
                                       TurnoRepository turnoRepository) {
        List<String> campos = parsearLinea(linea);
        long id = Long.parseLong(campos.get(0));
        Paciente paciente = pacienteRepository.buscarPorId(Long.parseLong(campos.get(1)));
        Odontologo odontologo = odontologoRepository.buscarPorId(Long.parseLong(campos.get(2)));
        Secretaria secretaria = secretariaRepository.buscarPorId(Long.parseLong(campos.get(3)));
        if (paciente == null || odontologo == null || secretaria == null) {
            return null;
        }

        Turno.setContadorId(id - 1);
        Turno turno = new Turno(
                paciente,
                odontologo,
                secretaria,
                LocalDate.parse(campos.get(4)),
                LocalTime.parse(campos.get(5)),
                campos.get(6));
        turno.setEstado(EstadoTurno.valueOf(campos.get(7)));
        paciente.agregarTurno(turno);
        odontologo.agregarTurno(turno);
        secretaria.agregarTurno(turno);
        turnoRepository.guardar(turno);
        return id;
    }

}
