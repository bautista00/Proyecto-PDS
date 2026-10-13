package service;

import entity.Odontologo;
import exception.DatoInvalidoException;

// Clase que se encarga de validar la regla de negocio del turno: que el odontólogo exista
// y pueda atender el motivo de consulta. Los campos obligatorios (fecha, hora, motivo, estado)
// ya llegan validados por TurnoController.
// Se utiliza en el servicio de TurnoServiceImpl antes de registrar o editar un turno.

public class TurnoValidador {

    public void validarOdontologoHabilitado(Odontologo odontologo, String motivoConsulta) {
        if (odontologo == null) {
            throw new DatoInvalidoException("El odontologo del turno no existe.");
        }
        if (!odontologo.puedeAtender(motivoConsulta)) {
            throw new DatoInvalidoException(
                    "El odontologo seleccionado no puede atender ese motivo de consulta.");
        }
    }
}
