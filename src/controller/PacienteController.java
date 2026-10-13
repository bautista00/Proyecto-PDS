package controller;

import dto.PacienteDatos;
import dto.PacienteEdicion;
import dto.PacienteRegistro;
import entity.CoberturaPaciente;
import entity.Paciente;
import exception.DatoInvalidoException;
import service.PacienteService;
import service.ValidacionesClinica;

import java.util.List;
import java.util.stream.Collectors;

//Controller para manejar las operaciones relacionadas con los Pacientes.
// Valida los campos que llegan de la vista, delega la lógica de negocio en el servicio
// y devuelve PacienteDatos para que la vista no reciba entidades del modelo (MVC).

public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    public PacienteDatos registrarPaciente(PacienteRegistro datos) {
        validarDatos(datos);
        return aDatos(pacienteService.registrar(datos));
    }

    public PacienteDatos buscarPacientePorId(Long id) {
        return aDatos(pacienteService.buscarPorId(id));
    }

    public PacienteDatos buscarPacientePorDni(Integer dni) {
        return aDatos(pacienteService.buscarPorDni(dni));
    }

    public List<PacienteDatos> listarPacientesOrdenadosPorApellido() {
        return pacienteService.listarOrdenadosPorApellido().stream()
                .map(this::aDatos)
                .collect(Collectors.toList());
    }

    public List<String> listarCoberturas() {
        return pacienteService.listarCoberturas().stream()
                .map(CoberturaPaciente::getDescripcion)
                .collect(Collectors.toList());
    }

    public PacienteDatos actualizarPaciente(PacienteEdicion edicion) {
        if (edicion == null) {
            throw new DatoInvalidoException("Los datos de edicion del paciente no pueden ser nulos.");
        }
        validarDatos(edicion.getDatos());
        return aDatos(pacienteService.actualizar(edicion));
    }

    public boolean eliminarPaciente(Long id) {
        return pacienteService.eliminar(id);
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
        ValidacionesClinica.validarTextoNoVacio(
                datos.getCobertura(), "Debe indicarse la cobertura del paciente.");
        ValidacionesClinica.validarDomicilioCalleNoVacia(datos.getCalle());
        ValidacionesClinica.validarDomicilioNumeroPositivo(datos.getNumero());
        ValidacionesClinica.validarDomicilioLocalidadNoVacia(datos.getLocalidad());
        ValidacionesClinica.validarDomicilioProvinciaNoVacia(datos.getProvincia());
    }

    private PacienteDatos aDatos(Paciente paciente) {
        PacienteDatos datos = new PacienteDatos();
        datos.setId(paciente.getId());
        datos.setNombre(paciente.getNombre());
        datos.setApellido(paciente.getApellido());
        datos.setDni(paciente.getDni());
        datos.setEmail(paciente.getEmail());
        datos.setCalle(paciente.getCalleDomicilio());
        datos.setNumero(paciente.getNumeroDomicilio());
        datos.setLocalidad(paciente.getLocalidadDomicilio());
        datos.setProvincia(paciente.getProvinciaDomicilio());
        datos.setCobertura(paciente.getCobertura().getDescripcion());
        return datos;
    }
}
