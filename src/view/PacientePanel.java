package view;

import controller.PacienteController;
import dto.PacienteEdicion;
import dto.PacienteRegistro;
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
            public boolean isCellEditable(int fila, int columna) { return false; }
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
        GridBagConstraints gbc = crearRestriccionesFormulario();

        inicializarCamposFormulario();

        gbc.gridy = 0;
        agregarCampo(panel, gbc, 0, "Nombre:", txtNombre);
        agregarCampo(panel, gbc, 2, "Apellido:", txtApellido);

        gbc.gridy = 1;
        agregarCampo(panel, gbc, 0, "DNI:", txtDni);
        agregarCampo(panel, gbc, 2, "Email:", txtEmail);

        gbc.gridy = 2;
        agregarCampo(panel, gbc, 0, "Calle:", txtCalle);
        agregarCampo(panel, gbc, 2, "Número:", txtNumero);

        gbc.gridy = 3;
        agregarCampo(panel, gbc, 0, "Localidad:", txtLocalidad);
        agregarCampo(panel, gbc, 2, "Provincia:", txtProvincia);

        gbc.gridy = 4;
        agregarCampo(panel, gbc, 0, "Obra Social:", cboObraSocial);
        agregarCampo(panel, gbc, 2, "Buscar por DNI:", txtBuscarDni);
        return panel;
    }

    private GridBagConstraints crearRestriccionesFormulario() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }

    private void inicializarCamposFormulario() {
        txtNombre = new JTextField(15);
        txtApellido = new JTextField(15);
        txtDni = new JTextField(10);
        txtEmail = new JTextField(20);
        txtCalle = new JTextField(15);
        txtNumero = new JTextField(5);
        txtLocalidad = new JTextField(15);
        txtProvincia = new JTextField(15);
        cboObraSocial = new JComboBox<>(new String[]{"No", "Sí"});
        txtBuscarDni = new JTextField(10);
    }

    private void agregarCampo(JPanel panel,
                              GridBagConstraints gbc,
                              int columna,
                              String etiqueta,
                              Component componente) {
        gbc.gridx = columna;
        gbc.weightx = 0;
        panel.add(new JLabel(etiqueta), gbc);
        gbc.gridx = columna + 1;
        gbc.weightx = 1;
        panel.add(componente, gbc);
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));

        JButton btnNuevo = new JButton("Nuevo");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnBuscar = new JButton("Buscar DNI");
        JButton btnLimpiar = new JButton("Limpiar");

        btnNuevo.addActionListener(e -> limpiarFormulario());
        btnGuardar.addActionListener(e -> guardar());
        btnEliminar.addActionListener(e -> eliminar());
        btnBuscar.addActionListener(e -> buscarPorDni());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

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
            List<Paciente> pacientes = controller.listarPacientesOrdenadosPorApellido();
            for (Paciente paciente : pacientes) {
                modeloTabla.addRow(new Object[]{
                        paciente.getId(),
                        paciente.getNombre(),
                        paciente.getApellido(),
                        paciente.getDni(),
                        paciente.getEmail(),
                        paciente.getObraSocial() ? "Sí" : "No"
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
            PacienteRegistro datos = controller.buscarDatosPacientePorId(idSeleccionado);
            cargarDatosEnFormulario(datos);
        } catch (ClinicaException e) {
            mostrarError(e.getMessage());
        }
    }

    private void cargarDatosEnFormulario(PacienteRegistro datos) {
        txtNombre.setText(datos.getNombre());
        txtApellido.setText(datos.getApellido());
        txtDni.setText(String.valueOf(datos.getDni()));
        txtEmail.setText(datos.getEmail());
        txtCalle.setText(datos.getCalle());
        txtNumero.setText(String.valueOf(datos.getNumero()));
        txtLocalidad.setText(datos.getLocalidad());
        txtProvincia.setText(datos.getProvincia());
        cboObraSocial.setSelectedIndex(Boolean.TRUE.equals(datos.getObraSocial()) ? 1 : 0);
    }

    private void guardar() {
        try {
            PacienteRegistro datos = leerDatosFormulario();
            if (idSeleccionado == null) {
                controller.registrarPaciente(datos);
                JOptionPane.showMessageDialog(this, "Paciente registrado correctamente.");
            } else {
                controller.actualizarPaciente(new PacienteEdicion(idSeleccionado, datos));
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

    private PacienteRegistro leerDatosFormulario() {
        PacienteRegistro datos = new PacienteRegistro();
        datos.setNombre(txtNombre.getText().trim());
        datos.setApellido(txtApellido.getText().trim());
        datos.setDni(Integer.parseInt(txtDni.getText().trim()));
        datos.setEmail(txtEmail.getText().trim());
        datos.setCalle(txtCalle.getText().trim());
        datos.setNumero(Integer.parseInt(txtNumero.getText().trim()));
        datos.setLocalidad(txtLocalidad.getText().trim());
        datos.setProvincia(txtProvincia.getText().trim());
        datos.setObraSocial(cboObraSocial.getSelectedIndex() == 1);
        return datos;
    }

    private void eliminar() {
        if (idSeleccionado == null) {
            mostrarError("Seleccione un paciente de la tabla.");
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el paciente seleccionado?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
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
            Paciente paciente = controller.buscarPacientePorDni(dni);
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                if (modeloTabla.getValueAt(i, 3).equals(paciente.getDni())) {
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
