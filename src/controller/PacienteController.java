package controller;

import entity.Domicilio;
import entity.Paciente;
import service.PacienteServiceImpl;

import java.util.List;

public class PacienteController {

    private PacienteServiceImpl pacienteService;

    public PacienteController(PacienteServiceImpl pacienteService) {
        this.pacienteService = pacienteService;
    }

    public Paciente registrarPaciente(String nombre,
                                      String apellido,
                                      Integer dni,
                                      String email,
                                      String calle,
                                      Integer numero,
                                      String localidad,
                                      String provincia,
                                      Boolean obraSocial) {

        Domicilio domicilio = new Domicilio(calle, numero, localidad, provincia);
        Paciente paciente = new Paciente(nombre, apellido, dni, email, domicilio, obraSocial);

        return pacienteService.registrar(paciente);
    }

    public Paciente buscarPacientePorId(Long id) {
        return pacienteService.buscarPorId(id);
    }

    public Paciente buscarPacientePorDni(Integer dni) {
        return pacienteService.buscarPorDni(dni);
    }

    public List<Paciente> listarPacientes() {
        return pacienteService.listarTodos();
    }

    public List<Paciente> listarPacientesOrdenadosPorApellido() {
        return pacienteService.listarOrdenadosPorApellido();
    }

    public Paciente actualizarPaciente(Long id,
                                       String nombre,
                                       String apellido,
                                       Integer dni,
                                       String email,
                                       String calle,
                                       Integer numero,
                                       String localidad,
                                       String provincia,
                                       Boolean obraSocial) {

        Paciente pacienteExistente = pacienteService.buscarPorId(id);

        pacienteExistente.setNombre(nombre);
        pacienteExistente.setApellido(apellido);
        pacienteExistente.setDni(dni);
        pacienteExistente.setEmail(email);
        pacienteExistente.setDomicilio(new Domicilio(calle, numero, localidad, provincia));
        pacienteExistente.setObraSocial(obraSocial);

        return pacienteService.actualizar(pacienteExistente);
    }

    public boolean eliminarPaciente(Long id) {
        return pacienteService.eliminar(id);
    }
}