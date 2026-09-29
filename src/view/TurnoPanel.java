package view;

import controller.OdontologoController;
import controller.PacienteController;
import controller.SecretariaController;
import controller.TurnoController;
import dto.TurnoEdicion;
import dto.TurnoRegistro;
import entity.Odontologo;
import entity.Paciente;
import entity.Secretaria;
import entity.Turno;
import exception.ClinicaException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

// Pantalla de turnos. Es la única que necesita los cuatro controllers, porque un turno
// relaciona paciente, odontólogo y secretaria.
// Delega la tabla, el formulario y los filtros en TurnoTablaPanel, TurnoFormularioPanel y
// TurnoFiltrosPanel, y les conecta las acciones de guardar, eliminar, filtrar y calcular monto.
public class TurnoPanel extends JPanel {

    private final TurnoController turnoController;
    private final PacienteController pacienteController;
    private final OdontologoController odontologoController;
    private final SecretariaController secretariaController;
    private final TurnoTablaPanel tablaPanel;
    private final TurnoFormularioPanel formularioPanel;
    private Long idSeleccionado;

    public TurnoPanel(TurnoController turnoController,
                      PacienteController pacienteController,
                      OdontologoController odontologoController,
                      SecretariaController secretariaController) {
        this.turnoController = turnoController;
        this.pacienteController = pacienteController;
        this.odontologoController = odontologoController;
        this.secretariaController = secretariaController;
        this.tablaPanel = new TurnoTablaPanel(this::cargarTurnoSeleccionado);
        this.formularioPanel = new TurnoFormularioPanel();

        setLayout(new BorderLayout(5, 5));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(tablaPanel, BorderLayout.NORTH);
        add(formularioPanel, BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);

        actualizarCombos();
        cargarTodos();
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 4, 4));
        panel.add(crearAccionesPrincipales());
        TurnoFiltroDialogos filtros = new TurnoFiltroDialogos(
                this,
                turnoController,
                tablaPanel::cargar,
                this::mostrarError,
                this::cargarTodos);
        panel.add(new TurnoFiltrosPanel(filtros.crearAcciones()));
        return panel;
    }

    private JPanel crearAccionesPrincipales() {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
        agregarBoton(fila, "Nuevo", this::limpiarFormulario);
        agregarBoton(fila, "Guardar", this::guardar);
        agregarBoton(fila, "Eliminar", this::eliminar);
        agregarBoton(fila, "Calcular Monto", this::calcularMonto);
        agregarBoton(fila, "Limpiar", this::limpiarFormulario);
        return fila;
    }

    private void agregarBoton(JPanel panel, String texto, Runnable accion) {
        JButton boton = new JButton(texto);
        boton.addActionListener(evento -> accion.run());
        panel.add(boton);
    }

    public void actualizarCombos() {
        List<Paciente> pacientes = obtenerPacientes();
        List<Odontologo> odontologos = obtenerOdontologos();
        List<Secretaria> secretarias = obtenerSecretarias();
        formularioPanel.actualizarActores(pacientes, odontologos, secretarias);
    }

    private List<Paciente> obtenerPacientes() {
        try {
            return pacienteController.listarPacientesOrdenadosPorApellido();
        } catch (ClinicaException excepcion) {
            return new ArrayList<>();
        }
    }

    private List<Odontologo> obtenerOdontologos() {
        try {
            return odontologoController.listarOdontologos();
        } catch (ClinicaException excepcion) {
            return new ArrayList<>();
        }
    }

    private List<Secretaria> obtenerSecretarias() {
        try {
            return secretariaController.listarSecretarias();
        } catch (ClinicaException excepcion) {
            return new ArrayList<>();
        }
    }

    private void cargarTurnoSeleccionado(Long idTurno) {
        idSeleccionado = idTurno;
        try {
            Turno turno = turnoController.buscarTurnoPorId(idTurno);
            formularioPanel.cargar(turno);
        } catch (ClinicaException excepcion) {
            mostrarError(excepcion.getMessage());
        }
    }

    private void guardar() {
        if (!formularioPanel.tieneActoresDisponibles()) {
            mostrarError("Registre pacientes, odontologos y secretarias antes de crear un turno.");
            return;
        }
        try {
            if (idSeleccionado == null) {
                TurnoRegistro registro = formularioPanel.leerRegistro();
                turnoController.registrarTurno(registro);
                JOptionPane.showMessageDialog(this, "Turno registrado correctamente.");
            } else {
                TurnoEdicion edicion = formularioPanel.crearEdicion(idSeleccionado);
                turnoController.actualizarTurno(edicion);
                JOptionPane.showMessageDialog(this, "Turno actualizado correctamente.");
            }
            limpiarFormulario();
            cargarTodos();
        } catch (IllegalStateException excepcion) {
            mostrarError(excepcion.getMessage());
        } catch (DateTimeParseException excepcion) {
            mostrarError("Formato incorrecto. Fecha: yyyy-MM-dd - Hora: HH:mm");
        } catch (ClinicaException | IllegalArgumentException excepcion) {
            mostrarError(excepcion.getMessage());
        }
    }

    private void eliminar() {
        if (idSeleccionado == null) {
            mostrarError("Seleccione un turno de la tabla.");
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "Eliminar el turno seleccionado?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                turnoController.eliminarTurno(idSeleccionado);
                JOptionPane.showMessageDialog(this, "Turno eliminado correctamente.");
                limpiarFormulario();
                cargarTodos();
            } catch (ClinicaException excepcion) {
                mostrarError(excepcion.getMessage());
            }
        }
    }

    private void calcularMonto() {
        if (idSeleccionado == null) {
            mostrarError("Seleccione un turno de la tabla.");
            return;
        }
        try {
            Double monto = turnoController.calcularMontoTurno(idSeleccionado);
            JOptionPane.showMessageDialog(
                    this,
                    String.format("Monto del turno #%d: $%.2f", idSeleccionado, monto),
                    "Monto del Turno",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (ClinicaException excepcion) {
            mostrarError(excepcion.getMessage());
        }
    }

    private void cargarTodos() {
        tablaPanel.cargar(turnoController.listarTurnos());
    }

    private void limpiarFormulario() {
        idSeleccionado = null;
        formularioPanel.limpiar();
        tablaPanel.limpiarSeleccion();
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
