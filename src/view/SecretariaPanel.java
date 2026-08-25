package view;

import controller.SecretariaController;
import entity.Secretaria;
import exception.ClinicaException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SecretariaPanel extends JPanel {

    private final SecretariaController controller;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private JTextField txtNombre, txtApellido, txtDni, txtBuscarDni;
    private Long idSeleccionado;

    public SecretariaPanel(SecretariaController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(crearPanelTabla(), BorderLayout.NORTH);
        add(crearPanelFormulario(), BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);
        cargarTabla();
    }

    private JScrollPane crearPanelTabla() {
        String[] columnas = {"ID", "Nombre", "Apellido", "DNI"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { cargarFilaSeleccionada(); }
        });
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(0, 220));
        return scroll;
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos de la Secretaria"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNombre     = new JTextField(15);
        txtApellido   = new JTextField(15);
        txtDni        = new JTextField(10);
        txtBuscarDni  = new JTextField(10);

        gbc.gridy = 0;
        gbc.gridx = 0; gbc.weightx = 0; panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; panel.add(txtNombre, gbc);
        gbc.gridx = 2; gbc.weightx = 0; panel.add(new JLabel("Apellido:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1; panel.add(txtApellido, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0; gbc.weightx = 0; panel.add(new JLabel("DNI:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; panel.add(txtDni, gbc);
        gbc.gridx = 2; gbc.weightx = 0; panel.add(new JLabel("Buscar por DNI:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1; panel.add(txtBuscarDni, gbc);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));

        JButton btnNuevo    = new JButton("Nuevo");
        JButton btnGuardar  = new JButton("Guardar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnBuscar   = new JButton("Buscar DNI");
        JButton btnLimpiar  = new JButton("Limpiar");

        btnNuevo.addActionListener(e    -> limpiarFormulario());
        btnGuardar.addActionListener(e  -> guardar());
        btnEliminar.addActionListener(e -> eliminar());
        btnBuscar.addActionListener(e   -> buscarPorDni());
        btnLimpiar.addActionListener(e  -> limpiarFormulario());

        panel.add(btnNuevo);
        panel.add(btnGuardar);
        panel.add(btnEliminar);
        panel.add(btnBuscar);
        panel.add(btnLimpiar);
        return panel;
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        try {
            for (Secretaria s : controller.listarSecretarias()) {
                modeloTabla.addRow(new Object[]{s.getId(), s.getNombre(), s.getApellido(), s.getDni()});
            }
        } catch (ClinicaException e) {
            mostrarError(e.getMessage());
        }
    }

    private void cargarFilaSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        idSeleccionado = (Long) modeloTabla.getValueAt(fila, 0);
        try {
            Secretaria s = controller.buscarSecretariaPorId(idSeleccionado);
            txtNombre.setText(s.getNombre());
            txtApellido.setText(s.getApellido());
            txtDni.setText(String.valueOf(s.getDni()));
        } catch (ClinicaException e) {
            mostrarError(e.getMessage());
        }
    }

    private void guardar() {
        try {
            String nombre   = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            Integer dni     = Integer.parseInt(txtDni.getText().trim());

            if (idSeleccionado == null) {
                controller.registrarSecretaria(nombre, apellido, dni);
                JOptionPane.showMessageDialog(this, "Secretaria registrada correctamente.");
            } else {
                controller.actualizarSecretaria(idSeleccionado, nombre, apellido, dni);
                JOptionPane.showMessageDialog(this, "Secretaria actualizada correctamente.");
            }
            limpiarFormulario();
            cargarTabla();
        } catch (NumberFormatException e) {
            mostrarError("DNI debe ser un valor numérico válido.");
        } catch (ClinicaException e) {
            mostrarError(e.getMessage());
        }
    }

    private void eliminar() {
        if (idSeleccionado == null) {
            mostrarError("Seleccione una secretaria de la tabla.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar la secretaria seleccionada?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controller.eliminarSecretaria(idSeleccionado);
                JOptionPane.showMessageDialog(this, "Secretaria eliminada correctamente.");
                limpiarFormulario();
                cargarTabla();
            } catch (ClinicaException e) {
                mostrarError(e.getMessage());
            }
        }
    }

    private void buscarPorDni() {
        String texto = txtBuscarDni.getText().trim();
        if (texto.isEmpty()) {
            mostrarError("Ingrese un DNI para buscar.");
            return;
        }
        try {
            Integer dni = Integer.parseInt(texto);
            Secretaria s = controller.buscarSecretariaPorDni(dni);
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                if (modeloTabla.getValueAt(i, 3).equals(s.getDni())) {
                    tabla.setRowSelectionInterval(i, i);
                    tabla.scrollRectToVisible(tabla.getCellRect(i, 0, true));
                    break;
                }
            }
            cargarFilaSeleccionada();
        } catch (NumberFormatException e) {
            mostrarError("DNI debe ser un número.");
        } catch (ClinicaException e) {
            mostrarError(e.getMessage());
        }
    }

    private void limpiarFormulario() {
        idSeleccionado = null;
        txtNombre.setText("");
        txtApellido.setText("");
        txtDni.setText("");
        txtBuscarDni.setText("");
        tabla.clearSelection();
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
