package view;

import dto.SecretariaDatos;

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

// Tabla de secretarias en modo solo lectura. Se extrajo de SecretariaPanel (SRP).
// Al hacer clic en una fila avisa el ID de la secretaria mediante el Consumer que recibe.
final class SecretariaTablaPanel extends JPanel {

    private final DefaultTableModel modelo;
    private final JTable tabla;
    private final Consumer<Long> alSeleccionar;

    SecretariaTablaPanel(Consumer<Long> alSeleccionar) {
        this.alSeleccionar = alSeleccionar;
        modelo = new DefaultTableModel(new String[]{"ID", "Nombre", "Apellido", "DNI"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evento) {
                notificarSeleccion();
            }
        });
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(0, 220));
        setLayout(new BorderLayout());
        add(scroll, BorderLayout.CENTER);
    }

    void cargar(List<SecretariaDatos> secretarias) {
        modelo.setRowCount(0);
        for (SecretariaDatos secretaria : secretarias) {
            modelo.addRow(new Object[]{
                    secretaria.getId(), secretaria.getNombre(),
                    secretaria.getApellido(), secretaria.getDni()
            });
        }
    }

    void seleccionarPorDni(Integer dni) {
        for (int fila = 0; fila < modelo.getRowCount(); fila++) {
            if (modelo.getValueAt(fila, 3).equals(dni)) {
                tabla.setRowSelectionInterval(fila, fila);
                tabla.scrollRectToVisible(tabla.getCellRect(fila, 0, true));
                notificarSeleccion();
                return;
            }
        }
    }

    void limpiarSeleccion() {
        tabla.clearSelection();
    }

    private void notificarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            alSeleccionar.accept((Long) modelo.getValueAt(fila, 0));
        }
    }
}
