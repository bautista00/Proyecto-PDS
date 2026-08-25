package persistence;

import entity.*;
import repository.*;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaServicio {

    private static final String DIR             = "datos/";
    private static final String SEP             = ";";
    private static final String PACIENTES_FILE   = DIR + "pacientes.txt";
    private static final String ODONTOLOGOS_FILE = DIR + "odontologos.txt";
    private static final String SECRETARIAS_FILE = DIR + "secretarias.txt";
    private static final String TURNOS_FILE      = DIR + "turnos.txt";

    // ── ESCAPE / PARSE ──────────────────────────────────────────────────────────
    // El archivo usa ';' como separador y un salto de linea por registro.
    // Para que un campo de texto pueda contener ';' o saltos de linea sin romper
    // el formato, se "escapan" al guardar y se "des-escapan" al leer:
    //   '\'  -> "\\"      ';'  -> "\;"      '\n' -> "\n"      '\r' -> "\r"

    private static String esc(Object valor) {
        String s = String.valueOf(valor);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '\\': sb.append("\\\\"); break;
                case ';':  sb.append("\\;");  break;
                case '\n': sb.append("\\n");  break;
                case '\r': sb.append("\\r");  break;
                default:   sb.append(c);
            }
        }
        return sb.toString();
    }

    // Une varios campos en una linea, escapando cada uno
    private static String unirLinea(Object... campos) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < campos.length; i++) {
            if (i > 0) sb.append(SEP);
            sb.append(esc(campos[i]));
        }
        return sb.toString();
    }

    // Separa una linea en campos respetando los escapes y des-escapando cada valor
    private static List<String> parsearLinea(String linea) {
        List<String> campos = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int i = 0, n = linea.length();
        while (i < n) {
            char c = linea.charAt(i);
            if (c == '\\' && i + 1 < n) {
                char x = linea.charAt(i + 1);
                switch (x) {
                    case '\\': sb.append('\\'); break;
                    case ';':  sb.append(';');  break;
                    case 'n':  sb.append('\n'); break;
                    case 'r':  sb.append('\r'); break;
                    default:   sb.append(x);
                }
                i += 2;
            } else if (c == ';') {
                campos.add(sb.toString());
                sb.setLength(0);
                i++;
            } else {
                sb.append(c);
                i++;
            }
        }
        campos.add(sb.toString());
        return campos;
    }

    // ── GUARDAR ────────────────────────────────────────────────────────────────

    public void guardar(PacienteRepository pacienteRepo,
                        OdontologoRepository odontologoRepo,
                        SecretariaRepository secretariaRepo,
                        TurnoRepository turnoRepo) {
        new File(DIR).mkdirs();
        guardarPacientes(pacienteRepo);
        guardarOdontologos(odontologoRepo);
        guardarSecretarias(secretariaRepo);
        guardarTurnos(turnoRepo);
    }

    private void guardarPacientes(PacienteRepository repo) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(PACIENTES_FILE))) {
            for (Paciente p : repo.listarTodos()) {
                Domicilio d = p.getDomicilio();
                pw.println(unirLinea(p.getId(), p.getNombre(), p.getApellido(),
                        p.getDni(), p.getEmail(), p.getFechaAlta(),
                        d.getCalle(), d.getNumero(), d.getLocalidad(), d.getProvincia(),
                        p.getObraSocial()));
            }
        } catch (IOException e) {
            System.err.println("Error al guardar pacientes: " + e.getMessage());
        }
    }

    private void guardarOdontologos(OdontologoRepository repo) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ODONTOLOGOS_FILE))) {
            for (Odontologo o : repo.listarTodos()) {
                pw.println(unirLinea(o.getId(), o.getNombre(), o.getApellido(),
                        o.getDni(), o.getMatricula(), o.getEspecialidad()));
            }
        } catch (IOException e) {
            System.err.println("Error al guardar odontologos: " + e.getMessage());
        }
    }

    private void guardarSecretarias(SecretariaRepository repo) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(SECRETARIAS_FILE))) {
            for (Secretaria s : repo.listarTodos()) {
                pw.println(unirLinea(s.getId(), s.getNombre(), s.getApellido(), s.getDni()));
            }
        } catch (IOException e) {
            System.err.println("Error al guardar secretarias: " + e.getMessage());
        }
    }

    private void guardarTurnos(TurnoRepository repo) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(TURNOS_FILE))) {
            for (Turno t : repo.listarTodos()) {
                pw.println(unirLinea(t.getId(), t.getPaciente().getId(),
                        t.getOdontologo().getId(), t.getSecretaria().getId(),
                        t.getFecha(), t.getHora(), t.getMotivoConsulta(), t.getEstado()));
            }
        } catch (IOException e) {
            System.err.println("Error al guardar turnos: " + e.getMessage());
        }
    }

    // ── CARGAR ─────────────────────────────────────────────────────────────────

    public PacienteRepository cargarPacientes() {
        PacienteRepository repo = new PacienteRepository();
        File archivo = new File(PACIENTES_FILE);
        if (!archivo.exists()) return repo;

        long maxId = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                try {
                    List<String> p = parsearLinea(linea);
                    long id = Long.parseLong(p.get(0));
                    Paciente.setContadorId(id - 1);
                    Domicilio domicilio = new Domicilio(p.get(6), Integer.parseInt(p.get(7)), p.get(8), p.get(9));
                    Paciente paciente = new Paciente(p.get(1), p.get(2), Integer.parseInt(p.get(3)), p.get(4), domicilio, Boolean.parseBoolean(p.get(10)));
                    paciente.setFechaAlta(LocalDate.parse(p.get(5)));
                    repo.guardar(paciente);
                    if (id > maxId) maxId = id;
                } catch (RuntimeException ex) {
                    System.err.println("Paciente ignorado, linea invalida: " + linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar pacientes: " + e.getMessage());
        }
        Paciente.setContadorId(maxId);
        return repo;
    }

    public OdontologoRepository cargarOdontologos() {
        OdontologoRepository repo = new OdontologoRepository();
        File archivo = new File(ODONTOLOGOS_FILE);
        if (!archivo.exists()) return repo;

        long maxId = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                try {
                    List<String> p = parsearLinea(linea);
                    long id = Long.parseLong(p.get(0));
                    Odontologo.setContadorId(id - 1);
                    Odontologo o;
                    switch (p.get(5)) {
                        case "Ortodoncia": o = new Ortodoncista(p.get(1), p.get(2), Integer.parseInt(p.get(3)), p.get(4)); break;
                        case "Endodoncia": o = new Endodoncista(p.get(1), p.get(2), Integer.parseInt(p.get(3)), p.get(4)); break;
                        default:           o = new OdontologoGeneral(p.get(1), p.get(2), Integer.parseInt(p.get(3)), p.get(4)); break;
                    }
                    repo.guardar(o);
                    if (id > maxId) maxId = id;
                } catch (RuntimeException ex) {
                    System.err.println("Odontologo ignorado, linea invalida: " + linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar odontologos: " + e.getMessage());
        }
        Odontologo.setContadorId(maxId);
        return repo;
    }

    public SecretariaRepository cargarSecretarias() {
        SecretariaRepository repo = new SecretariaRepository();
        File archivo = new File(SECRETARIAS_FILE);
        if (!archivo.exists()) return repo;

        long maxId = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                try {
                    List<String> p = parsearLinea(linea);
                    long id = Long.parseLong(p.get(0));
                    Secretaria.setContadorId(id - 1);
                    Secretaria s = new Secretaria(p.get(1), p.get(2), Integer.parseInt(p.get(3)));
                    repo.guardar(s);
                    if (id > maxId) maxId = id;
                } catch (RuntimeException ex) {
                    System.err.println("Secretaria ignorada, linea invalida: " + linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar secretarias: " + e.getMessage());
        }
        Secretaria.setContadorId(maxId);
        return repo;
    }

    // Recibe los repos ya cargados para resolver las referencias de Paciente, Odontologo y Secretaria
    public TurnoRepository cargarTurnos(PacienteRepository pacienteRepo,
                                        OdontologoRepository odontologoRepo,
                                        SecretariaRepository secretariaRepo) {
        TurnoRepository repo = new TurnoRepository();
        File archivo = new File(TURNOS_FILE);
        if (!archivo.exists()) return repo;

        long maxId = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                try {
                    List<String> p = parsearLinea(linea);
                    long id = Long.parseLong(p.get(0));
                    Paciente   paciente   = pacienteRepo.buscarPorId(Long.parseLong(p.get(1)));
                    Odontologo odontologo = odontologoRepo.buscarPorId(Long.parseLong(p.get(2)));
                    Secretaria secretaria = secretariaRepo.buscarPorId(Long.parseLong(p.get(3)));
                    if (paciente == null || odontologo == null || secretaria == null) continue;
                    Turno.setContadorId(id - 1);
                    Turno turno = new Turno(paciente, odontologo, secretaria,
                            LocalDate.parse(p.get(4)), LocalTime.parse(p.get(5)), p.get(6));
                    turno.setEstado(EstadoTurno.valueOf(p.get(7)));
                    // Reconstruir listas historial en memoria
                    paciente.agregarTurno(turno);
                    odontologo.agregarTurno(turno);
                    secretaria.agregarTurno(turno);
                    repo.guardar(turno);
                    if (id > maxId) maxId = id;
                } catch (RuntimeException ex) {
                    System.err.println("Turno ignorado, linea invalida: " + linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar turnos: " + e.getMessage());
        }
        Turno.setContadorId(maxId);
        return repo;
    }
}
