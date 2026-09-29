package view;

import dto.OdontologoRegistro;
import entity.EspecialidadOdontologica;
import entity.Odontologo;

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

final class OdontologoFormularioPanel extends JPanel {

    private final JTextField txtNombre = new JTextField(15);
    private final JTextField txtApellido = new JTextField(15);
    private final JTextField txtDni = new JTextField(10);
    private final JTextField txtMatricula = new JTextField(10);
    private final JComboBox<EspecialidadOdontologica> cboEspecialidad;
    private final JTextField txtBuscarMatricula = new JTextField(10);

    OdontologoFormularioPanel(List<EspecialidadOdontologica> especialidades) {
        if (especialidades == null || especialidades.isEmpty()) {
            throw new IllegalArgumentException("Debe existir al menos una especialidad disponible.");
        }
        cboEspecialidad = new JComboBox<>(
                especialidades.toArray(new EspecialidadOdontologica[0]));
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Datos del Odontologo"));
        construirCampos();
    }

    OdontologoRegistro leerRegistro() {
        return new OdontologoRegistro(
                txtNombre.getText().trim(),
                txtApellido.getText().trim(),
                Integer.parseInt(txtDni.getText().trim()),
                txtMatricula.getText().trim(),
                (EspecialidadOdontologica) cboEspecialidad.getSelectedItem());
    }

    void cargar(Odontologo odontologo) {
        txtNombre.setText(odontologo.getNombre());
        txtApellido.setText(odontologo.getApellido());
        txtDni.setText(String.valueOf(odontologo.getDni()));
        txtMatricula.setText(odontologo.getMatricula());
        cboEspecialidad.setSelectedItem(odontologo.getEspecialidad());
        cboEspecialidad.setEnabled(false);
    }

    String getMatriculaBusqueda() {
        return txtBuscarMatricula.getText().trim();
    }

    void limpiar() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtDni.setText("");
        txtMatricula.setText("");
        txtBuscarMatricula.setText("");
        cboEspecialidad.setEnabled(true);
        cboEspecialidad.setSelectedIndex(0);
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
        agregarCampo(gbc, 2, "Matricula:", txtMatricula);
        gbc.gridy = 2;
        agregarCampo(gbc, 0, "Especialidad:", cboEspecialidad);
        agregarCampo(gbc, 2, "Buscar matricula:", txtBuscarMatricula);
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
