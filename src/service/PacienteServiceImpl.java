package service;

import dto.PacienteEdicion;
import dto.PacienteRegistro;
import entity.Domicilio;
import entity.Paciente;
import exception.DatoInvalidoException;
import exception.PacienteNoEncontradoException;
import repository.IPacienteRepository;

import java.util.List;
import java.util.stream.Collectors;

// Implementación del servicio de pacientes, que se encarga de la lógica de negocio relacionada con los pacientes.
// Tiene la responsabilidad de registrar, buscar, listar, actualizar y eliminar pacientes, validando los datos y
//  las reglas de negocio correspondientes.
// Utiliza el repositorio de pacientes para persistir y recuperar los datos de los pacientes.
// Utiliza ValidacionesClinica para validar los datos de los pacientes antes de registrarlos o actualizarlos.


public class PacienteServiceImpl implements PacienteService {

    private final IPacienteRepository pacienteRepository;

    public PacienteServiceImpl(IPacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    public Paciente registrar(PacienteRegistro datos) {
        validarDatos(datos);
        if (pacienteRepository.existeDni(datos.getDni())) {
            throw new DatoInvalidoException("Ya existe un paciente con el DNI " + datos.getDni() + ".");
        }
        Paciente paciente = new Paciente(
                datos.getNombre(), datos.getApellido(), datos.getDni(), datos.getEmail(),
                crearDomicilio(datos), datos.getCobertura());
        pacienteRepository.guardar(paciente);
        return paciente;
    }

    private Paciente buscarPorId(Long id) {
        ValidacionesClinica.validarIdPacientePositivo(id);
        Paciente paciente = pacienteRepository.buscarPorId(id);
        if (paciente == null) {
            throw new PacienteNoEncontradoException("No existe un paciente con ID " + id + ".");
        }
        return paciente;
    }

    @Override
    public PacienteRegistro buscarDatosPorId(Long id) {
        return crearDatosPaciente(buscarPorId(id));
    }

    @Override
    public Paciente buscarPorDni(Integer dni) {
        ValidacionesClinica.validarDniPositivo(dni);
        Paciente paciente = pacienteRepository.buscarPorDni(dni);
        if (paciente == null) {
            throw new PacienteNoEncontradoException("No existe un paciente con DNI " + dni + ".");
        }
        return paciente;
    }

    @Override
    public List<Paciente> listarOrdenadosPorApellido() {
        return pacienteRepository.listarTodos().stream().sorted().collect(Collectors.toList());
    }

    @Override
    public Paciente actualizar(PacienteEdicion edicion) {
        if (edicion == null) {
            throw new DatoInvalidoException("Los datos de edicion del paciente no pueden ser nulos.");
        }
        PacienteRegistro datos = edicion.getDatos();
        validarDatos(datos);
        Paciente paciente = buscarPorId(edicion.getIdPaciente());
        Paciente mismoDni = pacienteRepository.buscarPorDni(datos.getDni());
        if (mismoDni != null && !mismoDni.getId().equals(paciente.getId())) {
            throw new DatoInvalidoException("Ya existe otro paciente con el DNI " + datos.getDni() + ".");
        }

        paciente.actualizarDatos(
                datos.getNombre(), datos.getApellido(), datos.getDni(), datos.getEmail(),
                crearDomicilio(datos), datos.getCobertura());
        pacienteRepository.actualizar(paciente);
        return paciente;
    }

    @Override
    public boolean eliminar(Long id) {
        Paciente paciente = buscarPorId(id);
        if (paciente.tieneTurnos()) {
            throw new DatoInvalidoException(
                    "No se puede eliminar el paciente porque posee turnos asociados. " +
                    "El historial clinico debe conservarse.");
        }
        pacienteRepository.eliminar(id);
        return true;
    }

    private void validarDatos(PacienteRegistro datos) {
        if (datos == null) {
            throw new DatoInvalidoException("Los datos del paciente no pueden ser nulos.");
        }
        ValidacionesClinica.validarNombreNoVacio(datos.getNombre());
        ValidacionesClinica.validarNombreSoloLetras(datos.getNombre());
        ValidacionesClinica.validarApellidoNoVacio(datos.getApellido());
        ValidacionesClinica.validarApellidoSoloLetras(datos.getApellido());
        ValidacionesClinica.validarDniPositivo(datos.getDni());
        ValidacionesClinica.validarEmailNoVacio(datos.getEmail());
        if (datos.getCobertura() == null) {
            throw new DatoInvalidoException("Debe indicarse la cobertura del paciente.");
        }
        ValidacionesClinica.validarDomicilioCalleNoVacia(datos.getCalle());
        ValidacionesClinica.validarDomicilioNumeroPositivo(datos.getNumero());
        ValidacionesClinica.validarDomicilioLocalidadNoVacia(datos.getLocalidad());
        ValidacionesClinica.validarDomicilioProvinciaNoVacia(datos.getProvincia());
    }

    private Domicilio crearDomicilio(PacienteRegistro datos) {
        return new Domicilio(datos.getCalle(), datos.getNumero(), datos.getLocalidad(), datos.getProvincia());
    }

    private PacienteRegistro crearDatosPaciente(Paciente paciente) {
        PacienteRegistro datos = new PacienteRegistro();
        datos.setNombre(paciente.getNombre());
        datos.setApellido(paciente.getApellido());
        datos.setDni(paciente.getDni());
        datos.setEmail(paciente.getEmail());
        datos.setCalle(paciente.getCalleDomicilio());
        datos.setNumero(paciente.getNumeroDomicilio());
        datos.setLocalidad(paciente.getLocalidadDomicilio());
        datos.setProvincia(paciente.getProvinciaDomicilio());
        datos.setCobertura(paciente.getCobertura());
        return datos;
    }
}
