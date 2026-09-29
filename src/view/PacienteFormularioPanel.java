package view;

import dto.PacienteRegistro;
import entity.CoberturaPaciente;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

final class PacienteFormularioPanel extends JPanel {

    private final JTextField txtNombre = new JTextField(15);
    private final JTextField txtApellido = new JTextField(15);
    private final JTextField txtDni = new JTextField(10);
    private final JTextField txtEmail = new JTextField(20);
    private final JTextField txtCalle = new JTextField(15);
    private final JTextField txtNumero = new JTextField(5);
    private final JTextField txtLocalidad = new JTextField(15);
    private final JTextField txtProvincia = new JTextField(15);
    private final JComboBox<CoberturaPaciente> cboCobertura;
    private final JTextField txtBuscarDni = new JTextField(10);

    PacienteFormularioPanel(List<CoberturaPaciente> coberturas) {
        if (coberturas == null || coberturas.isEmpty()) {
            throw new IllegalArgumentException("Debe existir al menos una cobertura disponible.");
        }
        cboCobertura = new JComboBox<>(coberturas.toArray(new CoberturaPaciente[0]));
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Datos del Paciente"));
        construirCampos();
    }

    PacienteRegistro leerRegistro() {
        PacienteRegistro datos = new PacienteRegistro();
        datos.setNombre(txtNombre.getText().trim());
        datos.setApellido(txtApellido.getText().trim());
        datos.setDni(Integer.parseInt(txtDni.getText().trim()));
        datos.setEmail(txtEmail.getText().trim());
        datos.setCalle(txtCalle.getText().trim());
        datos.setNumero(Integer.parseInt(txtNumero.getText().trim()));
        datos.setLocalidad(txtLocalidad.getText().trim());
        datos.setProvincia(txtProvincia.getText().trim());
        datos.setCobertura((CoberturaPaciente) cboCobertura.getSelectedItem());
        return datos;
    }

    void cargar(PacienteRegistro datos) {
        txtNombre.setText(datos.getNombre());
        txtApellido.setText(datos.getApellido());
        txtDni.setText(String.valueOf(datos.getDni()));
        txtEmail.setText(datos.getEmail());
        txtCalle.setText(datos.getCalle());
        txtNumero.setText(String.valueOf(datos.getNumero()));
        txtLocalidad.setText(datos.getLocalidad());
        txtProvincia.setText(datos.getProvincia());
        cboCobertura.setSelectedItem(datos.getCobertura());
    }

    String getDniBusqueda() {
        return txtBuscarDni.getText().trim();
    }

    void limpiar() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtDni.setText("");
        txtEmail.setText("");
        txtCalle.setText("");
        txtNumero.setText("");
        txtLocalidad.setText("");
        txtProvincia.setText("");
        txtBuscarDni.setText("");
        cboCobertura.setSelectedIndex(0);
    }

    private void construirCampos() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        agregarCampo(gbc, 0, "Nombre:", txtNombre);
        agregarCampo(gbc, 2, "Apellido:", txtApellido);
        gbc.gridy = 1;
        agregarCampo(gbc, 0, "DNI:", txtDni);
        agregarCampo(gbc, 2, "Email:", txtEmail);
        gbc.gridy = 2;
        agregarCampo(gbc, 0, "Calle:", txtCalle);
        agregarCampo(gbc, 2, "Numero:", txtNumero);
        gbc.gridy = 3;
        agregarCampo(gbc, 0, "Localidad:", txtLocalidad);
        agregarCampo(gbc, 2, "Provincia:", txtProvincia);
        gbc.gridy = 4;
        agregarCampo(gbc, 0, "Cobertura:", cboCobertura);
        agregarCampo(gbc, 2, "Buscar por DNI:", txtBuscarDni);
    }

    private void agregarCampo(GridBagConstraints gbc,
                              int columna,
                              String etiqueta,
                              Component componente) {
        gbc.gridx = columna;
        gbc.weightx = 0;
        add(new JLabel(etiqueta), gbc);
        gbc.gridx = columna + 1;
        gbc.weightx = 1;
        add(componente, gbc);
    }
}
