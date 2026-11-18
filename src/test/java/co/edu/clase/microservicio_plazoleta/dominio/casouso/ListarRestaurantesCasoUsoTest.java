package co.edu.clase.microservicio_plazoleta.dominio.casouso;

import co.edu.clase.microservicio_plazoleta.dominio.excepcion.ExcepcionPlazoleta;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pagina;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Restaurante;
import co.edu.clase.microservicio_plazoleta.dominio.spi.RestaurantePersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.util.constantes.CodigosError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarRestaurantesCasoUsoTest {

    @Mock
    private RestaurantePersistenciaPuerto
            restaurantePersistenciaPuerto;

    @Test
    void debeListarRestaurantesPaginados() {
        Pagina<Restaurante> paginaEsperada =
                new Pagina<>(
                        List.of(
                                crearRestaurante(
                                        1L,
                                        "Aromas"
                                ),
                                crearRestaurante(
                                        2L,
                                        "Sabores"
                                )
                        ),
                        0,
                        10,
                        2L,
                        1,
                        true,
                        true
                );

        when(restaurantePersistenciaPuerto.listarActivos(
                0,
                10
        )).thenReturn(paginaEsperada);

        ListarRestaurantesCasoUso casoUso =
                new ListarRestaurantesCasoUso(
                        restaurantePersistenciaPuerto
                );

        Pagina<Restaurante> resultado =
                casoUso.listar(0, 10);

        assertEquals(2, resultado.getContenido().size());
        assertEquals(
                "Aromas",
                resultado.getContenido()
                        .get(0)
                        .getNombre()
        );

        verify(restaurantePersistenciaPuerto)
                .listarActivos(0, 10);
    }

    @Test
    void debeRechazarPaginaNegativa() {
        ListarRestaurantesCasoUso casoUso =
                new ListarRestaurantesCasoUso(
                        restaurantePersistenciaPuerto
                );

        ExcepcionPlazoleta excepcion = assertThrows(
                ExcepcionPlazoleta.class,
                () -> casoUso.listar(-1, 10)
        );

        assertEquals(
                CodigosError.PAGINA_INVALIDA,
                excepcion.getCodigo()
        );
    }

    @Test
    void debeRechazarTamanoMayorAlPermitido() {
        ListarRestaurantesCasoUso casoUso =
                new ListarRestaurantesCasoUso(
                        restaurantePersistenciaPuerto
                );

        ExcepcionPlazoleta excepcion = assertThrows(
                ExcepcionPlazoleta.class,
                () -> casoUso.listar(0, 101)
        );

        assertEquals(
                CodigosError.TAMANO_PAGINA_INVALIDO,
                excepcion.getCodigo()
        );
    }

    private Restaurante crearRestaurante(
            Long id,
            String nombre) {

        Restaurante restaurante = new Restaurante();

        restaurante.setId(id);
        restaurante.setNombre(nombre);
        restaurante.setUrlLogo(
                "https://ejemplo.com/" + id + ".png"
        );
        restaurante.setActivo(Boolean.TRUE);

        return restaurante;
    }
}