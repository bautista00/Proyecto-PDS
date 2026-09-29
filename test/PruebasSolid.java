package test;

import entity.CatalogoCoberturas;
import entity.CoberturaPaciente;
import entity.CreadorEndodoncista;
import entity.CreadorOdontologo;
import entity.CreadorOdontologoGeneral;
import entity.CreadorOrtodoncista;
import entity.Domicilio;
import entity.EspecialidadOdontologica;
import entity.Odontologo;
import entity.OdontologoFactory;
import entity.Paciente;
import persistence.AlmacenamientoLineas;
import persistence.PersistenciaServicio;
import repository.OdontologoRepository;
import repository.PacienteRepository;
import repository.SecretariaRepository;
import repository.TurnoRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PruebasSolid {

    public static void main(String[] args) {
        comprobarCoberturaExtensible();
        comprobarEspecialidadExtensible();
        comprobarAlmacenamientoIntercambiable();
        System.out.println("Pruebas SOLID superadas.");
    }

    private static void comprobarCoberturaExtensible() {
        CoberturaPaciente cobertura = new CoberturaMitadTarifa();
        CatalogoCoberturas catalogo = new CatalogoCoberturas(Arrays.asList(
                CoberturaPaciente.PARTICULAR,
                cobertura));
        comprobar(catalogo.buscar("MITAD_TARIFA") == cobertura,
                "El catalogo no resolvio una cobertura agregada externamente.");
    }

    private static void comprobarEspecialidadExtensible() {
        EspecialidadOdontologica cirugia =
                new EspecialidadOdontologica("CIRUGIA_PRUEBA", "Cirugia de prueba");
        OdontologoFactory factory = new OdontologoFactory(Arrays.asList(
                new CreadorOdontologoPrueba(cirugia)));
        Odontologo odontologo = factory.crear(
                cirugia, "Ana", "Perez", 30111222, "CIR-1");
        comprobar(odontologo.getEspecialidad().equals(cirugia),
                "La factory no utilizo un creador registrado externamente.");
    }

    private static void comprobarAlmacenamientoIntercambiable() {
        CatalogoCoberturas coberturas = new CatalogoCoberturas(Arrays.asList(
                CoberturaPaciente.PARTICULAR,
                CoberturaPaciente.OBRA_SOCIAL));
        OdontologoFactory factory = new OdontologoFactory(Arrays.asList(
                new CreadorOdontologoGeneral(),
                new CreadorOrtodoncista(),
                new CreadorEndodoncista()));
        AlmacenamientoMemoria almacenamiento = new AlmacenamientoMemoria();
        PersistenciaServicio persistencia = new PersistenciaServicio(
                almacenamiento, coberturas, factory);

        PacienteRepository pacientesOrigen = new PacienteRepository();
        pacientesOrigen.guardar(new Paciente(
                "Belen", "Gomez", 30222333, "belen@mail.com",
                new Domicilio("Corrientes", 123, "CABA", "Buenos Aires"),
                CoberturaPaciente.PARTICULAR));
        persistencia.guardar(
                pacientesOrigen,
                new OdontologoRepository(),
                new SecretariaRepository(),
                new TurnoRepository());

        PacienteRepository pacientesDestino = new PacienteRepository();
        persistencia.cargarPacientes(pacientesDestino);
        comprobar(pacientesDestino.listarTodos().size() == 1,
                "La persistencia no pudo trabajar con un almacenamiento alternativo.");
    }

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
    }

    private static final class CoberturaMitadTarifa implements CoberturaPaciente {

        @Override
        public String getCodigo() {
            return "MITAD_TARIFA";
        }

        @Override
        public String getDescripcion() {
            return "Mitad de tarifa";
        }

        @Override
        public Double calcularMonto(Odontologo odontologo) {
            return odontologo.getTarifaBase() / 2;
        }
    }

    private static final class CreadorOdontologoPrueba implements CreadorOdontologo {

        private final EspecialidadOdontologica especialidad;

        private CreadorOdontologoPrueba(EspecialidadOdontologica especialidad) {
            this.especialidad = especialidad;
        }

        @Override
        public EspecialidadOdontologica getEspecialidad() {
            return especialidad;
        }

        @Override
        public Odontologo crear(String nombre, String apellido, Integer dni, String matricula) {
            return new OdontologoPrueba(nombre, apellido, dni, matricula, especialidad);
        }

        @Override
        public Odontologo rehidratar(Long id,
                                     String nombre,
                                     String apellido,
                                     Integer dni,
                                     String matricula) {
            return new OdontologoPrueba(id, nombre, apellido, dni, matricula, especialidad);
        }
    }

    private static final class OdontologoPrueba extends Odontologo {

        private OdontologoPrueba(String nombre,
                                 String apellido,
                                 Integer dni,
                                 String matricula,
                                 EspecialidadOdontologica especialidad) {
            super(nombre, apellido, dni, matricula, especialidad);
        }

        private OdontologoPrueba(Long id,
                                 String nombre,
                                 String apellido,
                                 Integer dni,
                                 String matricula,
                                 EspecialidadOdontologica especialidad) {
            super(id, nombre, apellido, dni, matricula, especialidad);
        }

        @Override
        public Double getTarifaBase() {
            return 90000.0;
        }

        @Override
        public boolean puedeAtender(String motivo) {
            return true;
        }
    }

    private static final class AlmacenamientoMemoria implements AlmacenamientoLineas {

        private final Map<String, List<String>> archivos = new HashMap<>();

        @Override
        public List<String> leerLineas(String ruta, String descripcion) {
            return new ArrayList<>(archivos.getOrDefault(ruta, new ArrayList<>()));
        }

        @Override
        public void escribirLineas(String ruta, String descripcion, List<String> lineas) {
            archivos.put(ruta, new ArrayList<>(lineas));
        }
    }
}
