package view;

import controller.PacienteController;
import entity.Paciente;
import exception.ClinicaException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PacientePanel extends JPanel {

    private final PacienteController controller;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private JTextField txtNombre, txtApellido, txtDni, txtEmail;
    private JTextField txtCalle, txtNumero, txtLocalidad, txtProvincia;
    private JComboBox<String> cboObraSocial;
    private JTextField txtBuscarDni;
    private Long idSeleccionado;

    public PacientePanel(PacienteController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(crearPanelTabla(), BorderLayout.NORTH);
        add(crearPanelFormulario(), BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);
        cargarTabla();
    }

    private JScrollPane crearPanelTabla() {
        String[] columnas = {"ID", "Nombre", "Apellido", "DNI", "Email", "Obra Social"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
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
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Paciente"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNombre    = new JTextField(15);
        txtApellido  = new JTextField(15);
        txtDni       = new JTextField(10);
        txtEmail     = new JTextField(20);
        txtCalle     = new JTextField(15);
        txtNumero    = new JTextField(5);
        txtLocalidad = new JTextField(15);
        txtProvincia = new JTextField(15);
        cboObraSocial = new JComboBox<>(new String[]{"No", "Sí"});
        txtBuscarDni  = new JTextField(10);

        gbc.gridy = 0;
        agregarFila(panel, gbc, "Nombre:", txtNombre, "Apellido:", txtApellido);

        gbc.gridy = 1;
        agregarFila(panel, gbc, "DNI:", txtDni, "Email:", txtEmail);

        gbc.gridy = 2;
        agregarFila(panel, gbc, "Calle:", txtCalle, "Número:", txtNumero);

        gbc.gridy = 3;
        agregarFila(panel, gbc, "Localidad:", txtLocalidad, "Provincia:", txtProvincia);

        gbc.gridy = 4;
        agregarFila(panel, gbc, "Obra Social:", cboObraSocial, "Buscar por DNI:", txtBuscarDni);

        return panel;
    }

    private void agregarFila(JPanel panel, GridBagConstraints gbc,
                              String label1, Component comp1,
                              String label2, Component comp2) {
        gbc.gridx = 0; gbc.weightx = 0; panel.add(new JLabel(label1), gbc);
        gbc.gridx = 1; gbc.weightx = 1; panel.add(comp1, gbc);
        gbc.gridx = 2; gbc.weightx = 0; panel.add(new JLabel(label2), gbc);
        gbc.gridx = 3; gbc.weightx = 1; panel.add(comp2, gbc);
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
            List<Paciente> lista = controller.listarPacientesOrdenadosPorApellido();
            for (Paciente p : lista) {
                modeloTabla.addRow(new Object[]{
                        p.getId(),
                        p.getNombre(),
                        p.getApellido(),
                        p.getDni(),
                        p.getEmail(),
                        p.getObraSocial() ? "Sí" : "No"
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
            Paciente p = controller.buscarPacientePorId(idSeleccionado);
            txtNombre.setText(p.getNombre());
            txtApellido.setText(p.getApellido());
            txtDni.setText(String.valueOf(p.getDni()));
            txtEmail.setText(p.getEmail());
            txtCalle.setText(p.getDomicilio().getCalle());
            txtNumero.setText(String.valueOf(p.getDomicilio().getNumero()));
            txtLocalidad.setText(p.getDomicilio().getLocalidad());
            txtProvincia.setText(p.getDomicilio().getProvincia());
            cboObraSocial.setSelectedIndex(Boolean.TRUE.equals(p.getObraSocial()) ? 1 : 0);
        } catch (ClinicaException e) {
            mostrarError(e.getMessage());
        }
    }

    private void guardar() {
        try {
            String nombre    = txtNombre.getText().trim();
            String apellido  = txtApellido.getText().trim();
            Integer dni      = Integer.parseInt(txtDni.getText().trim());
            String email     = txtEmail.getText().trim();
            String calle     = txtCalle.getText().trim();
            Integer numero   = Integer.parseInt(txtNumero.getText().trim());
            String localidad = txtLocalidad.getText().trim();
            String provincia = txtProvincia.getText().trim();
            Boolean obraSocial = cboObraSocial.getSelectedIndex() == 1;

            if (idSeleccionado == null) {
                controller.registrarPaciente(nombre, apellido, dni, email, calle, numero, localidad, provincia, obraSocial);
                JOptionPane.showMessageDialog(this, "Paciente registrado correctamente.");
            } else {
                controller.actualizarPaciente(idSeleccionado, nombre, apellido, dni, email, calle, numero, localidad, provincia, obraSocial);
                JOptionPane.showMessageDialog(this, "Paciente actualizado correctamente.");
            }
            limpiarFormulario();
            cargarTabla();
        } catch (NumberFormatException e) {
            mostrarError("DNI y Número deben ser valores numéricos válidos.");
        } catch (ClinicaException e) {
            mostrarError(e.getMessage());
        }
    }

    private void eliminar() {
        if (idSeleccionado == null) {
            mostrarError("Seleccione un paciente de la tabla.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el paciente seleccionado?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controller.eliminarPaciente(idSeleccionado);
                JOptionPane.showMessageDialog(this, "Paciente eliminado correctamente.");
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
            Paciente p = controller.buscarPacientePorDni(dni);
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                if (modeloTabla.getValueAt(i, 3).equals(p.getDni())) {
                    tabla.setRowSelectionInterval(i, i);
                    tabla.scrollRectToVisible(tabla.getCellRect(i, 0, true));
                    break;
                }
            }
            cargarFilaSeleccionada();
        } catch (NumberFormatException e) {
            mostrarError("El DNI debe ser un número.");
        } catch (ClinicaException e) {
            mostrarError(e.getMessage());
        }
    }

    private void limpiarFormulario() {
        idSeleccionado = null;
        txtNombre.setText("");
        txtApellido.setText("");
        txtDni.setText("");
        txtEmail.setText("");
        txtCalle.setText("");
        txtNumero.setText("");
        txtLocalidad.setText("");
        txtProvincia.setText("");
        cboObraSocial.setSelectedIndex(0);
        txtBuscarDni.setText("");
        tabla.clearSelection();
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
