package co.edu.clase.microservicio_plazoleta.aplicacion.handler;

import co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta.PaginaRespuestaDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta.RestauranteResumenDto;
import co.edu.clase.microservicio_plazoleta.dominio.api.ListarRestaurantesServicioPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pagina;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Restaurante;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListarRestaurantesHandler {

    private final ListarRestaurantesServicioPuerto
            listarRestaurantesServicioPuerto;

    public ListarRestaurantesHandler(
            ListarRestaurantesServicioPuerto
                    listarRestaurantesServicioPuerto) {

        this.listarRestaurantesServicioPuerto =
                listarRestaurantesServicioPuerto;
    }

    public PaginaRespuestaDto<RestauranteResumenDto>
            ejecutar(int pagina, int tamano) {

        Pagina<Restaurante> resultado =
                listarRestaurantesServicioPuerto
                        .listar(pagina, tamano);

        List<RestauranteResumenDto> contenido =
                resultado.getContenido()
                        .stream()
                        .map(restaurante ->
                                new RestauranteResumenDto(
                                        restaurante.getId(),
                                        restaurante.getNombre(),
                                        restaurante.getUrlLogo()
                                )
                        )
                        .toList();

        return new PaginaRespuestaDto<>(
                contenido,
                resultado.getPaginaActual(),
                resultado.getTamanoPagina(),
                resultado.getTotalElementos(),
                resultado.getTotalPaginas(),
                resultado.isPrimera(),
                resultado.isUltima()
        );
    }
}