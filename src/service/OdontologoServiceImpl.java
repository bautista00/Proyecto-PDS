package service;

import dto.OdontologoEdicion;
import dto.OdontologoRegistro;
import entity.Odontologo;
import entity.OdontologoFactory;
import exception.DatoInvalidoException;
import exception.OdontologoNoEncontradoException;
import repository.IOdontologoRepository;

import java.util.List;


// Implementación del servicio de odontólogos, que se encarga de la lógica de negocio relacionada con los odontólogos.
// Tiene la responsabilidad de registrar, buscar, listar, actualizar y eliminar odontólogos, validando los datos 
// y las reglas de negocio correspondientes.
// Utiliza el repositorio de odontólogos para persistir y recuperar los datos de los odontólogos.
// Utiliza ValidacionesClinica para validar los datos de los odontólogos antes de registrarlos o actualizarlos.


public class OdontologoServiceImpl implements OdontologoService {

    private final IOdontologoRepository odontologoRepository;

    public OdontologoServiceImpl(IOdontologoRepository odontologoRepository) {
        this.odontologoRepository = odontologoRepository;
    }

    @Override
    public Odontologo registrar(OdontologoRegistro datos) {
        validarDatos(datos);
        if (odontologoRepository.existeMatricula(datos.getMatricula())) {
            throw new DatoInvalidoException(
                    "Ya existe un odontologo con la matricula " + datos.getMatricula() + ".");
        }
        Odontologo odontologo = OdontologoFactory.crear(
                datos.getEspecialidad(), datos.getNombre(), datos.getApellido(),
                datos.getDni(), datos.getMatricula());
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

    @Override
    public Odontologo buscarPorMatricula(String matricula) {
        ValidacionesClinica.validarMatriculaNoVacia(matricula);
        Odontologo odontologo = odontologoRepository.buscarPorMatricula(matricula);
        if (odontologo == null) {
            throw new OdontologoNoEncontradoException(
                    "No existe un odontologo con matricula " + matricula + ".");
        }
        return odontologo;
    }

    @Override
    public List<Odontologo> listarTodos() {
        return odontologoRepository.listarTodos();
    }

    @Override
    public Odontologo actualizar(OdontologoEdicion edicion) {
        if (edicion == null) {
            throw new DatoInvalidoException("Los datos de edicion del odontologo no pueden ser nulos.");
        }
        OdontologoRegistro datos = edicion.getDatos();
        validarDatos(datos);
        Odontologo odontologo = buscarPorId(edicion.getIdOdontologo());
        if (odontologo.getEspecialidad() != datos.getEspecialidad()) {
            throw new DatoInvalidoException("La especialidad de un odontologo no puede modificarse.");
        }
        Odontologo mismaMatricula = odontologoRepository.buscarPorMatricula(datos.getMatricula());
        if (mismaMatricula != null && !mismaMatricula.getId().equals(odontologo.getId())) {
            throw new DatoInvalidoException(
                    "Ya existe otro odontologo con la matricula " + datos.getMatricula() + ".");
        }

        odontologo.actualizarDatos(
                datos.getNombre(), datos.getApellido(), datos.getDni(), datos.getMatricula());
        odontologoRepository.actualizar(odontologo);
        return odontologo;
    }

    @Override
    public boolean eliminar(Long id) {
        Odontologo odontologo = buscarPorId(id);
        if (odontologo.tieneTurnos()) {
            throw new DatoInvalidoException(
                    "No se puede eliminar el odontologo porque posee turnos asociados. " +
                    "El historial clinico debe conservarse.");
        }
        odontologoRepository.eliminar(id);
        return true;
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
        if (datos.getEspecialidad() == null) {
            throw new DatoInvalidoException("La especialidad no puede ser nula.");
        }
    }
}
