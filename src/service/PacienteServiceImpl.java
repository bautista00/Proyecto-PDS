package service;

import entity.Domicilio;
import entity.EstadoTurno;
import entity.Paciente;
import entity.Turno;
import exception.DatoInvalidoException;
import exception.PacienteNoEncontradoException;
import repository.PacienteRepository;

import java.util.List;
import java.util.stream.Collectors;

public class PacienteServiceImpl implements IService<Paciente> {

    private PacienteRepository pacienteRepository;

    public PacienteServiceImpl(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    public Paciente registrar(Paciente paciente) {
        validarPaciente(paciente);

        if (pacienteRepository.existeDni(paciente.getDni())) {
            throw new DatoInvalidoException("Ya existe un paciente con el DNI " + paciente.getDni() + ".");
        }

        pacienteRepository.guardar(paciente);
        return paciente;
    }

    @Override
    public Paciente buscarPorId(Long id) {
        ValidacionesClinica.validarIdPacientePositivo(id);

        Paciente paciente = pacienteRepository.buscarPorId(id);
        if (paciente == null) {
            throw new PacienteNoEncontradoException("No existe un paciente con ID " + id + ".");
        }

        return paciente;
    }

    public Paciente buscarPorDni(Integer dni) {
        ValidacionesClinica.validarDniPositivo(dni);

        Paciente paciente = pacienteRepository.buscarPorDni(dni);
        if (paciente == null) {
            throw new PacienteNoEncontradoException("No existe un paciente con DNI " + dni + ".");
        }

        return paciente;
    }

    @Override
    public List<Paciente> listarTodos() {
        return pacienteRepository.listarTodos();
    }

    public List<Paciente> listarOrdenadosPorApellido() {
        return pacienteRepository.listarTodos().stream()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public Paciente actualizar(Paciente paciente) {
        validarPaciente(paciente);

        Paciente pacienteExistente = pacienteRepository.buscarPorId(paciente.getId());
        if (pacienteExistente == null) {
            throw new PacienteNoEncontradoException("No existe un paciente con ID " + paciente.getId() + ".");
        }

        Paciente pacienteConMismoDni = pacienteRepository.buscarPorDni(paciente.getDni());
        if (pacienteConMismoDni != null && !pacienteConMismoDni.getId().equals(paciente.getId())) {
            throw new DatoInvalidoException("Ya existe otro paciente con el DNI " + paciente.getDni() + ".");
        }

        pacienteRepository.actualizar(paciente);
        return paciente;
    }

    @Override
    public boolean eliminar(Long id) {
        ValidacionesClinica.validarIdPacientePositivo(id);

        Paciente paciente = pacienteRepository.buscarPorId(id);
        if (paciente == null) {
            throw new PacienteNoEncontradoException("No existe un paciente con ID " + id + ".");
        }

        if (TurnoHistorialUtil.tieneTurnosFuturos(paciente.getHistorialPaciente())) {
            throw new DatoInvalidoException(
                    "No se puede eliminar el paciente porque tiene turnos a futuro. " +
                    "Cancele o complete esos turnos antes de eliminarlo.");
        }

        pacienteRepository.eliminar(id);
        return true;
    }

    private void validarPaciente(Paciente paciente) {
        if (paciente == null) {
            throw new DatoInvalidoException("El paciente no puede ser nulo.");
        }
        ValidacionesClinica.validarNombreNoVacio(paciente.getNombre());
        ValidacionesClinica.validarNombreSoloLetras(paciente.getNombre());
        ValidacionesClinica.validarApellidoNoVacio(paciente.getApellido());
        ValidacionesClinica.validarApellidoSoloLetras(paciente.getApellido());
        ValidacionesClinica.validarDniPositivo(paciente.getDni());
        ValidacionesClinica.validarEmailNoVacio(paciente.getEmail());
        if (paciente.getObraSocial() == null) {
            throw new DatoInvalidoException("Debe indicarse si el paciente tiene obra social.");
        }
        validarDomicilio(paciente.getDomicilio());
    }

    private void validarDomicilio(Domicilio domicilio) {
        if (domicilio == null) {
            throw new DatoInvalidoException("El domicilio no puede ser nulo.");
        }
        ValidacionesClinica.validarDomicilioCalleNoVacia(domicilio.getCalle());
        ValidacionesClinica.validarDomicilioNumeroPositivo(domicilio.getNumero());
        ValidacionesClinica.validarDomicilioLocalidadNoVacia(domicilio.getLocalidad());
        ValidacionesClinica.validarDomicilioProvinciaNoVacia(domicilio.getProvincia());
    }
}