package co.edu.clase.microservicio_plazoleta.aplicacion.handler;

import co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta.PedidoRespuestaDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.mapeador.PedidoDtoMapeador;
import co.edu.clase.microservicio_plazoleta.dominio.api.CancelarPedidoServicioPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pedido;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CancelarPedidoHandler {

    private final CancelarPedidoServicioPuerto cancelarPedidoServicioPuerto;
    private final PedidoDtoMapeador pedidoDtoMapeador;

    public CancelarPedidoHandler(
            CancelarPedidoServicioPuerto cancelarPedidoServicioPuerto,
            PedidoDtoMapeador pedidoDtoMapeador) {

        this.cancelarPedidoServicioPuerto = cancelarPedidoServicioPuerto;
        this.pedidoDtoMapeador = pedidoDtoMapeador;
    }

    @Transactional
    public PedidoRespuestaDto ejecutar(
            Long pedidoId,
            Long clienteId) {

        Pedido pedido = cancelarPedidoServicioPuerto.cancelar(
                pedidoId,
                clienteId
        );

        return pedidoDtoMapeador.aRespuesta(pedido);
    }
}
