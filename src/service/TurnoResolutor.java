package service;

import entity.Odontologo;
import entity.Paciente;
import entity.Secretaria;
import entity.Turno;
import exception.DatoInvalidoException;
import exception.OdontologoNoEncontradoException;
import exception.PacienteNoEncontradoException;
import repository.IOdontologoRepository;
import repository.IPacienteRepository;
import repository.ISecretariaRepository;
import repository.ITurnoRepository;


 // Resuelve IDs a entidades (paciente, odontólogo, secretaria, turno) y falla si no existen.
 // Se extrajo para que TurnoServiceImpl y TurnoConsultaService compartan esa búsqueda
 // en vez de duplicarla.
 
public class TurnoResolutor {

    private final ITurnoRepository turnoRepository;
    private final IPacienteRepository pacienteRepository;
    private final IOdontologoRepository odontologoRepository;
    private final ISecretariaRepository secretariaRepository;

    public TurnoResolutor(ITurnoRepository turnoRepository,
                          IPacienteRepository pacienteRepository,
                          IOdontologoRepository odontologoRepository,
                          ISecretariaRepository secretariaRepository) {
        this.turnoRepository = turnoRepository;
        this.pacienteRepository = pacienteRepository;
        this.odontologoRepository = odontologoRepository;
        this.secretariaRepository = secretariaRepository;
    }

    public Paciente obtenerPaciente(Long idPaciente) {
        ValidacionesClinica.validarIdPacientePositivo(idPaciente);
        Paciente paciente = pacienteRepository.buscarPorId(idPaciente);
        if (paciente == null) {
            throw new PacienteNoEncontradoException("No existe un paciente con ID " + idPaciente + ".");
        }
        return paciente;
    }

    public Odontologo obtenerOdontologo(Long idOdontologo) {
        ValidacionesClinica.validarIdOdontologoPositivo(idOdontologo);
        Odontologo odontologo = odontologoRepository.buscarPorId(idOdontologo);
        if (odontologo == null) {
            throw new OdontologoNoEncontradoException("No existe un odontologo con ID " + idOdontologo + ".");
        }
        return odontologo;
    }

    public Secretaria obtenerSecretaria(Long idSecretaria) {
        ValidacionesClinica.validarIdSecretariaPositivo(idSecretaria);
        Secretaria secretaria = secretariaRepository.buscarPorId(idSecretaria);
        if (secretaria == null) {
            throw new DatoInvalidoException("No existe una secretaria con ID " + idSecretaria + ".");
        }
        return secretaria;
    }

    public Turno obtenerTurno(Long idTurno) {
        ValidacionesClinica.validarIdTurnoPositivo(idTurno);
        Turno turno = turnoRepository.buscarPorId(idTurno);
        if (turno == null) {
            throw new DatoInvalidoException("No existe un turno con ID " + idTurno + ".");
        }
        return turno;
    }
}
