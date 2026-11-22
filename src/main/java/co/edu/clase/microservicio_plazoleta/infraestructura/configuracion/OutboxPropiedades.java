package co.edu.clase.microservicio_plazoleta.infraestructura.configuracion;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "outbox.despachador")
public class OutboxPropiedades {

    private long intervaloMs = 10000L;
    private long demoraInicialMs = 10000L;

    public long getIntervaloMs() {
        return intervaloMs;
    }

    public void setIntervaloMs(long intervaloMs) {
        this.intervaloMs = intervaloMs;
    }

    public long getDemoraInicialMs() {
        return demoraInicialMs;
    }

    public void setDemoraInicialMs(
            long demoraInicialMs) {

        this.demoraInicialMs = demoraInicialMs;
    }
}