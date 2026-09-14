package persistence;

import java.util.ArrayList;
import java.util.List;




// Clase para manejar el formato de las líneas en los archivos de texto.
// Se encarga de unir campos en una línea y de parsear líneas en campos,
//  manejando correctamente los caracteres especiales y el separador.
// Es final class para evitar que sea extendida, ya que su funcionalidad es específica y 
// no debería ser modificada mediante herencia.

final class FormatoLinea {

    private static final String SEPARADOR = ";";

    private FormatoLinea() {
    }

    static String unir(Object... campos) {
        StringBuilder linea = new StringBuilder();
        for (int i = 0; i < campos.length; i++) {
            if (i > 0) {
                linea.append(SEPARADOR);
            }
            linea.append(escapar(campos[i]));
        }
        return linea.toString();
    }

    static List<String> parsear(String linea) {
        List<String> campos = new ArrayList<>();
        StringBuilder campoActual = new StringBuilder();
        int indice = 0;

        while (indice < linea.length()) {
            char caracter = linea.charAt(indice);
            if (caracter == '\\' && indice + 1 < linea.length()) {
                agregarCaracterEscapado(campoActual, linea.charAt(indice + 1));
                indice += 2;
            } else if (caracter == ';') {
                campos.add(campoActual.toString());
                campoActual.setLength(0);
                indice++;
            } else {
                campoActual.append(caracter);
                indice++;
            }
        }

        campos.add(campoActual.toString());
        return campos;
    }

    private static String escapar(Object valor) {
        String texto = String.valueOf(valor);
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < texto.length(); i++) {
            char caracter = texto.charAt(i);
            switch (caracter) {
                case '\\':
                    resultado.append("\\\\");
                    break;
                case ';':
                    resultado.append("\\;");
                    break;
                case '\n':
                    resultado.append("\\n");
                    break;
                case '\r':
                    resultado.append("\\r");
                    break;
                default:
                    resultado.append(caracter);
            }
        }
        return resultado.toString();
    }

    private static void agregarCaracterEscapado(StringBuilder campo, char caracter) {
        switch (caracter) {
            case '\\':
                campo.append('\\');
                break;
            case ';':
                campo.append(';');
                break;
            case 'n':
                campo.append('\n');
                break;
            case 'r':
                campo.append('\r');
                break;
            default:
                campo.append(caracter);
        }
    }
}
