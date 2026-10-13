package controller;

import dto.SecretariaDatos;
import dto.SecretariaEdicion;
import dto.SecretariaRegistro;
import entity.Secretaria;
import exception.DatoInvalidoException;
import service.SecretariaService;
import service.ValidacionesClinica;

import java.util.List;
import java.util.stream.Collectors;


//Controller para manejar las operaciones relacionadas con las secretarias.
// Valida los campos que llegan de la vista, delega la lógica de negocio en el servicio
// y devuelve SecretariaDatos para que la vista no reciba entidades del modelo (MVC).

public class SecretariaController {

    private final SecretariaService secretariaService;

    public SecretariaController(SecretariaService secretariaService) {
        this.secretariaService = secretariaService;
    }

    public SecretariaDatos registrarSecretaria(SecretariaRegistro datos) {
        validarDatos(datos);
        return aDatos(secretariaService.registrar(datos));
    }

    public SecretariaDatos buscarSecretariaPorId(Long id) {
        return aDatos(secretariaService.buscarPorId(id));
    }

    public SecretariaDatos buscarSecretariaPorDni(Integer dni) {
        return aDatos(secretariaService.buscarPorDni(dni));
    }

    public List<SecretariaDatos> listarSecretarias() {
        return secretariaService.listarTodos().stream()
                .map(this::aDatos)
                .collect(Collectors.toList());
    }

    public SecretariaDatos actualizarSecretaria(SecretariaEdicion edicion) {
        if (edicion == null) {
            throw new DatoInvalidoException("Los datos de edicion de la secretaria no pueden ser nulos.");
        }
        validarDatos(edicion.getDatos());
        return aDatos(secretariaService.actualizar(edicion));
    }

    public boolean eliminarSecretaria(Long id) {
        return secretariaService.eliminar(id);
    }

    private void validarDatos(SecretariaRegistro datos) {
        if (datos == null) {
            throw new DatoInvalidoException("Los datos de la secretaria no pueden ser nulos.");
        }
        ValidacionesClinica.validarNombreNoVacio(datos.getNombre());
        ValidacionesClinica.validarNombreSoloLetras(datos.getNombre());
        ValidacionesClinica.validarApellidoNoVacio(datos.getApellido());
        ValidacionesClinica.validarApellidoSoloLetras(datos.getApellido());
        ValidacionesClinica.validarDniPositivo(datos.getDni());
    }

    private SecretariaDatos aDatos(Secretaria secretaria) {
        return new SecretariaDatos(
                secretaria.getId(),
                secretaria.getNombre(),
                secretaria.getApellido(),
                secretaria.getDni());
    }
}
