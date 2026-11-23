package co.edu.clase.microservicio_plazoleta.infraestructura.salida.seguridad;

import co.edu.clase.microservicio_plazoleta.dominio.spi.GeneradorPinPuerto;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class GeneradorPinSeguroAdaptador
        implements GeneradorPinPuerto {

    private static final int LIMITE_PIN = 1_000_000;

    private final SecureRandom secureRandom;

    public GeneradorPinSeguroAdaptador() {
        this.secureRandom = new SecureRandom();
    }

    @Override
    public String generar() {
        int valor = secureRandom.nextInt(
                LIMITE_PIN
        );

        return String.format("%06d", valor);
    }
}