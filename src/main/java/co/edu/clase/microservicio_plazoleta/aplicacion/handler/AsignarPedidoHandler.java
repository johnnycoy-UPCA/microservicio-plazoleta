package co.edu.clase.microservicio_plazoleta.aplicacion.handler;

import co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta.PedidoRespuestaDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.mapeador.PedidoDtoMapeador;
import co.edu.clase.microservicio_plazoleta.dominio.api.AsignarPedidoServicioPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pedido;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AsignarPedidoHandler {

    private final AsignarPedidoServicioPuerto
            asignarPedidoServicioPuerto;

    private final PedidoDtoMapeador
            pedidoDtoMapeador;

    public AsignarPedidoHandler(
            AsignarPedidoServicioPuerto
                    asignarPedidoServicioPuerto,
            PedidoDtoMapeador pedidoDtoMapeador) {

        this.asignarPedidoServicioPuerto =
                asignarPedidoServicioPuerto;

        this.pedidoDtoMapeador =
                pedidoDtoMapeador;
    }

    @Transactional
    public PedidoRespuestaDto ejecutar(
            Long pedidoId,
            Long empleadoId,
            String token) {

        Pedido pedido =
                asignarPedidoServicioPuerto.asignar(
                        pedidoId,
                        empleadoId,
                        token
                );

        return pedidoDtoMapeador.aRespuesta(pedido);
    }
}