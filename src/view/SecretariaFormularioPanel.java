package view;

import dto.SecretariaRegistro;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

// Formulario con los campos de la secretaria. Se extrajo de SecretariaPanel (SRP).
// Traduce los campos a SecretariaRegistro y los datos de una secretaria existente a los campos.
final class SecretariaFormularioPanel extends JPanel {

    private final JTextField txtNombre = new JTextField(15);
    private final JTextField txtApellido = new JTextField(15);
    private final JTextField txtDni = new JTextField(10);
    private final JTextField txtBuscarDni = new JTextField(10);

    SecretariaFormularioPanel() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Datos de la Secretaria"));
        construirCampos();
    }

    SecretariaRegistro leerRegistro() {
        return new SecretariaRegistro(
                txtNombre.getText().trim(),
                txtApellido.getText().trim(),
                Integer.parseInt(txtDni.getText().trim()));
    }

    void cargar(SecretariaRegistro datos) {
        txtNombre.setText(datos.getNombre());
        txtApellido.setText(datos.getApellido());
        txtDni.setText(String.valueOf(datos.getDni()));
    }

    String getDniBusqueda() {
        return txtBuscarDni.getText().trim();
    }

    void limpiar() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtDni.setText("");
        txtBuscarDni.setText("");
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
