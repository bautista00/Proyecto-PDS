package view;

import controller.OdontologoController;
import entity.Endodoncista;
import entity.Odontologo;
import entity.OdontologoGeneral;
import entity.Ortodoncista;
import exception.ClinicaException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OdontologoPanel extends JPanel {

    private final OdontologoController controller;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private JTextField txtNombre, txtApellido, txtDni, txtMatricula, txtBuscarMatricula;
    private JComboBox<String> cboEspecialidad;
    private Long idSeleccionado;

    public OdontologoPanel(OdontologoController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(crearPanelTabla(), BorderLayout.NORTH);
        add(crearPanelFormulario(), BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);
        cargarTabla();
    }

    private JScrollPane crearPanelTabla() {
        String[] columnas = {"ID", "Nombre", "Apellido", "DNI", "Matrícula", "Especialidad", "Tarifa"};
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
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Odontólogo"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNombre          = new JTextField(15);
        txtApellido        = new JTextField(15);
        txtDni             = new JTextField(10);
        txtMatricula       = new JTextField(10);
        cboEspecialidad    = new JComboBox<>(new String[]{"General", "Ortodoncista", "Endodoncista"});
        txtBuscarMatricula = new JTextField(10);

        gbc.gridy = 0;
        gbc.gridx = 0; gbc.weightx = 0; panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; panel.add(txtNombre, gbc);
        gbc.gridx = 2; gbc.weightx = 0; panel.add(new JLabel("Apellido:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1; panel.add(txtApellido, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0; gbc.weightx = 0; panel.add(new JLabel("DNI:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; panel.add(txtDni, gbc);
        gbc.gridx = 2; gbc.weightx = 0; panel.add(new JLabel("Matrícula:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1; panel.add(txtMatricula, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0; gbc.weightx = 0; panel.add(new JLabel("Especialidad:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; panel.add(cboEspecialidad, gbc);
        gbc.gridx = 2; gbc.weightx = 0; panel.add(new JLabel("Buscar matrícula:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1; panel.add(txtBuscarMatricula, gbc);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));

        JButton btnNuevo    = new JButton("Nuevo");
        JButton btnGuardar  = new JButton("Guardar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnBuscar   = new JButton("Buscar Matrícula");
        JButton btnLimpiar  = new JButton("Limpiar");

        btnNuevo.addActionListener(e    -> limpiarFormulario());
        btnGuardar.addActionListener(e  -> guardar());
        btnEliminar.addActionListener(e -> eliminar());
        btnBuscar.addActionListener(e   -> buscarPorMatricula());
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
            for (Odontologo o : controller.listarOdontologos()) {
                modeloTabla.addRow(new Object[]{
                        o.getId(),
                        o.getNombre(),
                        o.getApellido(),
                        o.getDni(),
                        o.getMatricula(),
                        o.getEspecialidad(),
                        String.format("$%.0f", o.getTarifaBase())
                });
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
            Odontologo o = controller.buscarOdontologoPorId(idSeleccionado);
            txtNombre.setText(o.getNombre());
            txtApellido.setText(o.getApellido());
            txtDni.setText(String.valueOf(o.getDni()));
            txtMatricula.setText(o.getMatricula());
            switch (o.getEspecialidad()) {
                case "Ortodoncia":  cboEspecialidad.setSelectedIndex(1); break;
                case "Endodoncia":  cboEspecialidad.setSelectedIndex(2); break;
                default:            cboEspecialidad.setSelectedIndex(0); break;
            }
        } catch (ClinicaException e) {
            mostrarError(e.getMessage());
        }
    }

    private void guardar() {
        try {
            String nombre   = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            Integer dni     = Integer.parseInt(txtDni.getText().trim());
            String matricula = txtMatricula.getText().trim();

            if (idSeleccionado == null) {
                Odontologo nuevo;
                switch (cboEspecialidad.getSelectedIndex()) {
                    case 1:  nuevo = new Ortodoncista(nombre, apellido, dni, matricula); break;
                    case 2:  nuevo = new Endodoncista(nombre, apellido, dni, matricula); break;
                    default: nuevo = new OdontologoGeneral(nombre, apellido, dni, matricula); break;
                }
                controller.registrarOdontologo(nuevo);
                JOptionPane.showMessageDialog(this, "Odontólogo registrado correctamente.");
            } else {
                controller.actualizarOdontologo(idSeleccionado, nombre, apellido, dni, matricula);
                JOptionPane.showMessageDialog(this, "Odontólogo actualizado correctamente.");
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
            mostrarError("Seleccione un odontólogo de la tabla.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el odontólogo seleccionado?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controller.eliminarOdontologo(idSeleccionado);
                JOptionPane.showMessageDialog(this, "Odontólogo eliminado correctamente.");
                limpiarFormulario();
                cargarTabla();
            } catch (ClinicaException e) {
                mostrarError(e.getMessage());
            }
        }
    }

    private void buscarPorMatricula() {
        String matricula = txtBuscarMatricula.getText().trim();
        if (matricula.isEmpty()) {
            mostrarError("Ingrese una matrícula para buscar.");
            return;
        }
        try {
            Odontologo o = controller.buscarOdontologoPorMatricula(matricula);
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                if (modeloTabla.getValueAt(i, 4).toString().equalsIgnoreCase(o.getMatricula())) {
                    tabla.setRowSelectionInterval(i, i);
                    tabla.scrollRectToVisible(tabla.getCellRect(i, 0, true));
                    break;
                }
            }
            cargarFilaSeleccionada();
        } catch (ClinicaException e) {
            mostrarError(e.getMessage());
        }
    }

    private void limpiarFormulario() {
        idSeleccionado = null;
        txtNombre.setText("");
        txtApellido.setText("");
        txtDni.setText("");
        txtMatricula.setText("");
        txtBuscarMatricula.setText("");
        cboEspecialidad.setSelectedIndex(0);
        tabla.clearSelection();
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
