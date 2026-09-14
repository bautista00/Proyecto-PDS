package dto;


// DTO para la edición de una secretaria.

public class SecretariaEdicion {

    private final Long idSecretaria;
    private final SecretariaRegistro datos;

    public SecretariaEdicion(Long idSecretaria, SecretariaRegistro datos) {
        this.idSecretaria = idSecretaria;
        this.datos = datos;
    }

    public Long getIdSecretaria() {
        return idSecretaria;
    }

    public SecretariaRegistro getDatos() {
        return datos;
    }
}
