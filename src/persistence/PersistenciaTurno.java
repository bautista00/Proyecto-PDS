package persistence;

import entity.EstadoTurno;
import entity.Odontologo;
import entity.Paciente;
import entity.Secretaria;
import entity.Turno;
import repository.BuscadorPorId;
import repository.RepositorioEscritura;
import repository.RepositorioLectura;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

// Clase para manejar la persistencia de los turnos en un archivo de texto.
// Se encarga de guardar y cargar los turnos desde un archivo,
//  utilizando la abstraccion AlmacenamientoLineas para leer y escribir,
//  y la clase FormatoLinea para unir y parsear los campos de cada turno.


final class PersistenciaTurno {

    private static final String RUTA = "datos/turnos.txt";
    private static final String DESCRIPCION = "turnos";

    private final AlmacenamientoLineas almacenamiento;

    PersistenciaTurno(AlmacenamientoLineas almacenamiento) {
        this.almacenamiento = almacenamiento;
    }

    void guardar(RepositorioLectura<Turno> repository) {
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
        almacenamiento.escribirLineas(RUTA, DESCRIPCION, lineas);
    }

    void cargar(BuscadorPorId<Paciente> pacienteRepository,
                BuscadorPorId<Odontologo> odontologoRepository,
                BuscadorPorId<Secretaria> secretariaRepository,
                RepositorioEscritura<Turno> repository) {
        for (String linea : almacenamiento.leerLineas(RUTA, DESCRIPCION)) {
            if (linea.trim().isEmpty()) {
                continue;
            }
            try {
                cargarDesdeLinea(
                        linea,
                        pacienteRepository,
                        odontologoRepository,
                        secretariaRepository,
                        repository);
            } catch (RuntimeException excepcion) {
                System.err.println("Turno ignorado, linea invalida: " + linea);
            }
        }
    }

    private Long cargarDesdeLinea(String linea,
                                  BuscadorPorId<Paciente> pacienteRepository,
                                  BuscadorPorId<Odontologo> odontologoRepository,
                                  BuscadorPorId<Secretaria> secretariaRepository,
                                  RepositorioEscritura<Turno> turnoRepository) {
        List<String> campos = FormatoLinea.parsear(linea);
        long id = Long.parseLong(campos.get(0));
        Paciente paciente = pacienteRepository.buscarPorId(Long.parseLong(campos.get(1)));
        Odontologo odontologo = odontologoRepository.buscarPorId(Long.parseLong(campos.get(2)));
        Secretaria secretaria = secretariaRepository.buscarPorId(Long.parseLong(campos.get(3)));
        if (paciente == null || odontologo == null || secretaria == null) {
            return null;
        }

        Turno turno = Turno.rehidratar(
                id,
                paciente,
                odontologo,
                secretaria,
                LocalDate.parse(campos.get(4)),
                LocalTime.parse(campos.get(5)),
                campos.get(6),
                EstadoTurno.valueOf(campos.get(7)));
        turno.vincularConActores();
        turnoRepository.guardar(turno);
        return id;
    }
}
