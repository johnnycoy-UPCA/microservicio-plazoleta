package co.edu.clase.microservicio_plazoleta.dominio.casouso;

import co.edu.clase.microservicio_plazoleta.dominio.excepcion.ExcepcionPlazoleta;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Propietario;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Restaurante;
import co.edu.clase.microservicio_plazoleta.dominio.spi.PropietarioConsultaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.RestaurantePersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.util.constantes.CodigosError;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.PerfilUsuario;
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
class CrearRestauranteCasoUsoTest {

    private static final Instant FECHA_PRUEBA =
            Instant.parse("2026-11-10T18:00:00Z");

    @Mock
    private RestaurantePersistenciaPuerto
            restaurantePersistenciaPuerto;

    @Mock
    private PropietarioConsultaPuerto
            propietarioConsultaPuerto;

    private CrearRestauranteCasoUso crearRestauranteCasoUso;

    @BeforeEach
    void configurar() {
        Clock reloj = Clock.fixed(
                FECHA_PRUEBA,
                ZoneId.of("America/Bogota")
        );

        crearRestauranteCasoUso =
                new CrearRestauranteCasoUso(
                        restaurantePersistenciaPuerto,
                        propietarioConsultaPuerto,
                        reloj
                );
    }

    @Test
    void debeCrearRestauranteCorrectamente() {
        Restaurante restaurante = crearRestauranteValido();

        when(restaurantePersistenciaPuerto.existePorNit(
                "900123456"
        )).thenReturn(false);

        when(propietarioConsultaPuerto.buscarPorId(10L))
                .thenReturn(Optional.of(
                        crearPropietarioValido()
                ));

        when(restaurantePersistenciaPuerto.guardar(
                any(Restaurante.class)
        )).thenAnswer(invocacion -> {
            Restaurante guardado =
                    invocacion.getArgument(0);

            guardado.setId(1L);
            return guardado;
        });

        Restaurante resultado =
                crearRestauranteCasoUso.crear(restaurante);

        assertEquals(1L, resultado.getId());
        assertEquals(
                "Restaurante Caribe",
                resultado.getNombre()
        );
        assertTrue(resultado.getActivo());
        assertNotNull(resultado.getFechaCreacion());
        assertNotNull(resultado.getFechaActualizacion());

        verify(restaurantePersistenciaPuerto)
                .guardar(restaurante);
    }

    @Test
    void debeRechazarNitDuplicado() {
        Restaurante restaurante = crearRestauranteValido();

        when(restaurantePersistenciaPuerto.existePorNit(
                "900123456"
        )).thenReturn(true);

        ExcepcionPlazoleta excepcion = assertThrows(
                ExcepcionPlazoleta.class,
                () -> crearRestauranteCasoUso.crear(
                        restaurante
                )
        );

        assertEquals(
                CodigosError.NIT_YA_REGISTRADO,
                excepcion.getCodigo()
        );

        verify(propietarioConsultaPuerto, never())
                .buscarPorId(any());

        verify(restaurantePersistenciaPuerto, never())
                .guardar(any());
    }

    @Test
    void debeRechazarNombreSoloNumerico() {
        Restaurante restaurante = crearRestauranteValido();
        restaurante.setNombre("123456");

        ExcepcionPlazoleta excepcion = assertThrows(
                ExcepcionPlazoleta.class,
                () -> crearRestauranteCasoUso.crear(
                        restaurante
                )
        );

        assertEquals(
                CodigosError.NOMBRE_SOLO_NUMEROS,
                excepcion.getCodigo()
        );

        verify(restaurantePersistenciaPuerto, never())
                .guardar(any());
    }

    @Test
    void debeRechazarPropietarioInexistente() {
        Restaurante restaurante = crearRestauranteValido();

        when(restaurantePersistenciaPuerto.existePorNit(
                "900123456"
        )).thenReturn(false);

        when(propietarioConsultaPuerto.buscarPorId(10L))
                .thenReturn(Optional.empty());

        ExcepcionPlazoleta excepcion = assertThrows(
                ExcepcionPlazoleta.class,
                () -> crearRestauranteCasoUso.crear(
                        restaurante
                )
        );

        assertEquals(
                CodigosError.PROPIETARIO_NO_ENCONTRADO,
                excepcion.getCodigo()
        );

        verify(restaurantePersistenciaPuerto, never())
                .guardar(any());
    }

    @Test
    void debeRechazarUsuarioSinPerfilPropietario() {
        Restaurante restaurante = crearRestauranteValido();

        Propietario cliente = new Propietario(
                10L,
                PerfilUsuario.CLIENTE,
                Boolean.TRUE
        );

        when(restaurantePersistenciaPuerto.existePorNit(
                "900123456"
        )).thenReturn(false);

        when(propietarioConsultaPuerto.buscarPorId(10L))
                .thenReturn(Optional.of(cliente));

        ExcepcionPlazoleta excepcion = assertThrows(
                ExcepcionPlazoleta.class,
                () -> crearRestauranteCasoUso.crear(
                        restaurante
                )
        );

        assertEquals(
                CodigosError.USUARIO_NO_ES_PROPIETARIO,
                excepcion.getCodigo()
        );

        verify(restaurantePersistenciaPuerto, never())
                .guardar(any());
    }

    private Restaurante crearRestauranteValido() {
        Restaurante restaurante = new Restaurante();

        restaurante.setNombre("  Restaurante Caribe  ");
        restaurante.setNit(" 900123456 ");
        restaurante.setDireccion(" Calle 72 # 45-10 ");
        restaurante.setCelular(" +573001234567 ");
        restaurante.setUrlLogo(
                " https://ejemplo.com/logo.png "
        );
        restaurante.setPropietarioId(10L);

        return restaurante;
    }

    private Propietario crearPropietarioValido() {
        return new Propietario(
                10L,
                PerfilUsuario.PROPIETARIO,
                Boolean.TRUE
        );
    }
}