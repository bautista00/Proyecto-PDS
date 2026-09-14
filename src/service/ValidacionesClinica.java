package service;

import exception.DatoInvalidoException;


// Catálogo de validaciones primitivas reutilizables: IDs y números positivos,
// texto no vacío, solo letras. No contiene reglas de negocio.
// Las reglas propias del turno (fecha, hora, estado, odontólogo habilitado) viven en TurnoValidador.
 
public class ValidacionesClinica {

    private static final String SOLO_LETRAS_REGEX = "[A-Za-záéíóúÁÉÍÓÚñÑ ]+";

    private ValidacionesClinica() {
    }

    public static void validarIdPacientePositivo(Long idPaciente) {
        validarLongPositivo(idPaciente, "El ID del paciente debe ser un numero positivo.");
    }

    public static void validarIdOdontologoPositivo(Long idOdontologo) {
        validarLongPositivo(idOdontologo, "El ID del odontologo debe ser un numero positivo.");
    }

    public static void validarIdSecretariaPositivo(Long idSecretaria) {
        validarLongPositivo(idSecretaria, "El ID de la secretaria debe ser un numero positivo.");
    }

    public static void validarIdTurnoPositivo(Long idTurno) {
        validarLongPositivo(idTurno, "El ID del turno debe ser un numero positivo.");
    }

    public static void validarDniPositivo(Integer dni) {
        validarEnteroPositivo(dni, "El DNI debe ser un numero positivo.");
    }

    public static void validarMatriculaNoVacia(String matricula) {
        validarTextoNoVacio(matricula, "La matricula no puede estar vacia.");
    }

    public static void validarNombreNoVacio(String nombre) {
        validarTextoNoVacio(nombre, "El nombre no puede estar vacio.");
    }

    public static void validarNombreSoloLetras(String nombre) {
        validarSoloLetras(nombre, "El nombre no puede contener numeros.");
    }

    public static void validarApellidoNoVacio(String apellido) {
        validarTextoNoVacio(apellido, "El apellido no puede estar vacio.");
    }

    public static void validarApellidoSoloLetras(String apellido) {
        validarSoloLetras(apellido, "El apellido no puede contener numeros.");
    }

    public static void validarEmailNoVacio(String email) {
        validarTextoNoVacio(email, "El email no puede estar vacio.");
    }

    public static void validarDomicilioCalleNoVacia(String calle) {
        validarTextoNoVacio(calle, "La calle no puede estar vacia.");
    }

    public static void validarDomicilioNumeroPositivo(Integer numero) {
        validarEnteroPositivo(numero, "El numero del domicilio debe ser positivo.");
    }

    public static void validarDomicilioLocalidadNoVacia(String localidad) {
        validarTextoNoVacio(localidad, "La localidad no puede estar vacia.");
    }

    public static void validarDomicilioProvinciaNoVacia(String provincia) {
        validarTextoNoVacio(provincia, "La provincia no puede estar vacia.");
    }

    public static void validarMotivoConsultaNoVacio(String motivoConsulta) {
        validarTextoNoVacio(motivoConsulta, "El motivo de consulta no puede estar vacio.");
    }

    public static void validarLongPositivo(Long valor, String mensaje) {
        if (valor == null || valor <= 0) {
            throw new DatoInvalidoException(mensaje);
        }
    }

    public static void validarEnteroPositivo(Integer valor, String mensaje) {
        if (valor == null || valor <= 0) {
            throw new DatoInvalidoException(mensaje);
        }
    }

    public static void validarTextoNoVacio(String valor, String mensaje) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new DatoInvalidoException(mensaje);
        }
    }

    public static void validarSoloLetras(String valor, String mensaje) {
        if (valor == null || !valor.matches(SOLO_LETRAS_REGEX)) {
            throw new DatoInvalidoException(mensaje);
        }
    }
}
