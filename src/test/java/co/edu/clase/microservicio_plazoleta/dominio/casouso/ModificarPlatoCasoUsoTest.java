package co.edu.clase.microservicio_plazoleta.dominio.casouso;

import co.edu.clase.microservicio_plazoleta.dominio.excepcion.ExcepcionPlazoleta;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Categoria;
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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ModificarPlatoCasoUsoTest {

    private static final Long PLATO_ID = 1L;
    private static final Long RESTAURANTE_ID = 5L;
    private static final Long PROPIETARIO_ID = 10L;

    @Mock
    private PlatoPersistenciaPuerto
            platoPersistenciaPuerto;

    @Mock
    private RestaurantePersistenciaPuerto
            restaurantePersistenciaPuerto;

    private ModificarPlatoCasoUso modificarPlatoCasoUso;

    @BeforeEach
    void configurar() {
        Clock reloj = Clock.fixed(
                Instant.parse("2026-11-17T15:00:00Z"),
                ZoneId.of("America/Bogota")
        );

        modificarPlatoCasoUso =
                new ModificarPlatoCasoUso(
                        platoPersistenciaPuerto,
                        restaurantePersistenciaPuerto,
                        reloj
                );
    }

    @Test
    void debeModificarPrecioYDescripcion() {
        Plato plato = crearPlato();

        when(platoPersistenciaPuerto.buscarPorId(PLATO_ID))
                .thenReturn(Optional.of(plato));

        when(restaurantePersistenciaPuerto.buscarPorId(
                RESTAURANTE_ID
        )).thenReturn(Optional.of(crearRestaurante(
                PROPIETARIO_ID
        )));

        when(platoPersistenciaPuerto.guardar(
                any(Plato.class)
        )).thenAnswer(invocacion ->
                invocacion.getArgument(0)
        );

        Plato resultado = modificarPlatoCasoUso.modificar(
                PLATO_ID,
                30000,
                "  Nueva descripción  ",
                PROPIETARIO_ID
        );

        assertEquals(30000, resultado.getPrecio());
        assertEquals(
                "Nueva descripción",
                resultado.getDescripcion()
        );
        assertNotEquals(
                plato.getFechaCreacion(),
                resultado.getFechaActualizacion()
        );
        assertEquals(
                "Arroz con pollo",
                resultado.getNombre()
        );
        assertEquals(
                2L,
                resultado.getCategoria().getId()
        );
        assertEquals(
                RESTAURANTE_ID,
                resultado.getRestauranteId()
        );

        verify(platoPersistenciaPuerto).guardar(plato);
    }

    @Test
    void debeRechazarPlatoInexistente() {
        when(platoPersistenciaPuerto.buscarPorId(PLATO_ID))
                .thenReturn(Optional.empty());

        ExcepcionPlazoleta excepcion = assertThrows(
                ExcepcionPlazoleta.class,
                () -> modificarPlatoCasoUso.modificar(
                        PLATO_ID,
                        30000,
                        "Nueva descripción",
                        PROPIETARIO_ID
                )
        );

        assertEquals(
                CodigosError.PLATO_NO_ENCONTRADO,
                excepcion.getCodigo()
        );
    }

    @Test
    void debeRechazarPrecioNoPositivo() {
        ExcepcionPlazoleta excepcion = assertThrows(
                ExcepcionPlazoleta.class,
                () -> modificarPlatoCasoUso.modificar(
                        PLATO_ID,
                        0,
                        "Nueva descripción",
                        PROPIETARIO_ID
                )
        );

        assertEquals(
                CodigosError.PRECIO_PLATO_INVALIDO,
                excepcion.getCodigo()
        );

        verify(platoPersistenciaPuerto, never())
                .buscarPorId(any());
    }

    @Test
    void debeRechazarRestauranteAjeno() {
        Plato plato = crearPlato();

        when(platoPersistenciaPuerto.buscarPorId(PLATO_ID))
                .thenReturn(Optional.of(plato));

        when(restaurantePersistenciaPuerto.buscarPorId(
                RESTAURANTE_ID
        )).thenReturn(Optional.of(
                crearRestaurante(99L)
        ));

        ExcepcionPlazoleta excepcion = assertThrows(
                ExcepcionPlazoleta.class,
                () -> modificarPlatoCasoUso.modificar(
                        PLATO_ID,
                        30000,
                        "Nueva descripción",
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
        plato.setNombre("Arroz con pollo");
        plato.setDescripcion("Descripción anterior");
        plato.setPrecio(25000);
        plato.setUrlImagen(
                "https://ejemplo.com/arroz-pollo.png"
        );
        plato.setCategoria(new Categoria(
                2L,
                "PLATO_FUERTE",
                "Platos principales",
                Boolean.TRUE
        ));
        plato.setRestauranteId(RESTAURANTE_ID);
        plato.setActivo(Boolean.TRUE);
        plato.setFechaCreacion(
                java.time.LocalDateTime.of(
                        2026, 11, 16, 10, 0
                )
        );
        plato.setFechaActualizacion(
                java.time.LocalDateTime.of(
                        2026, 11, 16, 10, 0
                )
        );

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