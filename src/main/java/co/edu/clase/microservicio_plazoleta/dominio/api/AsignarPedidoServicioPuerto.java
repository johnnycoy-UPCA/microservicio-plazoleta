package co.edu.clase.microservicio_plazoleta.dominio.api;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pedido;

public interface AsignarPedidoServicioPuerto {

    Pedido asignar(
            Long pedidoId,
            Long empleadoId,
            String token
    );
}