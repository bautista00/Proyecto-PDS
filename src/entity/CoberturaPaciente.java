package entity;

// Interfaz Strategy: define cómo se calcula el monto que paga el paciente por un turno.
// CoberturaParticular y CoberturaObraSocial son las estrategias concretas y Paciente es el contexto.
public interface CoberturaPaciente {

    CoberturaPaciente PARTICULAR = new CoberturaParticular();
    CoberturaPaciente OBRA_SOCIAL = new CoberturaObraSocial();

    String getCodigo();

    String getDescripcion();

    Double calcularMonto(Odontologo odontologo);
}
