package view;

import controller.OdontologoController;
import controller.PacienteController;
import controller.SecretariaController;
import controller.TurnoController;
import entity.EstadoTurno;
import entity.Odontologo;
import entity.Paciente;
import entity.Secretaria;
import entity.Turno;
import exception.ClinicaException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class TurnoPanel extends JPanel {

    private final TurnoController turnoController;
    private final PacienteController pacienteController;
    private final OdontologoController odontologoController;
    private final SecretariaController secretariaController;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private JComboBox<String> cboPaciente, cboOdontologo, cboSecretaria, cboEstado;
    private JTextField txtFecha, txtHora, txtMotivo;
    private Long idSeleccionado;

    private List<Paciente> listaPacientes     = new ArrayList<>();
    private List<Odontologo> listaOdontologos = new ArrayList<>();
    private List<Secretaria> listaSecretarias = new ArrayList<>();

    public TurnoPanel(TurnoController turnoController,
                      PacienteController pacienteController,
                      OdontologoController odontologoController,
                      SecretariaController secretariaController) {
        this.turnoController       = turnoController;
        this.pacienteController    = pacienteController;
        this.odontologoController  = odontologoController;
        this.secretariaController  = secretariaController;

        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(crearPanelTabla(), BorderLayout.NORTH);
        add(crearPanelFormulario(), BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);

        actualizarCombos();
        cargarTabla(turnoController.listarTurnos());
    }

    // Llamado cuando se selecciona la pestaña Turnos, para reflejar cambios de otras pestañas
    public void actualizarCombos() {
        try { listaPacientes   = pacienteController.listarPacientesOrdenadosPorApellido(); }
        catch (ClinicaException ignored) { listaPacientes = new ArrayList<>(); }

        try { listaOdontologos = odontologoController.listarOdontologos(); }
        catch (ClinicaException ignored) { listaOdontologos = new ArrayList<>(); }

        try { listaSecretarias = secretariaController.listarSecretarias(); }
        catch (ClinicaException ignored) { listaSecretarias = new ArrayList<>(); }

        cboPaciente.removeAllItems();
        for (Paciente p : listaPacientes) {
            cboPaciente.addItem(p.getId() + " - " + p.getNombre() + " " + p.getApellido());
        }

        cboOdontologo.removeAllItems();
        for (Odontologo o : listaOdontologos) {
            cboOdontologo.addItem(o.getId() + " - " + o.getNombre() + " " + o.getApellido()
                    + " (" + o.getEspecialidad() + ")");
        }

        cboSecretaria.removeAllItems();
        for (Secretaria s : listaSecretarias) {
            cboSecretaria.addItem(s.getId() + " - " + s.getNombre() + " " + s.getApellido());
        }
    }

    private JScrollPane crearPanelTabla() {
        String[] columnas = {"ID", "Paciente", "Odontólogo", "Especialidad", "Secretaria", "Fecha", "Hora", "Motivo", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(130);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(130);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(110);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(110);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(90);
        tabla.getColumnModel().getColumn(6).setPreferredWidth(60);
        tabla.getColumnModel().getColumn(7).setPreferredWidth(140);
        tabla.getColumnModel().getColumn(8).setPreferredWidth(90);
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { cargarFilaSeleccionada(); }
        });
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(0, 190));
        return scroll;
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Turno"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cboPaciente   = new JComboBox<>();
        cboOdontologo = new JComboBox<>();
        cboSecretaria = new JComboBox<>();
        cboEstado = new JComboBox<>(new String[]{"PENDIENTE", "CONFIRMADO", "CANCELADO", "COMPLETADO"});
        txtFecha = new JTextField(12);
        txtHora = new JTextField(8);
        txtMotivo = new JTextField(30);

        agregarFilaCompleta(panel, gbc, 0, "Paciente:", cboPaciente);
        agregarFilaCompleta(panel, gbc, 1, "Odontólogo:", cboOdontologo);
        agregarFilaCompleta(panel, gbc, 2, "Secretaria:", cboSecretaria);
        agregarFilaFechaHora(panel, gbc);
        agregarFilaMotivo(panel, gbc);
        agregarFilaEstado(panel, gbc);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 4, 4));
        panel.add(crearFilaAccionesPrincipales());
        panel.add(crearFilaFiltros());
        return panel;
    }

    private void agregarFilaCompleta(JPanel panel,
                                     GridBagConstraints gbc,
                                     int fila,
                                     String etiqueta,
                                     JComponent componente) {
        gbc.gridy = fila;
        gbc.gridx = 0;
        gbc.weightx = 0;
        panel.add(new JLabel(etiqueta), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.gridwidth = 3;
        panel.add(componente, gbc);
        gbc.gridwidth = 1;
    }

    private void agregarFilaFechaHora(JPanel panel, GridBagConstraints gbc) {
        gbc.gridy = 3;
        gbc.gridx = 0; gbc.weightx = 0; panel.add(new JLabel("Fecha (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; panel.add(txtFecha, gbc);
        gbc.gridx = 2; gbc.weightx = 0; panel.add(new JLabel("Hora (HH:mm):"), gbc);
        gbc.gridx = 3; gbc.weightx = 1; panel.add(txtHora, gbc);
    }

    private void agregarFilaMotivo(JPanel panel, GridBagConstraints gbc) {
        gbc.gridy = 4;
        gbc.gridx = 0; gbc.weightx = 0; panel.add(new JLabel("Motivo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; gbc.gridwidth = 3; panel.add(txtMotivo, gbc);
        gbc.gridwidth = 1;
    }

    private void agregarFilaEstado(JPanel panel, GridBagConstraints gbc) {
        gbc.gridy = 5;
        gbc.gridx = 0; gbc.weightx = 0; panel.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; panel.add(cboEstado, gbc);
    }

    private JPanel crearFilaAccionesPrincipales() {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));

        JButton btnNuevo = new JButton("Nuevo");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnMonto = new JButton("Calcular Monto");
        JButton btnLimpiar = new JButton("Limpiar");

        btnNuevo.addActionListener(e -> onNuevoAction());
        btnGuardar.addActionListener(e -> onGuardarAction());
        btnEliminar.addActionListener(e -> onEliminarAction());
        btnMonto.addActionListener(e -> onCalcularMontoAction());
        btnLimpiar.addActionListener(e -> onLimpiarAction());

        fila.add(btnNuevo);
        fila.add(btnGuardar);
        fila.add(btnEliminar);
        fila.add(btnMonto);
        fila.add(btnLimpiar);
        return fila;
    }

    private JPanel crearFilaFiltros() {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));

        JButton btnVerTodos = new JButton("Ver Todos");
        JButton btnPorPaciente = new JButton("Filtrar por Paciente");
        JButton btnPorOdontologo = new JButton("Filtrar por Odontólogo");
        JButton btnPorSecretaria = new JButton("Filtrar por Secretaria");
        JButton btnPorFechas = new JButton("Filtrar por Fechas");
        JButton btnPorEstado = new JButton("Filtrar por Estado");

        btnVerTodos.addActionListener(e -> onVerTodosAction());
        btnPorPaciente.addActionListener(e -> onFiltrarPorPacienteAction());
        btnPorOdontologo.addActionListener(e -> onFiltrarPorOdontologoAction());
        btnPorSecretaria.addActionListener(e -> onFiltrarPorSecretariaAction());
        btnPorFechas.addActionListener(e -> onFiltrarPorFechasAction());
        btnPorEstado.addActionListener(e -> onFiltrarPorEstadoAction());

        fila.add(btnVerTodos);
        fila.add(btnPorPaciente);
        fila.add(btnPorOdontologo);
        fila.add(btnPorSecretaria);
        fila.add(btnPorFechas);
        fila.add(btnPorEstado);
        return fila;
    }

    private void cargarTabla(List<Turno> turnos) {
        modeloTabla.setRowCount(0);
        for (Turno t : turnos) {
            modeloTabla.addRow(new Object[]{
                    t.getId(),
                    t.getPaciente().getNombre() + " " + t.getPaciente().getApellido(),
                    t.getOdontologo().getNombre() + " " + t.getOdontologo().getApellido(),
                    t.getOdontologo().getEspecialidad(),
                    t.getSecretaria().getNombre() + " " + t.getSecretaria().getApellido(),
                    t.getFecha().toString(),
                    t.getHora().toString(),
                    t.getMotivoConsulta(),
                    t.getEstado().toString()
            });
        }
    }

    private void cargarFilaSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        idSeleccionado = (Long) modeloTabla.getValueAt(fila, 0);
        try {
            Turno t = turnoController.buscarTurnoPorId(idSeleccionado);
            seleccionarEnCombo(cboPaciente,   listaPacientes,   t.getPaciente().getId());
            seleccionarEnCombo(cboOdontologo, listaOdontologos, t.getOdontologo().getId());
            seleccionarEnCombo(cboSecretaria, listaSecretarias, t.getSecretaria().getId());
            txtFecha.setText(t.getFecha().toString());
            txtHora.setText(t.getHora().toString());
            txtMotivo.setText(t.getMotivoConsulta());
            cboEstado.setSelectedItem(t.getEstado().toString());
            cboPaciente.setEnabled(false); // paciente no se puede cambiar al editar
        } catch (ClinicaException e) {
            mostrarError(e.getMessage());
        }
    }

    private void onNuevoAction() {
        limpiarFormulario();
    }

    private void onGuardarAction() {
        guardar();
    }

    private void onEliminarAction() {
        eliminar();
    }

    private void onCalcularMontoAction() {
        calcularMonto();
    }

    private void onLimpiarAction() {
        limpiarFormulario();
    }

    private void onVerTodosAction() {
        cargarTabla(turnoController.listarTurnos());
    }

    private void onFiltrarPorPacienteAction() {
        filtrarPorPaciente();
    }

    private void onFiltrarPorOdontologoAction() {
        filtrarPorOdontologo();
    }

    private void onFiltrarPorSecretariaAction() {
        filtrarPorSecretaria();
    }

    private void onFiltrarPorFechasAction() {
        filtrarPorFechas();
    }

    private void onFiltrarPorEstadoAction() {
        filtrarPorEstado();
    }

    private void seleccionarEnCombo(JComboBox<String> combo, List<?> lista, Long id) {
        for (int i = 0; i < lista.size(); i++) {
            if (combo.getItemAt(i).startsWith(id + " - ")) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void guardar() {
        if (cboPaciente.getItemCount() == 0 || cboOdontologo.getItemCount() == 0 || cboSecretaria.getItemCount() == 0) {
            mostrarError("Registre pacientes, odontólogos y secretarias antes de crear un turno.");
            return;
        }
        try {
            int idxPaciente   = cboPaciente.getSelectedIndex();
            int idxOdontologo = cboOdontologo.getSelectedIndex();
            int idxSecretaria = cboSecretaria.getSelectedIndex();

            if (idxPaciente < 0 || idxOdontologo < 0 || idxSecretaria < 0) {
                mostrarError("Seleccione paciente, odontólogo y secretaria.");
                return;
            }

            Long idPaciente   = listaPacientes.get(idxPaciente).getId();
            Long idOdontologo = listaOdontologos.get(idxOdontologo).getId();
            Long idSecretaria = listaSecretarias.get(idxSecretaria).getId();
            LocalDate fecha   = LocalDate.parse(txtFecha.getText().trim());
            LocalTime hora    = LocalTime.parse(txtHora.getText().trim());
            String motivo     = txtMotivo.getText().trim();
            EstadoTurno estado = EstadoTurno.valueOf(cboEstado.getSelectedItem().toString());

            if (idSeleccionado == null) {
                turnoController.registrarTurno(idPaciente, idOdontologo, idSecretaria, fecha, hora, motivo);
                JOptionPane.showMessageDialog(this, "Turno registrado correctamente.");
            } else {
                turnoController.actualizarTurno(idSeleccionado, idOdontologo, idSecretaria, fecha, hora, motivo, estado);
                JOptionPane.showMessageDialog(this, "Turno actualizado correctamente.");
            }
            limpiarFormulario();
            cargarTabla(turnoController.listarTurnos());
        } catch (DateTimeParseException e) {
            mostrarError("Formato incorrecto. Fecha: yyyy-MM-dd  —  Hora: HH:mm");
        } catch (ClinicaException e) {
            mostrarError(e.getMessage());
        }
    }

    private void eliminar() {
        if (idSeleccionado == null) {
            mostrarError("Seleccione un turno de la tabla.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el turno seleccionado?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                turnoController.eliminarTurno(idSeleccionado);
                JOptionPane.showMessageDialog(this, "Turno eliminado correctamente.");
                limpiarFormulario();
                cargarTabla(turnoController.listarTurnos());
            } catch (ClinicaException e) {
                mostrarError(e.getMessage());
            }
        }
    }

    private void calcularMonto() {
        if (idSeleccionado == null) {
            mostrarError("Seleccione un turno de la tabla.");
            return;
        }
        try {
            Double monto = turnoController.calcularMontoTurno(idSeleccionado);
            JOptionPane.showMessageDialog(this,
                    String.format("Monto del turno #%d:  $%.2f", idSeleccionado, monto),
                    "Monto del Turno", JOptionPane.INFORMATION_MESSAGE);
        } catch (ClinicaException e) {
            mostrarError(e.getMessage());
        }
    }

    private void filtrarPorPaciente() {
        String input = JOptionPane.showInputDialog(this, "ID del Paciente:");
        if (input == null || input.trim().isEmpty()) return;
        try {
            cargarTabla(turnoController.listarTurnosPorPaciente(Long.parseLong(input.trim())));
        } catch (NumberFormatException e) {
            mostrarError("Ingrese un ID numérico.");
        } catch (ClinicaException e) {
            mostrarError(e.getMessage());
        }
    }

    private void filtrarPorOdontologo() {
        String input = JOptionPane.showInputDialog(this, "ID del Odontólogo:");
        if (input == null || input.trim().isEmpty()) return;
        try {
            cargarTabla(turnoController.listarTurnosPorOdontologo(Long.parseLong(input.trim())));
        } catch (NumberFormatException e) {
            mostrarError("Ingrese un ID numérico.");
        } catch (ClinicaException e) {
            mostrarError(e.getMessage());
        }
    }

    private void filtrarPorSecretaria() {
        String input = JOptionPane.showInputDialog(this, "ID de la Secretaria:");
        if (input == null || input.trim().isEmpty()) return;
        try {
            cargarTabla(turnoController.listarTurnosPorSecretaria(Long.parseLong(input.trim())));
        } catch (NumberFormatException e) {
            mostrarError("Ingrese un ID numérico.");
        } catch (ClinicaException e) {
            mostrarError(e.getMessage());
        }
    }

    private void filtrarPorEstado() {
        EstadoTurno seleccionado = (EstadoTurno) JOptionPane.showInputDialog(
                this,
                "Seleccione el estado a filtrar:",
                "Filtrar por Estado",
                JOptionPane.QUESTION_MESSAGE,
                null,
                EstadoTurno.values(),
                EstadoTurno.PENDIENTE);
        if (seleccionado == null) return; // el usuario canceló
        try {
            cargarTabla(turnoController.listarTurnosPorEstado(seleccionado));
        } catch (ClinicaException e) {
            mostrarError(e.getMessage());
        }
    }

    private void filtrarPorFechas() {
        String desde = JOptionPane.showInputDialog(this, "Fecha desde (yyyy-MM-dd):");
        if (desde == null || desde.trim().isEmpty()) return;
        String hasta = JOptionPane.showInputDialog(this, "Fecha hasta (yyyy-MM-dd):");
        if (hasta == null || hasta.trim().isEmpty()) return;
        try {
            cargarTabla(turnoController.buscarTurnosPorRangoFechas(
                    LocalDate.parse(desde.trim()), LocalDate.parse(hasta.trim())));
        } catch (DateTimeParseException e) {
            mostrarError("Formato de fecha: yyyy-MM-dd");
        } catch (ClinicaException e) {
            mostrarError(e.getMessage());
        }
    }

    private void limpiarFormulario() {
        idSeleccionado = null;
        cboPaciente.setEnabled(true);
        if (cboPaciente.getItemCount()   > 0) cboPaciente.setSelectedIndex(0);
        if (cboOdontologo.getItemCount() > 0) cboOdontologo.setSelectedIndex(0);
        if (cboSecretaria.getItemCount() > 0) cboSecretaria.setSelectedIndex(0);
        txtFecha.setText("");
        txtHora.setText("");
        txtMotivo.setText("");
        cboEstado.setSelectedIndex(0);
        tabla.clearSelection();
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
