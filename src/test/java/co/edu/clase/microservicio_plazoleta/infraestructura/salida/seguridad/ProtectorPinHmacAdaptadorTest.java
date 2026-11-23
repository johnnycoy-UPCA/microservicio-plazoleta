package co.edu.clase.microservicio_plazoleta.infraestructura.salida.seguridad;

import co.edu.clase.microservicio_plazoleta.infraestructura.configuracion.PinPropiedades;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProtectorPinHmacAdaptadorTest {

    private ProtectorPinHmacAdaptador adaptador;

    @BeforeEach
    void configurar() {
        PinPropiedades propiedades =
                new PinPropiedades();

        propiedades.setSecreto(
                "MDEyMzQ1Njc4OWFiY2RlZjAxMjM0"
                        + "NTY3ODlhYmNkZWY="
        );

        adaptador =
                new ProtectorPinHmacAdaptador(
                        propiedades
                );
    }

    @Test
    void debeProtegerYValidarPin() {
        String protegido =
                adaptador.proteger(
                        100L,
                        "482193"
                );

        assertTrue(
                adaptador.coincide(
                        100L,
                        "482193",
                        protegido
                )
        );

        assertFalse(
                adaptador.coincide(
                        100L,
                        "111111",
                        protegido
                )
        );
    }

    @Test
    void debeGenerarValoresDiferentesPorPedido() {
        String primero =
                adaptador.proteger(
                        100L,
                        "482193"
                );

        String segundo =
                adaptador.proteger(
                        101L,
                        "482193"
                );

        assertNotEquals(primero, segundo);
    }
}