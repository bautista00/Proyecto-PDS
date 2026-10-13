package service;

import dto.OdontologoEdicion;
import dto.OdontologoRegistro;
import entity.EspecialidadOdontologica;
import entity.Odontologo;
import entity.OdontologoFactory;
import exception.DatoInvalidoException;
import exception.OdontologoNoEncontradoException;
import repository.IOdontologoRepository;

import java.util.List;


// Implementación del servicio de odontólogos, que se encarga de la lógica de negocio relacionada con los odontólogos.
// Tiene la responsabilidad de registrar, buscar, listar, actualizar y eliminar odontólogos, aplicando
// las reglas de negocio correspondientes (los campos ya llegan validados por OdontologoController).
// Utiliza el repositorio de odontólogos para persistir y recuperar los datos de los odontólogos.
// Utiliza la fábrica para crear la subclase y convertir la especialidad elegida en la vista.


public class OdontologoServiceImpl implements OdontologoService {

    private final IOdontologoRepository odontologoRepository;
    private final OdontologoFactory odontologoFactory;

    public OdontologoServiceImpl(IOdontologoRepository odontologoRepository,
                                 OdontologoFactory odontologoFactory) {
        this.odontologoRepository = odontologoRepository;
        this.odontologoFactory = odontologoFactory;
    }

    @Override
    public Odontologo registrar(OdontologoRegistro datos) {
        if (odontologoRepository.existeMatricula(datos.getMatricula())) {
            throw new DatoInvalidoException(
                    "Ya existe un odontologo con la matricula " + datos.getMatricula() + ".");
        }
        Odontologo odontologo = odontologoFactory.crear(
                odontologoFactory.buscarEspecialidad(datos.getEspecialidad()), datos.getNombre(), datos.getApellido(),
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
    public List<EspecialidadOdontologica> listarEspecialidades() {
        return odontologoFactory.listarEspecialidades();
    }

    @Override
    public Odontologo actualizar(OdontologoEdicion edicion) {
        OdontologoRegistro datos = edicion.getDatos();
        Odontologo odontologo = buscarPorId(edicion.getIdOdontologo());
        EspecialidadOdontologica especialidad = odontologoFactory.buscarEspecialidad(datos.getEspecialidad());
        if (!odontologo.getEspecialidad().equals(especialidad)) {
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
}
