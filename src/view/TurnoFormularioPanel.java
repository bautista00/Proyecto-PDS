package view;

import dto.OdontologoDatos;
import dto.PacienteDatos;
import dto.SecretariaDatos;
import dto.TurnoDatos;
import dto.TurnoEdicion;
import dto.TurnoRegistro;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

// Formulario de carga y edición de un turno (combos de paciente, odontólogo, secretaria
// y estado, más fecha, hora y motivo).
// Se extrajo de TurnoPanel para aislar el manejo de los campos de pantalla.
// Su tarea es traducir en las dos direcciones: de los campos a los DTOs TurnoRegistro
// y TurnoEdicion, y de los datos de un turno existente a los campos.
final class TurnoFormularioPanel extends JPanel {

    private final JComboBox<OpcionEntidad> cboPaciente = new JComboBox<>();
    private final JComboBox<OpcionEntidad> cboOdontologo = new JComboBox<>();
    private final JComboBox<OpcionEntidad> cboSecretaria = new JComboBox<>();
    private final JComboBox<String> cboEstado;
    private final JTextField txtFecha = new JTextField(12);
    private final JTextField txtHora = new JTextField(8);
    private final JTextField txtMotivo = new JTextField(30);

    TurnoFormularioPanel(List<String> estados) {
        cboEstado = new JComboBox<>(estados.toArray(new String[0]));
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Datos del Turno"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        agregarFilaCompleta(gbc, 0, "Paciente:", cboPaciente);
        agregarFilaCompleta(gbc, 1, "Odontologo:", cboOdontologo);
        agregarFilaCompleta(gbc, 2, "Secretaria:", cboSecretaria);
        agregarFilaFechaHora(gbc);
        agregarFilaCompleta(gbc, 4, "Motivo:", txtMotivo);

        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.weightx = 0;
        add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        add(cboEstado, gbc);
    }

    void actualizarActores(List<PacienteDatos> pacientes,
                           List<OdontologoDatos> odontologos,
                           List<SecretariaDatos> secretarias) {
        cboPaciente.removeAllItems();
        for (PacienteDatos paciente : pacientes) {
            cboPaciente.addItem(new OpcionEntidad(paciente.getId(), paciente.getId() + " - "
                    + paciente.getNombre() + " " + paciente.getApellido()));
        }

        cboOdontologo.removeAllItems();
        for (OdontologoDatos odontologo : odontologos) {
            cboOdontologo.addItem(new OpcionEntidad(odontologo.getId(), odontologo.getId() + " - "
                    + odontologo.getNombre() + " " + odontologo.getApellido()
                    + " (" + odontologo.getEspecialidad() + ")"));
        }

        cboSecretaria.removeAllItems();
        for (SecretariaDatos secretaria : secretarias) {
            cboSecretaria.addItem(new OpcionEntidad(secretaria.getId(), secretaria.getId() + " - "
                    + secretaria.getNombre() + " " + secretaria.getApellido()));
        }
    }

    boolean tieneActoresDisponibles() {
        return cboPaciente.getItemCount() > 0
                && cboOdontologo.getItemCount() > 0
                && cboSecretaria.getItemCount() > 0;
    }

    TurnoRegistro leerRegistro() {
        OpcionEntidad paciente = opcionSeleccionada(cboPaciente, "Seleccione un paciente.");
        OpcionEntidad odontologo = opcionSeleccionada(cboOdontologo, "Seleccione un odontologo.");
        OpcionEntidad secretaria = opcionSeleccionada(cboSecretaria, "Seleccione una secretaria.");

        TurnoRegistro datos = new TurnoRegistro();
        datos.setIdPaciente(paciente.getId());
        datos.setIdOdontologo(odontologo.getId());
        datos.setIdSecretaria(secretaria.getId());
        datos.setFecha(LocalDate.parse(txtFecha.getText().trim()));
        datos.setHora(LocalTime.parse(txtHora.getText().trim()));
        datos.setMotivoConsulta(txtMotivo.getText().trim());
        return datos;
    }

    TurnoEdicion crearEdicion(Long idTurno) {
        TurnoRegistro registro = leerRegistro();
        TurnoEdicion edicion = new TurnoEdicion();
        edicion.setIdTurno(idTurno);
        edicion.setIdOdontologo(registro.getIdOdontologo());
        edicion.setIdSecretaria(registro.getIdSecretaria());
        edicion.setFecha(registro.getFecha());
        edicion.setHora(registro.getHora());
        edicion.setMotivoConsulta(registro.getMotivoConsulta());
        edicion.setEstado((String) cboEstado.getSelectedItem());
        return edicion;
    }

    void cargar(TurnoDatos turno) {
        seleccionarPorId(cboPaciente, turno.getIdPaciente());
        seleccionarPorId(cboOdontologo, turno.getIdOdontologo());
        seleccionarPorId(cboSecretaria, turno.getIdSecretaria());
        txtFecha.setText(turno.getFecha().toString());
        txtHora.setText(turno.getHora().toString());
        txtMotivo.setText(turno.getMotivoConsulta());
        cboEstado.setSelectedItem(turno.getEstado());
        cboPaciente.setEnabled(false);
    }

    void limpiar() {
        cboPaciente.setEnabled(true);
        seleccionarPrimero(cboPaciente);
        seleccionarPrimero(cboOdontologo);
        seleccionarPrimero(cboSecretaria);
        txtFecha.setText("");
        txtHora.setText("");
        txtMotivo.setText("");
        seleccionarPrimero(cboEstado);
    }

    private OpcionEntidad opcionSeleccionada(JComboBox<OpcionEntidad> combo, String mensaje) {
        int indice = combo.getSelectedIndex();
        if (indice < 0) {
            throw new IllegalStateException(mensaje);
        }
        return combo.getItemAt(indice);
    }

    private void seleccionarPorId(JComboBox<OpcionEntidad> combo, Long id) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).getId().equals(id)) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void seleccionarPrimero(JComboBox<?> combo) {
        if (combo.getItemCount() > 0) {
            combo.setSelectedIndex(0);
        }
    }

    private void agregarFilaCompleta(GridBagConstraints gbc,
                                     int fila,
                                     String etiqueta,
                                     java.awt.Component componente) {
        gbc.gridy = fila;
        gbc.gridx = 0;
        gbc.weightx = 0;
        add(new JLabel(etiqueta), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.gridwidth = 3;
        add(componente, gbc);
        gbc.gridwidth = 1;
    }

    private void agregarFilaFechaHora(GridBagConstraints gbc) {
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.weightx = 0;
        add(new JLabel("Fecha (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        add(txtFecha, gbc);
        gbc.gridx = 2;
        gbc.weightx = 0;
        add(new JLabel("Hora (HH:mm):"), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1;
        add(txtHora, gbc);
    }
}
