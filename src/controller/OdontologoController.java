package controller;

import dto.OdontologoDatos;
import dto.OdontologoEdicion;
import dto.OdontologoRegistro;
import entity.EspecialidadOdontologica;
import entity.Odontologo;
import exception.DatoInvalidoException;
import service.OdontologoService;
import service.ValidacionesClinica;

import java.util.List;
import java.util.stream.Collectors;

//Controller para manejar las operaciones relacionadas con los odontólogos.
// Valida los campos que llegan de la vista, delega la lógica de negocio en el servicio
// y devuelve OdontologoDatos para que la vista no reciba entidades del modelo (MVC).

public class OdontologoController {

    private final OdontologoService odontologoService;

    public OdontologoController(OdontologoService odontologoService) {
        this.odontologoService = odontologoService;
    }

    public OdontologoDatos registrarOdontologo(OdontologoRegistro datos) {
        validarDatos(datos);
        return aDatos(odontologoService.registrar(datos));
    }

    public OdontologoDatos buscarOdontologoPorId(Long id) {
        return aDatos(odontologoService.buscarPorId(id));
    }

    public OdontologoDatos buscarOdontologoPorMatricula(String matricula) {
        return aDatos(odontologoService.buscarPorMatricula(matricula));
    }

    public List<OdontologoDatos> listarOdontologos() {
        return odontologoService.listarTodos().stream()
                .map(this::aDatos)
                .collect(Collectors.toList());
    }

    public List<String> listarEspecialidades() {
        return odontologoService.listarEspecialidades().stream()
                .map(EspecialidadOdontologica::getDescripcion)
                .collect(Collectors.toList());
    }

    public OdontologoDatos actualizarOdontologo(OdontologoEdicion edicion) {
        if (edicion == null) {
            throw new DatoInvalidoException("Los datos de edicion del odontologo no pueden ser nulos.");
        }
        validarDatos(edicion.getDatos());
        return aDatos(odontologoService.actualizar(edicion));
    }

    public boolean eliminarOdontologo(Long id) {
        return odontologoService.eliminar(id);
    }

    private void validarDatos(OdontologoRegistro datos) {
        if (datos == null) {
            throw new DatoInvalidoException("Los datos del odontologo no pueden ser nulos.");
        }
        ValidacionesClinica.validarNombreNoVacio(datos.getNombre());
        ValidacionesClinica.validarNombreSoloLetras(datos.getNombre());
        ValidacionesClinica.validarApellidoNoVacio(datos.getApellido());
        ValidacionesClinica.validarApellidoSoloLetras(datos.getApellido());
        ValidacionesClinica.validarDniPositivo(datos.getDni());
        ValidacionesClinica.validarMatriculaNoVacia(datos.getMatricula());
        ValidacionesClinica.validarTextoNoVacio(
                datos.getEspecialidad(), "La especialidad no puede ser nula.");
    }

    private OdontologoDatos aDatos(Odontologo odontologo) {
        return new OdontologoDatos(
                odontologo.getId(),
                odontologo.getNombre(),
                odontologo.getApellido(),
                odontologo.getDni(),
                odontologo.getMatricula(),
                odontologo.getEspecialidad().getDescripcion(),
                odontologo.getTarifaBase());
    }
}
