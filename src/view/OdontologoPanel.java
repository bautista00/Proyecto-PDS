package view;

import controller.OdontologoController;
import dto.OdontologoEdicion;
import dto.OdontologoRegistro;
import entity.EspecialidadOdontologica;
import entity.Odontologo;
import exception.ClinicaException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

public class OdontologoPanel extends JPanel {

    private final OdontologoController controller;
    private final OdontologoFormularioPanel formularioPanel;
    private final OdontologoTablaPanel tablaPanel;
    private Long idSeleccionado;

    public OdontologoPanel(OdontologoController controller,
                           List<EspecialidadOdontologica> especialidades) {
        this.controller = controller;
        formularioPanel = new OdontologoFormularioPanel(especialidades);
        tablaPanel = new OdontologoTablaPanel(this::cargarOdontologoSeleccionado);

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
        agregarBoton(panel, "Buscar Matricula", this::buscarPorMatricula);
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
            tablaPanel.cargar(controller.listarOdontologos());
        } catch (ClinicaException excepcion) {
            mostrarError(excepcion.getMessage());
        }
    }

    private void cargarOdontologoSeleccionado(Long idOdontologo) {
        idSeleccionado = idOdontologo;
        try {
            formularioPanel.cargar(controller.buscarOdontologoPorId(idOdontologo));
        } catch (ClinicaException excepcion) {
            mostrarError(excepcion.getMessage());
        }
    }

    private void guardar() {
        try {
            OdontologoRegistro datos = formularioPanel.leerRegistro();
            if (idSeleccionado == null) {
                controller.registrarOdontologo(datos);
                JOptionPane.showMessageDialog(this, "Odontologo registrado correctamente.");
            } else {
                controller.actualizarOdontologo(new OdontologoEdicion(idSeleccionado, datos));
                JOptionPane.showMessageDialog(this, "Odontologo actualizado correctamente.");
            }
            limpiar();
            cargarTabla();
        } catch (NumberFormatException excepcion) {
            mostrarError("DNI debe ser un valor numerico valido.");
        } catch (ClinicaException | IllegalArgumentException excepcion) {
            mostrarError(excepcion.getMessage());
        }
    }

    private void eliminar() {
        if (idSeleccionado == null) {
            mostrarError("Seleccione un odontologo de la tabla.");
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(
                this, "¿Eliminar el odontologo seleccionado?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                controller.eliminarOdontologo(idSeleccionado);
                JOptionPane.showMessageDialog(this, "Odontologo eliminado correctamente.");
                limpiar();
                cargarTabla();
            } catch (ClinicaException excepcion) {
                mostrarError(excepcion.getMessage());
            }
        }
    }

    private void buscarPorMatricula() {
        String matricula = formularioPanel.getMatriculaBusqueda();
        if (matricula.isEmpty()) {
            mostrarError("Ingrese una matricula para buscar.");
            return;
        }
        try {
            Odontologo odontologo = controller.buscarOdontologoPorMatricula(matricula);
            tablaPanel.seleccionarPorMatricula(odontologo.getMatricula());
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
