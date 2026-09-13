package persistence;

import entity.EstadoTurno;
import entity.Odontologo;
import entity.Paciente;
import entity.Secretaria;
import entity.Turno;
import repository.OdontologoRepository;
import repository.PacienteRepository;
import repository.SecretariaRepository;
import repository.TurnoRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

final class PersistenciaTurno {

    private static final String RUTA = "datos/turnos.txt";
    private static final String DESCRIPCION = "turnos";

    private final ArchivoTexto archivoTexto;

    PersistenciaTurno(ArchivoTexto archivoTexto) {
        this.archivoTexto = archivoTexto;
    }

    void guardar(TurnoRepository repository) {
        List<String> lineas = new ArrayList<>();
        for (Turno turno : repository.listarTodos()) {
            lineas.add(FormatoLinea.unir(
                    turno.getId(),
                    turno.getIdPaciente(),
                    turno.getIdOdontologo(),
                    turno.getIdSecretaria(),
                    turno.getFecha(),
                    turno.getHora(),
                    turno.getMotivoConsulta(),
                    turno.getEstado()));
        }
        archivoTexto.escribirLineas(RUTA, DESCRIPCION, lineas);
    }

    TurnoRepository cargar(PacienteRepository pacienteRepository,
                            OdontologoRepository odontologoRepository,
                            SecretariaRepository secretariaRepository) {
        TurnoRepository repository = new TurnoRepository();
        long maxId = 0;

        for (String linea : archivoTexto.leerLineas(RUTA, DESCRIPCION)) {
            if (linea.trim().isEmpty()) {
                continue;
            }
            try {
                Long id = cargarDesdeLinea(
                        linea,
                        pacienteRepository,
                        odontologoRepository,
                        secretariaRepository,
                        repository);
                if (id != null && id > maxId) {
                    maxId = id;
                }
            } catch (RuntimeException excepcion) {
                System.err.println("Turno ignorado, linea invalida: " + linea);
            }
        }

        Turno.setContadorId(maxId);
        return repository;
    }

    private Long cargarDesdeLinea(String linea,
                                  PacienteRepository pacienteRepository,
                                  OdontologoRepository odontologoRepository,
                                  SecretariaRepository secretariaRepository,
                                  TurnoRepository turnoRepository) {
        List<String> campos = FormatoLinea.parsear(linea);
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
        turno.vincularConActores();
        turnoRepository.guardar(turno);
        return id;
    }
}
