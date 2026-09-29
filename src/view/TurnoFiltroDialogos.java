package view;

import controller.TurnoController;
import entity.EstadoTurno;
import entity.Turno;
import exception.ClinicaException;

import javax.swing.JOptionPane;
import java.awt.Component;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

final class TurnoFiltroDialogos {

    private final Component padre;
    private final TurnoController controller;
    private final Consumer<List<Turno>> mostrarResultados;
    private final Consumer<String> mostrarError;
    private final Runnable verTodos;

    TurnoFiltroDialogos(Component padre,
                        TurnoController controller,
                        Consumer<List<Turno>> mostrarResultados,
                        Consumer<String> mostrarError,
                        Runnable verTodos) {
        this.padre = padre;
        this.controller = controller;
        this.mostrarResultados = mostrarResultados;
        this.mostrarError = mostrarError;
        this.verTodos = verTodos;
    }

    List<AccionFiltroTurno> crearAcciones() {
        List<AccionFiltroTurno> acciones = new ArrayList<>();
        acciones.add(new AccionFiltroTurno("Ver Todos", verTodos));
        acciones.add(new AccionFiltroTurno("Filtrar por Paciente", this::porPaciente));
        acciones.add(new AccionFiltroTurno("Filtrar por Odontologo", this::porOdontologo));
        acciones.add(new AccionFiltroTurno("Filtrar por Secretaria", this::porSecretaria));
        acciones.add(new AccionFiltroTurno("Filtrar por Fechas", this::porFechas));
        acciones.add(new AccionFiltroTurno("Filtrar por Estado", this::porEstado));
        return acciones;
    }

    private void porPaciente() {
        Long id = solicitarId("ID del Paciente:");
        if (id != null) {
            ejecutar(() -> controller.listarTurnosPorPaciente(id));
        }
    }

    private void porOdontologo() {
        Long id = solicitarId("ID del Odontologo:");
        if (id != null) {
            ejecutar(() -> controller.listarTurnosPorOdontologo(id));
        }
    }

    private void porSecretaria() {
        Long id = solicitarId("ID de la Secretaria:");
        if (id != null) {
            ejecutar(() -> controller.listarTurnosPorSecretaria(id));
        }
    }

    private void porEstado() {
        EstadoTurno estado = (EstadoTurno) JOptionPane.showInputDialog(
                padre,
                "Seleccione el estado a filtrar:",
                "Filtrar por Estado",
                JOptionPane.QUESTION_MESSAGE,
                null,
                EstadoTurno.values(),
                EstadoTurno.PENDIENTE);
        if (estado != null) {
            ejecutar(() -> controller.listarTurnosPorEstado(estado));
        }
    }

    private void porFechas() {
        String desde = JOptionPane.showInputDialog(padre, "Fecha desde (yyyy-MM-dd):");
        if (desde == null || desde.trim().isEmpty()) {
            return;
        }
        String hasta = JOptionPane.showInputDialog(padre, "Fecha hasta (yyyy-MM-dd):");
        if (hasta == null || hasta.trim().isEmpty()) {
            return;
        }
        try {
            LocalDate fechaDesde = LocalDate.parse(desde.trim());
            LocalDate fechaHasta = LocalDate.parse(hasta.trim());
            ejecutar(() -> controller.buscarTurnosPorRangoFechas(fechaDesde, fechaHasta));
        } catch (DateTimeParseException excepcion) {
            mostrarError.accept("Formato de fecha invalido. Use yyyy-MM-dd.");
        }
    }

    private Long solicitarId(String mensaje) {
        String valor = JOptionPane.showInputDialog(padre, mensaje);
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(valor.trim());
        } catch (NumberFormatException excepcion) {
            mostrarError.accept("Ingrese un ID numerico.");
            return null;
        }
    }

    private void ejecutar(Supplier<List<Turno>> consulta) {
        try {
            mostrarResultados.accept(consulta.get());
        } catch (ClinicaException excepcion) {
            mostrarError.accept(excepcion.getMessage());
        }
    }
}
