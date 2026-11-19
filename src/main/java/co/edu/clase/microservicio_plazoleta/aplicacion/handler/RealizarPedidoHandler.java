package co.edu.clase.microservicio_plazoleta.aplicacion.handler;

import co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta.PedidoRespuestaDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.dto.solicitud.RealizarPedidoSolicitudDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.mapeador.PedidoDtoMapeador;
import co.edu.clase.microservicio_plazoleta.dominio.api.RealizarPedidoServicioPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pedido;
import org.springframework.stereotype.Component;

@Component
public class RealizarPedidoHandler {

    private final RealizarPedidoServicioPuerto
            realizarPedidoServicioPuerto;

    private final PedidoDtoMapeador
            pedidoDtoMapeador;

    public RealizarPedidoHandler(
            RealizarPedidoServicioPuerto
                    realizarPedidoServicioPuerto,
            PedidoDtoMapeador pedidoDtoMapeador) {

        this.realizarPedidoServicioPuerto =
                realizarPedidoServicioPuerto;
        this.pedidoDtoMapeador = pedidoDtoMapeador;
    }

    public PedidoRespuestaDto ejecutar(
            RealizarPedidoSolicitudDto solicitud,
            Long clienteId) {

        Pedido pedido = pedidoDtoMapeador.aModelo(
                solicitud,
                clienteId
        );

        Pedido creado =
                realizarPedidoServicioPuerto.realizar(
                        pedido
                );

        return pedidoDtoMapeador.aRespuesta(creado);
    }
}