package co.edu.clase.microservicio_plazoleta.dominio.api;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pedido;

public interface MarcarPedidoListoServicioPuerto {

    Pedido marcarListo(
            Long pedidoId,
            Long empleadoId,
            String token
    );
}