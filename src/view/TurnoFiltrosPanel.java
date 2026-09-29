package view;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.util.List;

// Muestra las acciones de filtro recibidas. El panel no necesita cambiar cuando se agrega
// una nueva accion: construye sus botones recorriendo la coleccion registrada.
final class TurnoFiltrosPanel extends JPanel {

    TurnoFiltrosPanel(List<AccionFiltroTurno> acciones) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 8, 4));
        for (AccionFiltroTurno accion : acciones) {
            agregarBoton(accion);
        }
    }

    private void agregarBoton(AccionFiltroTurno accion) {
        JButton boton = new JButton(accion.getTexto());
        boton.addActionListener(evento -> accion.ejecutar());
        add(boton);
    }
}
