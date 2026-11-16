package co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.adaptador;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.Restaurante;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.entidad.RestauranteEntidad;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.mapeador.RestaurantePersistenciaMapeador;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.repositorio.RestauranteJpaRepositorio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantePersistenciaAdaptadorTest {

    @Mock
    private RestauranteJpaRepositorio
		restauranteJpaRepositorio;

    @Spy
    private RestaurantePersistenciaMapeador
		restaurantePersistenciaMapeador;

    @InjectMocks
    private RestaurantePersistenciaAdaptador
		restaurantePersistenciaAdaptador;

    @Test
    void debeDetectarNitRegistrado() {
        when(restauranteJpaRepositorio.existsByNit(
			"900123456"
        )).thenReturn(true);

        boolean resultado =
			restaurantePersistenciaAdaptador
				.existePorNit("900123456");

        assertTrue(resultado);
    }

    @Test
    void debeDetectarNitNoRegistrado() {
        when(restauranteJpaRepositorio.existsByNit(
			"900999999"
        )).thenReturn(false);

        boolean resultado =
			restaurantePersistenciaAdaptador
				.existePorNit("900999999");

        assertFalse(resultado);
    }

    @Test
    void debeGuardarRestaurante() {
        Restaurante restaurante = crearRestaurante();

        RestauranteEntidad entidadGuardada =
			crearEntidadGuardada();

        when(restauranteJpaRepositorio.save(
			any(RestauranteEntidad.class)
        )).thenReturn(entidadGuardada);

        Restaurante resultado =
			restaurantePersistenciaAdaptador.guardar(
				restaurante
			);

        assertEquals(1L, resultado.getId());
        assertEquals(
			"Restaurante Caribe",
			resultado.getNombre()
        );
        assertEquals(
			"900123456",
			resultado.getNit()
        );
        assertTrue(resultado.getActivo());
    }

    private Restaurante crearRestaurante() {
        Restaurante restaurante = new Restaurante();

        restaurante.setNombre("Restaurante Caribe");
        restaurante.setNit("900123456");
        restaurante.setDireccion("Calle 72 # 45-10");
        restaurante.setCelular("+573001234567");
        restaurante.setUrlLogo(
			"https://ejemplo.com/logo.png"
        );
        restaurante.setPropietarioId(10L);
        restaurante.setActivo(Boolean.TRUE);
        restaurante.setFechaCreacion(
			LocalDateTime.of(
				2026, 11, 10, 13, 0
			)
        );
        restaurante.setFechaActualizacion(
			LocalDateTime.of(
				2026, 11, 10, 13, 0
			)
        );

        return restaurante;
    }

    private RestauranteEntidad crearEntidadGuardada() {
        RestauranteEntidad entidad =
			new RestauranteEntidad();

        entidad.setId(1L);
        entidad.setNombre("Restaurante Caribe");
        entidad.setNit("900123456");
        entidad.setDireccion("Calle 72 # 45-10");
        entidad.setCelular("+573001234567");
        entidad.setUrlLogo(
			"https://ejemplo.com/logo.png"
        );
        entidad.setPropietarioId(10L);
        entidad.setActivo(Boolean.TRUE);
        entidad.setFechaCreacion(
			LocalDateTime.of(
				2026, 11, 10, 13, 0
			)
        );
        entidad.setFechaActualizacion(
			LocalDateTime.of(
				2026, 11, 10, 13, 0
			)
        );

        return entidad;
    }
}