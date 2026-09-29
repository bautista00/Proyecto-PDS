package persistence;

import entity.CatalogoCoberturas;
import entity.Odontologo;
import entity.OdontologoFactory;
import entity.Paciente;
import entity.Secretaria;
import entity.Turno;
import repository.BuscadorPorId;
import repository.RepositorioEscritura;
import repository.RepositorioLectura;


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

    public PersistenciaServicio(AlmacenamientoLineas almacenamiento,
                                CatalogoCoberturas catalogoCoberturas,
                                OdontologoFactory odontologoFactory) {
        this.persistenciaPaciente = new PersistenciaPaciente(almacenamiento, catalogoCoberturas);
        this.persistenciaOdontologo = new PersistenciaOdontologo(almacenamiento, odontologoFactory);
        this.persistenciaSecretaria = new PersistenciaSecretaria(almacenamiento);
        this.persistenciaTurno = new PersistenciaTurno(almacenamiento);
    }

    public void guardar(RepositorioLectura<Paciente> pacienteRepository,
                        RepositorioLectura<Odontologo> odontologoRepository,
                        RepositorioLectura<Secretaria> secretariaRepository,
                        RepositorioLectura<Turno> turnoRepository) {
        persistenciaPaciente.guardar(pacienteRepository);
        persistenciaOdontologo.guardar(odontologoRepository);
        persistenciaSecretaria.guardar(secretariaRepository);
        persistenciaTurno.guardar(turnoRepository);
    }

    public void cargarPacientes(RepositorioEscritura<Paciente> repository) {
        persistenciaPaciente.cargar(repository);
    }

    public void cargarOdontologos(RepositorioEscritura<Odontologo> repository) {
        persistenciaOdontologo.cargar(repository);
    }

    public void cargarSecretarias(RepositorioEscritura<Secretaria> repository) {
        persistenciaSecretaria.cargar(repository);
    }

    public void cargarTurnos(BuscadorPorId<Paciente> pacienteRepository,
                             BuscadorPorId<Odontologo> odontologoRepository,
                             BuscadorPorId<Secretaria> secretariaRepository,
                             RepositorioEscritura<Turno> turnoRepository) {
        persistenciaTurno.cargar(
                pacienteRepository,
                odontologoRepository,
                secretariaRepository,
                turnoRepository);
    }
}
