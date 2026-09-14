package service;

import entity.EstadoTurno;
import entity.Odontologo;
import exception.DatoInvalidoException;

import java.time.LocalDate;
import java.time.LocalTime;

// Clase que se encarga de validar los datos de un turno, incluyendo la fecha, hora, motivo de consulta, estado
//  y odontólogo asociado al turno.
// Se utiliza en el servicio de TurnoServiceImpl para validar los datos antes de registrar o editar un turno.

public class TurnoValidador {

    public void validarDatos(LocalDate fecha,
                             LocalTime hora,
                             String motivoConsulta,
                             EstadoTurno estado,
                             Odontologo odontologo) {
        if (fecha == null) {
            throw new DatoInvalidoException("La fecha no puede ser nula.");
        }
        if (hora == null) {
            throw new DatoInvalidoException("La hora no puede ser nula.");
        }
        ValidacionesClinica.validarMotivoConsultaNoVacio(motivoConsulta);
        if (estado == null) {
            throw new DatoInvalidoException("El estado del turno no puede ser nulo.");
        }
        if (odontologo == null) {
            throw new DatoInvalidoException("El odontologo del turno no existe.");
        }
        if (!odontologo.puedeAtender(motivoConsulta)) {
            throw new DatoInvalidoException(
                    "El odontologo seleccionado no puede atender ese motivo de consulta.");
        }
    }

    public void validarEstado(EstadoTurno estado) {
        if (estado == null) {
            throw new DatoInvalidoException("El estado del turno no puede ser nulo.");
        }
    }
}
