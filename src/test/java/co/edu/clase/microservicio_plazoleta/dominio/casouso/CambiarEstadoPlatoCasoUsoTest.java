package co.edu.clase.microservicio_plazoleta.dominio.casouso;

import co.edu.clase.microservicio_plazoleta.dominio.excepcion.ExcepcionPlazoleta;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Plato;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Restaurante;
import co.edu.clase.microservicio_plazoleta.dominio.spi.PlatoPersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.RestaurantePersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.util.constantes.CodigosError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CambiarEstadoPlatoCasoUsoTest {

    private static final Long PLATO_ID = 1L;
    private static final Long RESTAURANTE_ID = 5L;
    private static final Long PROPIETARIO_ID = 10L;

    @Mock
    private PlatoPersistenciaPuerto
            platoPersistenciaPuerto;

    @Mock
    private RestaurantePersistenciaPuerto
            restaurantePersistenciaPuerto;

    private CambiarEstadoPlatoCasoUso casoUso;

    @BeforeEach
    void configurar() {
        Clock reloj = Clock.fixed(
                Instant.parse("2026-11-17T14:00:00Z"),
                ZoneId.of("America/Bogota")
        );

        casoUso = new CambiarEstadoPlatoCasoUso(
                platoPersistenciaPuerto,
                restaurantePersistenciaPuerto,
                reloj
        );
    }

    @Test
    void debeDesactivarPlato() {
        Plato plato = crearPlato();
        Restaurante restaurante =
                crearRestaurante(PROPIETARIO_ID);

        when(platoPersistenciaPuerto.buscarPorId(PLATO_ID))
                .thenReturn(Optional.of(plato));

        when(restaurantePersistenciaPuerto.buscarPorId(
                RESTAURANTE_ID
        )).thenReturn(Optional.of(restaurante));

        when(platoPersistenciaPuerto.guardar(any()))
                .thenAnswer(invocacion ->
                        invocacion.getArgument(0)
                );

        Plato resultado = casoUso.cambiarEstado(
                PLATO_ID,
                Boolean.FALSE,
                PROPIETARIO_ID
        );

        assertFalse(resultado.getActivo());
        verify(platoPersistenciaPuerto).guardar(plato);
    }

    @Test
    void debeRechazarEstadoNulo() {
        ExcepcionPlazoleta excepcion = assertThrows(
                ExcepcionPlazoleta.class,
                () -> casoUso.cambiarEstado(
                        PLATO_ID,
                        null,
                        PROPIETARIO_ID
                )
        );

        assertEquals(
                CodigosError.ESTADO_PLATO_OBLIGATORIO,
                excepcion.getCodigo()
        );

        verify(platoPersistenciaPuerto, never())
                .buscarPorId(any());
    }

    @Test
    void debeRechazarPlatoInexistente() {
        when(platoPersistenciaPuerto.buscarPorId(PLATO_ID))
                .thenReturn(Optional.empty());

        ExcepcionPlazoleta excepcion = assertThrows(
                ExcepcionPlazoleta.class,
                () -> casoUso.cambiarEstado(
                        PLATO_ID,
                        Boolean.FALSE,
                        PROPIETARIO_ID
                )
        );

        assertEquals(
                CodigosError.PLATO_NO_ENCONTRADO,
                excepcion.getCodigo()
        );
    }

    @Test
    void debeRechazarRestauranteAjeno() {
        when(platoPersistenciaPuerto.buscarPorId(PLATO_ID))
                .thenReturn(Optional.of(crearPlato()));

        when(restaurantePersistenciaPuerto.buscarPorId(
                RESTAURANTE_ID
        )).thenReturn(Optional.of(
                crearRestaurante(99L)
        ));

        ExcepcionPlazoleta excepcion = assertThrows(
                ExcepcionPlazoleta.class,
                () -> casoUso.cambiarEstado(
                        PLATO_ID,
                        Boolean.FALSE,
                        PROPIETARIO_ID
                )
        );

        assertEquals(
                CodigosError
                        .RESTAURANTE_NO_PERTENECE_PROPIETARIO,
                excepcion.getCodigo()
        );

        verify(platoPersistenciaPuerto, never())
                .guardar(any());
    }

    private Plato crearPlato() {
        Plato plato = new Plato();

        plato.setId(PLATO_ID);
        plato.setRestauranteId(RESTAURANTE_ID);
        plato.setActivo(Boolean.TRUE);

        return plato;
    }

    private Restaurante crearRestaurante(
            Long propietarioId) {

        Restaurante restaurante = new Restaurante();

        restaurante.setId(RESTAURANTE_ID);
        restaurante.setPropietarioId(propietarioId);
        restaurante.setActivo(Boolean.TRUE);

        return restaurante;
    }
}