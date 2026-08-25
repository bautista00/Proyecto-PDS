package service;

import entity.EstadoTurno;
import entity.Odontologo;
import entity.Secretaria;
import entity.Turno;

import java.time.LocalDate;
import java.time.LocalTime;

public class TurnoRelacionador {

    public void sincronizarAlta(Turno turno) {
        turno.getPaciente().agregarTurno(turno);
        turno.getOdontologo().agregarTurno(turno);
        turno.getSecretaria().agregarTurno(turno);
    }

    public void sincronizarBaja(Turno turno) {
        turno.getPaciente().removerTurno(turno);
        turno.getOdontologo().removerTurno(turno);
        turno.getSecretaria().removerTurno(turno);
    }

    public void aplicarCambiosAlTurno(Turno turno,
                                      Odontologo nuevoOdontologo,
                                      Secretaria nuevaSecretaria,
                                      LocalDate fecha,
                                      LocalTime hora,
                                      String motivoConsulta,
                                      EstadoTurno estado) {
        turno.setOdontologo(nuevoOdontologo);
        turno.setSecretaria(nuevaSecretaria);
        turno.setFecha(fecha);
        turno.setHora(hora);
        turno.setMotivoConsulta(motivoConsulta);
        turno.setEstado(estado);
    }

    public void sincronizarActoresTurno(Turno turno,
                                        Odontologo odontologoAnterior,
                                        Secretaria secretariaAnterior,
                                        Odontologo nuevoOdontologo,
                                        Secretaria nuevaSecretaria) {
        if (!odontologoAnterior.getId().equals(nuevoOdontologo.getId())) {
            odontologoAnterior.removerTurno(turno);
            nuevoOdontologo.agregarTurno(turno);
        }

        if (!secretariaAnterior.getId().equals(nuevaSecretaria.getId())) {
            secretariaAnterior.removerTurno(turno);
            nuevaSecretaria.agregarTurno(turno);
        }
    }
}
