package co.edu.clase.microservicio_plazoleta.dominio.api;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pedido;

public interface EntregarPedidoServicioPuerto {

    Pedido entregar(
            Long pedidoId,
            Long empleadoId,
            String token,
            String pin
    );
}
