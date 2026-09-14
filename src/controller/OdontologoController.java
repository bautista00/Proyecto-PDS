package controller;

import dto.OdontologoEdicion;
import dto.OdontologoRegistro;
import entity.Odontologo;
import service.OdontologoService;

import java.util.List;

//Controller para manejar las operaciones relacionadas con los odontólogos.
// No se encarga de la lógica de negocio, sino que delega las operaciones al servicio correspondiente.

public class OdontologoController {

    private final OdontologoService odontologoService;

    public OdontologoController(OdontologoService odontologoService) {
        this.odontologoService = odontologoService;
    }

    public Odontologo registrarOdontologo(OdontologoRegistro datos) {
        return odontologoService.registrar(datos);
    }

    public Odontologo buscarOdontologoPorId(Long id) {
        return odontologoService.buscarPorId(id);
    }

    public Odontologo buscarOdontologoPorMatricula(String matricula) {
        return odontologoService.buscarPorMatricula(matricula);
    }

    public List<Odontologo> listarOdontologos() {
        return odontologoService.listarTodos();
    }

    public Odontologo actualizarOdontologo(OdontologoEdicion edicion) {
        return odontologoService.actualizar(edicion);
    }

    public boolean eliminarOdontologo(Long id) {
        return odontologoService.eliminar(id);
    }
}
