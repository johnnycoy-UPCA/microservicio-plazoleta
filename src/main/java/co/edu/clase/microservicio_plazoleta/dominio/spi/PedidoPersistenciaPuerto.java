package co.edu.clase.microservicio_plazoleta.dominio.spi;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pedido;

public interface PedidoPersistenciaPuerto {
    
    boolean existePedidoActivoPorCliente(
        Long clienteId
    );

    Pedido guardar(Pedido pedido);
}
