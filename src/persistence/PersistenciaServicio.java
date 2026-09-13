package persistence;

import repository.OdontologoRepository;
import repository.PacienteRepository;
import repository.SecretariaRepository;
import repository.TurnoRepository;

public class PersistenciaServicio {

    private final PersistenciaPaciente persistenciaPaciente;
    private final PersistenciaOdontologo persistenciaOdontologo;
    private final PersistenciaSecretaria persistenciaSecretaria;
    private final PersistenciaTurno persistenciaTurno;

    public PersistenciaServicio() {
        ArchivoTexto archivoTexto = new ArchivoTexto();
        this.persistenciaPaciente = new PersistenciaPaciente(archivoTexto);
        this.persistenciaOdontologo = new PersistenciaOdontologo(archivoTexto);
        this.persistenciaSecretaria = new PersistenciaSecretaria(archivoTexto);
        this.persistenciaTurno = new PersistenciaTurno(archivoTexto);
    }

    public void guardar(PacienteRepository pacienteRepository,
                        OdontologoRepository odontologoRepository,
                        SecretariaRepository secretariaRepository,
                        TurnoRepository turnoRepository) {
        persistenciaPaciente.guardar(pacienteRepository);
        persistenciaOdontologo.guardar(odontologoRepository);
        persistenciaSecretaria.guardar(secretariaRepository);
        persistenciaTurno.guardar(turnoRepository);
    }

    public PacienteRepository cargarPacientes() {
        return persistenciaPaciente.cargar();
    }

    public OdontologoRepository cargarOdontologos() {
        return persistenciaOdontologo.cargar();
    }

    public SecretariaRepository cargarSecretarias() {
        return persistenciaSecretaria.cargar();
    }

    public TurnoRepository cargarTurnos(PacienteRepository pacienteRepository,
                                        OdontologoRepository odontologoRepository,
                                        SecretariaRepository secretariaRepository) {
        return persistenciaTurno.cargar(
                pacienteRepository,
                odontologoRepository,
                secretariaRepository);
    }
}
