package service;

import dto.PacienteEdicion;
import dto.PacienteRegistro;
import entity.CatalogoCoberturas;
import entity.CoberturaPaciente;
import entity.Domicilio;
import entity.Paciente;
import exception.DatoInvalidoException;
import exception.PacienteNoEncontradoException;
import repository.IPacienteRepository;

import java.util.List;
import java.util.stream.Collectors;

// Implementación del servicio de pacientes, que se encarga de la lógica de negocio relacionada con los pacientes.
// Tiene la responsabilidad de registrar, buscar, listar, actualizar y eliminar pacientes, aplicando
//  las reglas de negocio correspondientes (los campos ya llegan validados por PacienteController).
// Utiliza el repositorio de pacientes para persistir y recuperar los datos de los pacientes.
// Utiliza el catálogo de coberturas para convertir la cobertura elegida en la vista.


public class PacienteServiceImpl implements PacienteService {

    private final IPacienteRepository pacienteRepository;
    private final CatalogoCoberturas catalogoCoberturas;

    public PacienteServiceImpl(IPacienteRepository pacienteRepository,
                               CatalogoCoberturas catalogoCoberturas) {
        this.pacienteRepository = pacienteRepository;
        this.catalogoCoberturas = catalogoCoberturas;
    }

    @Override
    public Paciente registrar(PacienteRegistro datos) {
        if (pacienteRepository.existeDni(datos.getDni())) {
            throw new DatoInvalidoException("Ya existe un paciente con el DNI " + datos.getDni() + ".");
        }
        Paciente paciente = new Paciente(
                datos.getNombre(), datos.getApellido(), datos.getDni(), datos.getEmail(),
                crearDomicilio(datos), catalogoCoberturas.buscar(datos.getCobertura()));
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
    public List<CoberturaPaciente> listarCoberturas() {
        return catalogoCoberturas.listar();
    }

    @Override
    public Paciente actualizar(PacienteEdicion edicion) {
        PacienteRegistro datos = edicion.getDatos();
        Paciente paciente = buscarPorId(edicion.getIdPaciente());
        Paciente mismoDni = pacienteRepository.buscarPorDni(datos.getDni());
        if (mismoDni != null && !mismoDni.getId().equals(paciente.getId())) {
            throw new DatoInvalidoException("Ya existe otro paciente con el DNI " + datos.getDni() + ".");
        }

        paciente.actualizarDatos(
                datos.getNombre(), datos.getApellido(), datos.getDni(), datos.getEmail(),
                crearDomicilio(datos), catalogoCoberturas.buscar(datos.getCobertura()));
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

    private Domicilio crearDomicilio(PacienteRegistro datos) {
        return new Domicilio(datos.getCalle(), datos.getNumero(), datos.getLocalidad(), datos.getProvincia());
    }
}
