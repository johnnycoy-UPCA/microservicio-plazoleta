package co.edu.clase.microservicio_plazoleta.infraestructura.salida.seguridad;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GeneradorPinSeguroAdaptadorTest {

    @Test
    void debeGenerarPinNumericoDeSeisDigitos() {
        GeneradorPinSeguroAdaptador adaptador =
                new GeneradorPinSeguroAdaptador();

        String pin = adaptador.generar();

        assertNotNull(pin);
        assertTrue(pin.matches("\\d{6}"));
    }
}