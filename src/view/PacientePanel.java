package view;

import controller.PacienteController;
import dto.PacienteEdicion;
import dto.PacienteRegistro;
import entity.CoberturaPaciente;
import entity.Paciente;
import exception.ClinicaException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

public class PacientePanel extends JPanel {

    private final PacienteController controller;
    private final PacienteFormularioPanel formularioPanel;
    private final PacienteTablaPanel tablaPanel;
    private Long idSeleccionado;

    public PacientePanel(PacienteController controller, List<CoberturaPaciente> coberturas) {
        this.controller = controller;
        formularioPanel = new PacienteFormularioPanel(coberturas);
        tablaPanel = new PacienteTablaPanel(this::cargarPacienteSeleccionado);

        setLayout(new BorderLayout(5, 5));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(tablaPanel, BorderLayout.NORTH);
        add(formularioPanel, BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);
        cargarTabla();
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        agregarBoton(panel, "Nuevo", this::limpiar);
        agregarBoton(panel, "Guardar", this::guardar);
        agregarBoton(panel, "Eliminar", this::eliminar);
        agregarBoton(panel, "Buscar DNI", this::buscarPorDni);
        agregarBoton(panel, "Limpiar", this::limpiar);
        return panel;
    }

    private void agregarBoton(JPanel panel, String texto, Runnable accion) {
        JButton boton = new JButton(texto);
        boton.addActionListener(evento -> accion.run());
        panel.add(boton);
    }

    private void cargarTabla() {
        try {
            tablaPanel.cargar(controller.listarPacientesOrdenadosPorApellido());
        } catch (ClinicaException excepcion) {
            mostrarError(excepcion.getMessage());
        }
    }

    private void cargarPacienteSeleccionado(Long idPaciente) {
        idSeleccionado = idPaciente;
        try {
            formularioPanel.cargar(controller.buscarDatosPacientePorId(idPaciente));
        } catch (ClinicaException excepcion) {
            mostrarError(excepcion.getMessage());
        }
    }

    private void guardar() {
        try {
            PacienteRegistro datos = formularioPanel.leerRegistro();
            if (idSeleccionado == null) {
                controller.registrarPaciente(datos);
                JOptionPane.showMessageDialog(this, "Paciente registrado correctamente.");
            } else {
                controller.actualizarPaciente(new PacienteEdicion(idSeleccionado, datos));
                JOptionPane.showMessageDialog(this, "Paciente actualizado correctamente.");
            }
            limpiar();
            cargarTabla();
        } catch (NumberFormatException excepcion) {
            mostrarError("DNI y numero de domicilio deben ser valores numericos validos.");
        } catch (ClinicaException | IllegalArgumentException excepcion) {
            mostrarError(excepcion.getMessage());
        }
    }

    private void eliminar() {
        if (idSeleccionado == null) {
            mostrarError("Seleccione un paciente de la tabla.");
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(
                this, "¿Eliminar el paciente seleccionado?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                controller.eliminarPaciente(idSeleccionado);
                JOptionPane.showMessageDialog(this, "Paciente eliminado correctamente.");
                limpiar();
                cargarTabla();
            } catch (ClinicaException excepcion) {
                mostrarError(excepcion.getMessage());
            }
        }
    }

    private void buscarPorDni() {
        String texto = formularioPanel.getDniBusqueda();
        if (texto.isEmpty()) {
            mostrarError("Ingrese un DNI para buscar.");
            return;
        }
        try {
            Paciente paciente = controller.buscarPacientePorDni(Integer.parseInt(texto));
            tablaPanel.seleccionarPorDni(paciente.getDni());
        } catch (NumberFormatException excepcion) {
            mostrarError("El DNI debe ser un numero.");
        } catch (ClinicaException excepcion) {
            mostrarError(excepcion.getMessage());
        }
    }

    private void limpiar() {
        idSeleccionado = null;
        formularioPanel.limpiar();
        tablaPanel.limpiarSeleccion();
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
