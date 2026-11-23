package co.edu.clase.microservicio_plazoleta.aplicacion.handler;

import co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta.PedidoRespuestaDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.mapeador.PedidoDtoMapeador;
import co.edu.clase.microservicio_plazoleta.dominio.api.EntregarPedidoServicioPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.excepcion.ExcepcionPlazoleta;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pedido;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class EntregarPedidoHandler {

    private final EntregarPedidoServicioPuerto
            entregarPedidoServicioPuerto;

    private final PedidoDtoMapeador
            pedidoDtoMapeador;

    public EntregarPedidoHandler(
            EntregarPedidoServicioPuerto
                    entregarPedidoServicioPuerto,
            PedidoDtoMapeador pedidoDtoMapeador) {

        this.entregarPedidoServicioPuerto =
                entregarPedidoServicioPuerto;

        this.pedidoDtoMapeador =
                pedidoDtoMapeador;
    }

    @Transactional(
            noRollbackFor = ExcepcionPlazoleta.class
    )
    public PedidoRespuestaDto ejecutar(
            Long pedidoId,
            Long empleadoId,
            String token,
            String pin) {

        Pedido pedido =
                entregarPedidoServicioPuerto.entregar(
                        pedidoId,
                        empleadoId,
                        token,
                        pin
                );

        return pedidoDtoMapeador.aRespuesta(pedido);
    }
}