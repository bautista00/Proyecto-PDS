package entity;

public interface CoberturaPaciente {

    CoberturaPaciente PARTICULAR = new CoberturaParticular();
    CoberturaPaciente OBRA_SOCIAL = new CoberturaObraSocial();

    String getCodigo();

    String getDescripcion();

    Double calcularMonto(Odontologo odontologo);
}
