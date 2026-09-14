package view;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.FlowLayout;

final class TurnoFiltrosPanel extends JPanel {

    TurnoFiltrosPanel(Runnable verTodos,
                      Runnable porPaciente,
                      Runnable porOdontologo,
                      Runnable porSecretaria,
                      Runnable porFechas,
                      Runnable porEstado) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 8, 4));
        agregarBoton("Ver Todos", verTodos);
        agregarBoton("Filtrar por Paciente", porPaciente);
        agregarBoton("Filtrar por Odontologo", porOdontologo);
        agregarBoton("Filtrar por Secretaria", porSecretaria);
        agregarBoton("Filtrar por Fechas", porFechas);
        agregarBoton("Filtrar por Estado", porEstado);
    }

    private void agregarBoton(String texto, Runnable accion) {
        JButton boton = new JButton(texto);
        boton.addActionListener(evento -> accion.run());
        add(boton);
    }
}
