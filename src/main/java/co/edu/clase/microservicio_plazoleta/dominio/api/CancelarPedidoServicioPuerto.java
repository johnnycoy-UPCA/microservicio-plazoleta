package co.edu.clase.microservicio_plazoleta.dominio.api;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pedido;

public interface CancelarPedidoServicioPuerto {

    Pedido cancelar(
            Long pedidoId,
            Long clienteId
    );
}
