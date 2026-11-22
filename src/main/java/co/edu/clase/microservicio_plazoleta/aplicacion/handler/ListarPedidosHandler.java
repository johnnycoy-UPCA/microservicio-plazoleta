package co.edu.clase.microservicio_plazoleta.aplicacion.handler;

import co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta.PaginaRespuestaDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta.PedidoRespuestaDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.mapeador.PedidoDtoMapeador;
import co.edu.clase.microservicio_plazoleta.dominio.api.ListarPedidosServicioPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pagina;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pedido;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoPedido;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ListarPedidosHandler {

    private final ListarPedidosServicioPuerto
            listarPedidosServicioPuerto;

    private final PedidoDtoMapeador
            pedidoDtoMapeador;

    public ListarPedidosHandler(
            ListarPedidosServicioPuerto
                    listarPedidosServicioPuerto,
            PedidoDtoMapeador pedidoDtoMapeador) {

        this.listarPedidosServicioPuerto =
                listarPedidosServicioPuerto;

        this.pedidoDtoMapeador =
                pedidoDtoMapeador;
    }

    public PaginaRespuestaDto<PedidoRespuestaDto>
            ejecutar(
                    Long empleadoId,
                    String token,
                    EstadoPedido estado,
                    int pagina,
                    int tamano) {

        Pagina<Pedido> resultado =
                listarPedidosServicioPuerto.listar(
                        empleadoId,
                        token,
                        estado,
                        pagina,
                        tamano
                );

        List<PedidoRespuestaDto> contenido =
                new ArrayList<>();

        for (Pedido pedido :
                resultado.getContenido()) {

            contenido.add(
                    pedidoDtoMapeador.aRespuesta(
                            pedido
                    )
            );
        }

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