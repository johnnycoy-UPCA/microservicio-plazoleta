package co.edu.clase.microservicio_plazoleta.dominio.modelo;

public class ConfiguracionPin {

    private final int duracionMinutos;
    private final int maximoIntentos;

    public ConfiguracionPin(
            int duracionMinutos,
            int maximoIntentos) {

        this.duracionMinutos = duracionMinutos;
        this.maximoIntentos = maximoIntentos;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public int getMaximoIntentos() {
        return maximoIntentos;
    }
}