package co.edu.clase.microservicio_plazoleta.dominio.casouso;

import co.edu.clase.microservicio_plazoleta.dominio.excepcion.ExcepcionPlazoleta;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Categoria;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Plato;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Restaurante;
import co.edu.clase.microservicio_plazoleta.dominio.spi.CategoriaPersistenciaPuerto;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrearPlatoCasoUsoTest {

    private static final Long PROPIETARIO_ID = 10L;
    private static final Long RESTAURANTE_ID = 1L;
    private static final Long CATEGORIA_ID = 2L;

    @Mock
    private PlatoPersistenciaPuerto
            platoPersistenciaPuerto;

    @Mock
    private RestaurantePersistenciaPuerto
            restaurantePersistenciaPuerto;

    @Mock
    private CategoriaPersistenciaPuerto
            categoriaPersistenciaPuerto;

    private CrearPlatoCasoUso crearPlatoCasoUso;

    @BeforeEach
    void configurar() {
        Clock reloj = Clock.fixed(
                Instant.parse("2026-11-16T17:00:00Z"),
                ZoneId.of("America/Bogota")
        );

        crearPlatoCasoUso = new CrearPlatoCasoUso(
                platoPersistenciaPuerto,
                restaurantePersistenciaPuerto,
                categoriaPersistenciaPuerto,
                reloj
        );
    }

    @Test
    void debeCrearPlatoCorrectamente() {
        Plato plato = crearPlatoValido();

        when(restaurantePersistenciaPuerto.buscarPorId(
                RESTAURANTE_ID
        )).thenReturn(Optional.of(
                crearRestauranteActivo(PROPIETARIO_ID)
        ));

        when(categoriaPersistenciaPuerto
                .buscarActivaPorId(CATEGORIA_ID))
                .thenReturn(Optional.of(
                        crearCategoriaActiva()
                ));

        when(platoPersistenciaPuerto.guardar(
                any(Plato.class)
        )).thenAnswer(invocacion -> {
            Plato guardado = invocacion.getArgument(0);
            guardado.setId(1L);
            return guardado;
        });

        Plato resultado = crearPlatoCasoUso.crear(
                plato,
                PROPIETARIO_ID
        );

        assertEquals(1L, resultado.getId());
        assertEquals(
                "Arroz con pollo",
                resultado.getNombre()
        );
        assertEquals(
                CATEGORIA_ID,
                resultado.getCategoria().getId()
        );
        assertTrue(resultado.getActivo());
        assertNotNull(resultado.getFechaCreacion());
        assertNotNull(resultado.getFechaActualizacion());

        verify(platoPersistenciaPuerto).guardar(plato);
    }

    @Test
    void debeRechazarPrecioCero() {
        Plato plato = crearPlatoValido();
        plato.setPrecio(0);

        ExcepcionPlazoleta excepcion = assertThrows(
                ExcepcionPlazoleta.class,
                () -> crearPlatoCasoUso.crear(
                        plato,
                        PROPIETARIO_ID
                )
        );

        assertEquals(
                CodigosError.PRECIO_PLATO_INVALIDO,
                excepcion.getCodigo()
        );

        verify(platoPersistenciaPuerto, never())
                .guardar(any());
    }

    @Test
    void debeRechazarRestauranteInexistente() {
        Plato plato = crearPlatoValido();

        when(restaurantePersistenciaPuerto.buscarPorId(
                RESTAURANTE_ID
        )).thenReturn(Optional.empty());

        ExcepcionPlazoleta excepcion = assertThrows(
                ExcepcionPlazoleta.class,
                () -> crearPlatoCasoUso.crear(
                        plato,
                        PROPIETARIO_ID
                )
        );

        assertEquals(
                CodigosError.RESTAURANTE_NO_ENCONTRADO,
                excepcion.getCodigo()
        );

        verify(platoPersistenciaPuerto, never())
                .guardar(any());
    }

    @Test
    void debeRechazarRestauranteInactivo() {
        Plato plato = crearPlatoValido();

        Restaurante restaurante =
                crearRestauranteActivo(PROPIETARIO_ID);

        restaurante.setActivo(Boolean.FALSE);

        when(restaurantePersistenciaPuerto.buscarPorId(
                RESTAURANTE_ID
        )).thenReturn(Optional.of(restaurante));

        ExcepcionPlazoleta excepcion = assertThrows(
                ExcepcionPlazoleta.class,
                () -> crearPlatoCasoUso.crear(
                        plato,
                        PROPIETARIO_ID
                )
        );

        assertEquals(
                CodigosError.RESTAURANTE_INACTIVO,
                excepcion.getCodigo()
        );
    }

    @Test
    void debeRechazarRestauranteDeOtroPropietario() {
        Plato plato = crearPlatoValido();

        when(restaurantePersistenciaPuerto.buscarPorId(
                RESTAURANTE_ID
        )).thenReturn(Optional.of(
                crearRestauranteActivo(99L)
        ));

        ExcepcionPlazoleta excepcion = assertThrows(
                ExcepcionPlazoleta.class,
                () -> crearPlatoCasoUso.crear(
                        plato,
                        PROPIETARIO_ID
                )
        );

        assertEquals(
                CodigosError
                        .RESTAURANTE_NO_PERTENECE_PROPIETARIO,
                excepcion.getCodigo()
        );

        verify(categoriaPersistenciaPuerto, never())
                .buscarActivaPorId(any());
    }

    @Test
    void debeRechazarCategoriaInexistente() {
        Plato plato = crearPlatoValido();

        when(restaurantePersistenciaPuerto.buscarPorId(
                RESTAURANTE_ID
        )).thenReturn(Optional.of(
                crearRestauranteActivo(PROPIETARIO_ID)
        ));

        when(categoriaPersistenciaPuerto
                .buscarActivaPorId(CATEGORIA_ID))
                .thenReturn(Optional.empty());

        ExcepcionPlazoleta excepcion = assertThrows(
                ExcepcionPlazoleta.class,
                () -> crearPlatoCasoUso.crear(
                        plato,
                        PROPIETARIO_ID
                )
        );

        assertEquals(
                CodigosError.CATEGORIA_NO_ENCONTRADA,
                excepcion.getCodigo()
        );

        verify(platoPersistenciaPuerto, never())
                .guardar(any());
    }

    private Plato crearPlatoValido() {
        Plato plato = new Plato();

        plato.setNombre("  Arroz con pollo  ");
        plato.setDescripcion(
                "  Arroz acompañado de pollo  "
        );
        plato.setPrecio(25000);
        plato.setUrlImagen(
                "  https://ejemplo.com/arroz-pollo.png  "
        );
        plato.setRestauranteId(RESTAURANTE_ID);
        plato.setCategoria(new Categoria(
                CATEGORIA_ID,
                null,
                null,
                null
        ));

        return plato;
    }

    private Restaurante crearRestauranteActivo(
            Long propietarioId) {

        Restaurante restaurante = new Restaurante();

        restaurante.setId(RESTAURANTE_ID);
        restaurante.setPropietarioId(propietarioId);
        restaurante.setActivo(Boolean.TRUE);

        return restaurante;
    }

    private Categoria crearCategoriaActiva() {
        return new Categoria(
                CATEGORIA_ID,
                "PLATO_FUERTE",
                "Platos principales del menú",
                Boolean.TRUE
        );
    }
}