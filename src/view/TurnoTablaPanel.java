package view;

import entity.Turno;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.Consumer;

// Tabla de turnos en modo solo lectura (las celdas no se editan).
// Se extrajo de TurnoPanel para separar el armado de la grilla de la lógica de la pantalla.
// Cuando el usuario hace clic en una fila, avisa el ID del turno mediante el Consumer
// que recibe en el constructor: no sabe qué se hace con ese ID.
final class TurnoTablaPanel extends JPanel {

    private final JTable tabla;
    private final DefaultTableModel modelo;

    TurnoTablaPanel(Consumer<Long> alSeleccionarTurno) {
        setLayout(new BorderLayout());
        String[] columnas = {
                "ID", "Paciente", "Odontologo", "Especialidad", "Secretaria",
                "Fecha", "Hora", "Motivo", "Estado"
        };
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int[] anchos = {40, 130, 130, 110, 110, 90, 60, 140, 90};
        for (int i = 0; i < anchos.length; i++) {
            tabla.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evento) {
                int fila = tabla.getSelectedRow();
                if (fila >= 0) {
                    alSeleccionarTurno.accept((Long) modelo.getValueAt(fila, 0));
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(0, 190));
        add(scroll, BorderLayout.CENTER);
    }

    void cargar(List<Turno> turnos) {
        modelo.setRowCount(0);
        for (Turno turno : turnos) {
            modelo.addRow(new Object[]{
                    turno.getId(),
                    turno.getNombrePaciente(),
                    turno.getNombreOdontologo(),
                    turno.getEspecialidadOdontologo().getDescripcion(),
                    turno.getNombreSecretaria(),
                    turno.getFecha().toString(),
                    turno.getHora().toString(),
                    turno.getMotivoConsulta(),
                    turno.getEstado().toString()
            });
        }
    }

    void limpiarSeleccion() {
        tabla.clearSelection();
    }
}
