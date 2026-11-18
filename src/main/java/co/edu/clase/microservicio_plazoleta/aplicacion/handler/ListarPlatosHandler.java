package co.edu.clase.microservicio_plazoleta.aplicacion.handler;

import co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta.PaginaRespuestaDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta.PlatoResumenDto;
import co.edu.clase.microservicio_plazoleta.dominio.api.ListarPlatosServicioPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pagina;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Plato;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListarPlatosHandler {

    private final ListarPlatosServicioPuerto
            listarPlatosServicioPuerto;

    public ListarPlatosHandler(
            ListarPlatosServicioPuerto
                    listarPlatosServicioPuerto) {

        this.listarPlatosServicioPuerto =
                listarPlatosServicioPuerto;
    }

    public PaginaRespuestaDto<PlatoResumenDto> ejecutar(
            Long restauranteId,
            Long categoriaId,
            int pagina,
            int tamano) {

        Pagina<Plato> resultado =
                listarPlatosServicioPuerto.listar(
                        restauranteId,
                        categoriaId,
                        pagina,
                        tamano
                );

        List<PlatoResumenDto> contenido =
                resultado.getContenido()
                        .stream()
                        .map(plato -> new PlatoResumenDto(
                                plato.getId(),
                                plato.getNombre(),
                                plato.getDescripcion(),
                                plato.getPrecio(),
                                plato.getUrlImagen(),
                                plato.getCategoria().getId(),
                                plato.getCategoria().getNombre()
                        ))
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