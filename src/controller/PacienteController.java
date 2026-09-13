package controller;

import dto.PacienteEdicion;
import dto.PacienteRegistro;
import entity.Domicilio;
import entity.Paciente;
import exception.DatoInvalidoException;
import service.PacienteServiceImpl;

import java.util.List;

public class PacienteController {

    private final PacienteServiceImpl pacienteService;

    public PacienteController(PacienteServiceImpl pacienteService) {
        this.pacienteService = pacienteService;
    }

    public Paciente registrarPaciente(PacienteRegistro datos) {
        validarDatosPaciente(datos);
        Paciente paciente = crearPaciente(datos);
        return pacienteService.registrar(paciente);
    }

    public PacienteRegistro buscarDatosPacientePorId(Long id) {
        Paciente paciente = pacienteService.buscarPorId(id);
        return crearDatosPaciente(paciente);
    }

    public Paciente buscarPacientePorDni(Integer dni) {
        return pacienteService.buscarPorDni(dni);
    }

    public List<Paciente> listarPacientesOrdenadosPorApellido() {
        return pacienteService.listarOrdenadosPorApellido();
    }

    public Paciente actualizarPaciente(PacienteEdicion edicion) {
        if (edicion == null) {
            throw new DatoInvalidoException("Los datos de edicion del paciente no pueden ser nulos.");
        }
        PacienteRegistro datos = edicion.getDatos();
        validarDatosPaciente(datos);
        Paciente pacienteExistente = pacienteService.buscarPorId(edicion.getIdPaciente());

        pacienteExistente.setNombre(datos.getNombre());
        pacienteExistente.setApellido(datos.getApellido());
        pacienteExistente.setDni(datos.getDni());
        pacienteExistente.setEmail(datos.getEmail());
        pacienteExistente.setDomicilio(crearDomicilio(datos));
        pacienteExistente.setCobertura(datos.getCobertura());

        return pacienteService.actualizar(pacienteExistente);
    }

    public boolean eliminarPaciente(Long id) {
        return pacienteService.eliminar(id);
    }

    private Paciente crearPaciente(PacienteRegistro datos) {
        return new Paciente(
                datos.getNombre(),
                datos.getApellido(),
                datos.getDni(),
                datos.getEmail(),
                crearDomicilio(datos),
                datos.getCobertura());
    }

    private Domicilio crearDomicilio(PacienteRegistro datos) {
        return new Domicilio(
                datos.getCalle(),
                datos.getNumero(),
                datos.getLocalidad(),
                datos.getProvincia());
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
    private void validarDatosPaciente(PacienteRegistro datos) {
        if (datos == null) {
            throw new DatoInvalidoException("Los datos del paciente no pueden ser nulos.");
        }
    }

}
