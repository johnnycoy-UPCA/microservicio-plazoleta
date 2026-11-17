package co.edu.clase.microservicio_plazoleta.dominio.casouso;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pagina;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Plato;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Restaurante;
import co.edu.clase.microservicio_plazoleta.dominio.spi.PlatoPersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.RestaurantePersistenciaPuerto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarPlatosCasoUsoTest {

    @Mock
    private PlatoPersistenciaPuerto
            platoPersistenciaPuerto;

    @Mock
    private RestaurantePersistenciaPuerto
            restaurantePersistenciaPuerto;

    @Test
    void debeListarPlatosActivosPorRestaurante() {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setActivo(Boolean.TRUE);

        Pagina<Plato> paginaEsperada =
                new Pagina<>(
                        List.of(new Plato()),
                        0,
                        10,
                        1L,
                        1,
                        true,
                        true
                );

        when(restaurantePersistenciaPuerto
                .buscarPorId(1L))
                .thenReturn(Optional.of(restaurante));

        when(platoPersistenciaPuerto
                .listarActivosPorRestaurante(
                        1L,
                        2L,
                        0,
                        10
                ))
                .thenReturn(paginaEsperada);

        ListarPlatosCasoUso casoUso =
                new ListarPlatosCasoUso(
                        platoPersistenciaPuerto,
                        restaurantePersistenciaPuerto
                );

        Pagina<Plato> resultado =
                casoUso.listar(1L, 2L, 0, 10);

        assertEquals(1, resultado.getContenido().size());

        verify(platoPersistenciaPuerto)
                .listarActivosPorRestaurante(
                        1L,
                        2L,
                        0,
                        10
                );
    }
}