package service;

import entity.EstadoTurno;
import entity.Odontologo;
import entity.Turno;
import exception.DatoInvalidoException;
import exception.OdontologoNoEncontradoException;
import repository.OdontologoRepository;

import java.util.List;

public class OdontologoServiceImpl implements IService<Odontologo> {

    private OdontologoRepository odontologoRepository;

    public OdontologoServiceImpl(OdontologoRepository odontologoRepository) {
        this.odontologoRepository = odontologoRepository;
    }

    @Override
    public Odontologo registrar(Odontologo odontologo) {
        validarOdontologo(odontologo);

        if (odontologoRepository.existeMatricula(odontologo.getMatricula())) {
            throw new DatoInvalidoException("Ya existe un odontologo con la matricula " + odontologo.getMatricula() + ".");
        }

        odontologoRepository.guardar(odontologo);
        return odontologo;
    }

    @Override
    public Odontologo buscarPorId(Long id) {
        ValidacionesClinica.validarIdOdontologoPositivo(id);

        Odontologo odontologo = odontologoRepository.buscarPorId(id);
        if (odontologo == null) {
            throw new OdontologoNoEncontradoException("No existe un odontologo con ID " + id + ".");
        }

        return odontologo;
    }

    public Odontologo buscarPorMatricula(String matricula) {
        ValidacionesClinica.validarMatriculaNoVacia(matricula);

        Odontologo odontologo = odontologoRepository.buscarPorMatricula(matricula);
        if (odontologo == null) {
            throw new OdontologoNoEncontradoException("No existe un odontologo con matricula " + matricula + ".");
        }

        return odontologo;
    }

    @Override
    public List<Odontologo> listarTodos() {
        return odontologoRepository.listarTodos();
    }

    @Override
    public Odontologo actualizar(Odontologo odontologo) {
        validarOdontologo(odontologo);

        Odontologo odontologoExistente = odontologoRepository.buscarPorId(odontologo.getId());
        if (odontologoExistente == null) {
            throw new OdontologoNoEncontradoException("No existe un odontologo con ID " + odontologo.getId() + ".");
        }

        Odontologo odontologoConMismaMatricula = odontologoRepository.buscarPorMatricula(odontologo.getMatricula());
        if (odontologoConMismaMatricula != null && !odontologoConMismaMatricula.getId().equals(odontologo.getId())) {
            throw new DatoInvalidoException("Ya existe otro odontologo con la matricula " + odontologo.getMatricula() + ".");
        }

        odontologoRepository.actualizar(odontologo);
        return odontologo;
    }

    @Override
    public boolean eliminar(Long id) {
        ValidacionesClinica.validarIdOdontologoPositivo(id);

        Odontologo odontologo = odontologoRepository.buscarPorId(id);
        if (odontologo == null) {
            throw new OdontologoNoEncontradoException("No existe un odontologo con ID " + id + ".");
        }

        if (TurnoHistorialUtil.tieneTurnosFuturos(odontologo.getHistorialOdontologo())) {
            throw new DatoInvalidoException(
                    "No se puede eliminar el odontologo porque tiene turnos a futuro. " +
                    "Cancele o complete esos turnos antes de eliminarlo.");
        }

        odontologoRepository.eliminar(id);
        return true;
    }

    private void validarOdontologo(Odontologo odontologo) {
        if (odontologo == null) {
            throw new DatoInvalidoException("El odontologo no puede ser nulo.");
        }
        ValidacionesClinica.validarNombreNoVacio(odontologo.getNombre());
        ValidacionesClinica.validarNombreSoloLetras(odontologo.getNombre());
        ValidacionesClinica.validarApellidoNoVacio(odontologo.getApellido());
        ValidacionesClinica.validarApellidoSoloLetras(odontologo.getApellido());
        ValidacionesClinica.validarDniPositivo(odontologo.getDni());
        ValidacionesClinica.validarMatriculaNoVacia(odontologo.getMatricula());
    }
}