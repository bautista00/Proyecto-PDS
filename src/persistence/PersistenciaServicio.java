package persistence;

import repository.OdontologoRepository;
import repository.PacienteRepository;
import repository.SecretariaRepository;
import repository.TurnoRepository;


// Clase para manejar la persistencia de los datos de la aplicación.
// Se encarga de guardar y cargar los datos desde archivos de texto,
//  utilizando las clases PersistenciaPaciente, PersistenciaOdontologo, PersistenciaSecretaria 
// y PersistenciaTurno para cada tipo de entidad.
// Esta clase actúa como un servicio de persistencia, coordinando la lectura 
// y escritura de los datos de todas las entidades.



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
