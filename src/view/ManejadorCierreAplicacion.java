package view;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public final class ManejadorCierreAplicacion {

    private final JFrame ventana;
    private final Runnable guardarDatos;

    public ManejadorCierreAplicacion(JFrame ventana, Runnable guardarDatos) {
        if (ventana == null || guardarDatos == null) {
            throw new IllegalArgumentException("La ventana y la accion de guardado son obligatorias.");
        }
        this.ventana = ventana;
        this.guardarDatos = guardarDatos;
    }

    public void configurar() {
        ventana.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evento) {
                cerrarAplicacion();
            }
        });
    }

    private void cerrarAplicacion() {
        int opcion = JOptionPane.showConfirmDialog(
                ventana,
                "¿Desea guardar los datos antes de salir?",
                "Salir",
                JOptionPane.YES_NO_CANCEL_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            guardarDatos.run();
            ventana.dispose();
        } else if (opcion == JOptionPane.NO_OPTION) {
            ventana.dispose();
        }
    }
}
