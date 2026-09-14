package view;

import dto.TurnoEdicion;
import dto.TurnoRegistro;
import entity.EstadoTurno;
import entity.Odontologo;
import entity.Paciente;
import entity.Secretaria;
import entity.Turno;

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

final class TurnoFormularioPanel extends JPanel {

    private final JComboBox<OpcionEntidad<Paciente>> cboPaciente = new JComboBox<>();
    private final JComboBox<OpcionEntidad<Odontologo>> cboOdontologo = new JComboBox<>();
    private final JComboBox<OpcionEntidad<Secretaria>> cboSecretaria = new JComboBox<>();
    private final JComboBox<EstadoTurno> cboEstado = new JComboBox<>(EstadoTurno.values());
    private final JTextField txtFecha = new JTextField(12);
    private final JTextField txtHora = new JTextField(8);
    private final JTextField txtMotivo = new JTextField(30);

    TurnoFormularioPanel() {
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

    void actualizarActores(List<Paciente> pacientes,
                           List<Odontologo> odontologos,
                           List<Secretaria> secretarias) {
        cboPaciente.removeAllItems();
        for (Paciente paciente : pacientes) {
            cboPaciente.addItem(new OpcionEntidad<>(
                    paciente, paciente.getId() + " - " + paciente.getNombreCompleto()));
        }

        cboOdontologo.removeAllItems();
        for (Odontologo odontologo : odontologos) {
            cboOdontologo.addItem(new OpcionEntidad<>(
                    odontologo,
                    odontologo.getId() + " - " + odontologo.getNombreCompleto()
                            + " (" + odontologo.getEspecialidad().getDescripcion() + ")"));
        }

        cboSecretaria.removeAllItems();
        for (Secretaria secretaria : secretarias) {
            cboSecretaria.addItem(new OpcionEntidad<>(
                    secretaria, secretaria.getId() + " - " + secretaria.getNombreCompleto()));
        }
    }

    boolean tieneActoresDisponibles() {
        return cboPaciente.getItemCount() > 0
                && cboOdontologo.getItemCount() > 0
                && cboSecretaria.getItemCount() > 0;
    }

    TurnoRegistro leerRegistro() {
        OpcionEntidad<Paciente> paciente = opcionSeleccionada(cboPaciente, "Seleccione un paciente.");
        OpcionEntidad<Odontologo> odontologo =
                opcionSeleccionada(cboOdontologo, "Seleccione un odontologo.");
        OpcionEntidad<Secretaria> secretaria =
                opcionSeleccionada(cboSecretaria, "Seleccione una secretaria.");

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
        edicion.setEstado((EstadoTurno) cboEstado.getSelectedItem());
        return edicion;
    }

    void cargar(Turno turno) {
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
        cboEstado.setSelectedItem(EstadoTurno.PENDIENTE);
    }

    private <T extends entity.Persona> OpcionEntidad<T> opcionSeleccionada(
            JComboBox<OpcionEntidad<T>> combo,
            String mensaje) {
        int indice = combo.getSelectedIndex();
        if (indice < 0) {
            throw new IllegalStateException(mensaje);
        }
        return combo.getItemAt(indice);
    }

    private <T extends entity.Persona> void seleccionarPorId(
            JComboBox<OpcionEntidad<T>> combo,
            Long id) {
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
