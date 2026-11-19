package co.edu.clase.microservicio_plazoleta.aplicacion.mapeador;

import co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta.DetallePedidoRespuestaDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta.PedidoRespuestaDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.dto.solicitud.DetallePedidoSolicitudDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.dto.solicitud.RealizarPedidoSolicitudDto;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.DetallePedido;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pedido;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PedidoDtoMapeador {

    public Pedido aModelo(
            RealizarPedidoSolicitudDto solicitud,
            Long clienteId) {

        Pedido pedido = new Pedido();

        pedido.setClienteId(clienteId);
        pedido.setRestauranteId(
                solicitud.getRestauranteId()
        );
        pedido.setDetalles(
                detallesAModelo(solicitud.getPlatos())
        );

        return pedido;
    }

    public PedidoRespuestaDto aRespuesta(
            Pedido pedido) {

        PedidoRespuestaDto respuesta =
                new PedidoRespuestaDto();

        respuesta.setId(pedido.getId());
        respuesta.setClienteId(pedido.getClienteId());
        respuesta.setRestauranteId(
                pedido.getRestauranteId()
        );
        respuesta.setEstado(pedido.getEstado());
        respuesta.setTotal(pedido.getTotal());
        respuesta.setPlatos(
                detallesARespuesta(
                        pedido.getDetalles()
                )
        );
        respuesta.setFechaCreacion(
                pedido.getFechaCreacion()
        );

        return respuesta;
    }

    private List<DetallePedido> detallesAModelo(
            List<DetallePedidoSolicitudDto>
                    solicitudes) {

        List<DetallePedido> detalles =
                new ArrayList<>();

        for (DetallePedidoSolicitudDto solicitud :
                solicitudes) {

            detalles.add(new DetallePedido(
                    null,
                    solicitud.getPlatoId(),
                    null,
                    null,
                    solicitud.getCantidad()
            ));
        }

        return detalles;
    }

    private List<DetallePedidoRespuestaDto>
            detallesARespuesta(
                    List<DetallePedido> detalles) {

        List<DetallePedidoRespuestaDto> respuesta =
                new ArrayList<>();

        for (DetallePedido detalle : detalles) {
            long subtotal =
                    (long) detalle.getPrecioUnitario()
                            * detalle.getCantidad();

            respuesta.add(
                    new DetallePedidoRespuestaDto(
                            detalle.getPlatoId(),
                            detalle.getNombrePlato(),
                            detalle.getPrecioUnitario(),
                            detalle.getCantidad(),
                            subtotal
                    )
            );
        }

        return respuesta;
    }
}