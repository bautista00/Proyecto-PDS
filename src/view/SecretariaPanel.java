package view;

import controller.SecretariaController;
import dto.SecretariaEdicion;
import dto.SecretariaRegistro;
import entity.Secretaria;
import exception.ClinicaException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class SecretariaPanel extends JPanel {

    private final SecretariaController controller;
    private final SecretariaFormularioPanel formularioPanel;
    private final SecretariaTablaPanel tablaPanel;
    private Long idSeleccionado;

    public SecretariaPanel(SecretariaController controller) {
        this.controller = controller;
        formularioPanel = new SecretariaFormularioPanel();
        tablaPanel = new SecretariaTablaPanel(this::cargarSecretariaSeleccionada);

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
            tablaPanel.cargar(controller.listarSecretarias());
        } catch (ClinicaException excepcion) {
            mostrarError(excepcion.getMessage());
        }
    }

    private void cargarSecretariaSeleccionada(Long idSecretaria) {
        idSeleccionado = idSecretaria;
        try {
            formularioPanel.cargar(controller.buscarSecretariaPorId(idSecretaria));
        } catch (ClinicaException excepcion) {
            mostrarError(excepcion.getMessage());
        }
    }

    private void guardar() {
        try {
            SecretariaRegistro datos = formularioPanel.leerRegistro();
            if (idSeleccionado == null) {
                controller.registrarSecretaria(datos);
                JOptionPane.showMessageDialog(this, "Secretaria registrada correctamente.");
            } else {
                controller.actualizarSecretaria(new SecretariaEdicion(idSeleccionado, datos));
                JOptionPane.showMessageDialog(this, "Secretaria actualizada correctamente.");
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
            mostrarError("Seleccione una secretaria de la tabla.");
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(
                this, "¿Eliminar la secretaria seleccionada?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                controller.eliminarSecretaria(idSeleccionado);
                JOptionPane.showMessageDialog(this, "Secretaria eliminada correctamente.");
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
            Secretaria secretaria = controller.buscarSecretariaPorDni(Integer.parseInt(texto));
            tablaPanel.seleccionarPorDni(secretaria.getDni());
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
