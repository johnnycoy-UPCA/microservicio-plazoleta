package co.edu.clase.microservicio_plazoleta.dominio.casouso;

import co.edu.clase.microservicio_plazoleta.dominio.excepcion.ExcepcionPlazoleta;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Restaurante;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.RestauranteValidacion;
import co.edu.clase.microservicio_plazoleta.dominio.spi.RestaurantePersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.util.constantes.CodigosError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidarRestaurantePropietarioCasoUsoTest {

    @Mock
    private RestaurantePersistenciaPuerto
            restaurantePersistenciaPuerto;

    @Test
    void debeValidarRestauranteDelPropietario() {
        Restaurante restaurante =
                crearRestaurante(10L, true);

        when(restaurantePersistenciaPuerto
                .buscarPorId(1L))
                .thenReturn(Optional.of(restaurante));

        ValidarRestaurantePropietarioCasoUso casoUso =
                new ValidarRestaurantePropietarioCasoUso(
                        restaurantePersistenciaPuerto
                );

        RestauranteValidacion resultado =
                casoUso.validar(1L, 10L);

        assertEquals(1L, resultado.getId());
        assertEquals(10L, resultado.getPropietarioId());
        assertEquals(Boolean.TRUE, resultado.getActivo());
    }

    @Test
    void debeRechazarRestauranteInexistente() {
        when(restaurantePersistenciaPuerto
                .buscarPorId(99L))
                .thenReturn(Optional.empty());

        ValidarRestaurantePropietarioCasoUso casoUso =
                new ValidarRestaurantePropietarioCasoUso(
                        restaurantePersistenciaPuerto
                );

        ExcepcionPlazoleta excepcion = assertThrows(
                ExcepcionPlazoleta.class,
                () -> casoUso.validar(99L, 10L)
        );

        assertEquals(
                CodigosError.RESTAURANTE_NO_ENCONTRADO,
                excepcion.getCodigo()
        );
    }

    @Test
    void debeRechazarRestauranteAjeno() {
        when(restaurantePersistenciaPuerto
                .buscarPorId(1L))
                .thenReturn(Optional.of(
                        crearRestaurante(20L, true)
                ));

        ValidarRestaurantePropietarioCasoUso casoUso =
                new ValidarRestaurantePropietarioCasoUso(
                        restaurantePersistenciaPuerto
                );

        ExcepcionPlazoleta excepcion = assertThrows(
                ExcepcionPlazoleta.class,
                () -> casoUso.validar(1L, 10L)
        );

        assertEquals(
                CodigosError
                        .RESTAURANTE_NO_PERTENECE_PROPIETARIO,
                excepcion.getCodigo()
        );
    }

    private Restaurante crearRestaurante(
            Long propietarioId,
            boolean activo) {

        Restaurante restaurante = new Restaurante();

        restaurante.setId(1L);
        restaurante.setPropietarioId(propietarioId);
        restaurante.setActivo(activo);

        return restaurante;
    }
}