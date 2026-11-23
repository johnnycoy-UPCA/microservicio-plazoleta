package co.edu.clase.microservicio_plazoleta.aplicacion.handler;

import co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta.PedidoRespuestaDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.mapeador.PedidoDtoMapeador;
import co.edu.clase.microservicio_plazoleta.dominio.api.MarcarPedidoListoServicioPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pedido;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MarcarPedidoListoHandler {

    private final MarcarPedidoListoServicioPuerto
            marcarPedidoListoServicioPuerto;

    private final PedidoDtoMapeador
            pedidoDtoMapeador;

    public MarcarPedidoListoHandler(
            MarcarPedidoListoServicioPuerto
                    marcarPedidoListoServicioPuerto,
            PedidoDtoMapeador pedidoDtoMapeador) {

        this.marcarPedidoListoServicioPuerto =
                marcarPedidoListoServicioPuerto;
        this.pedidoDtoMapeador = pedidoDtoMapeador;
    }

    @Transactional
    public PedidoRespuestaDto ejecutar(
            Long pedidoId,
            Long empleadoId,
            String token) {

        Pedido pedido =
                marcarPedidoListoServicioPuerto
                        .marcarListo(
                                pedidoId,
                                empleadoId,
                                token
                        );

        return pedidoDtoMapeador.aRespuesta(
                pedido
        );
    }
}