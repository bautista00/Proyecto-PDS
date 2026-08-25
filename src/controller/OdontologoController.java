package controller;

import entity.Odontologo;
import service.OdontologoServiceImpl;

import java.util.List;

public class OdontologoController {

    private OdontologoServiceImpl odontologoService;

    public OdontologoController(OdontologoServiceImpl odontologoService) {
        this.odontologoService = odontologoService;
    }

    public Odontologo registrarOdontologo(Odontologo odontologo) {
        return odontologoService.registrar(odontologo);
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

    public Odontologo actualizarOdontologo(Long id,
                                           String nombre,
                                           String apellido,
                                           Integer dni,
                                           String matricula) {

        Odontologo odontologoExistente = odontologoService.buscarPorId(id);

        odontologoExistente.setNombre(nombre);
        odontologoExistente.setApellido(apellido);
        odontologoExistente.setDni(dni);
        odontologoExistente.setMatricula(matricula);

        return odontologoService.actualizar(odontologoExistente);
    }

    public boolean eliminarOdontologo(Long id) {
        return odontologoService.eliminar(id);
    }
}