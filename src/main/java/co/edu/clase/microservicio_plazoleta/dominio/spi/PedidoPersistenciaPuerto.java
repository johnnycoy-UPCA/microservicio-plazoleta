package co.edu.clase.microservicio_plazoleta.dominio.spi;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pagina;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pedido;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoPedido;

public interface PedidoPersistenciaPuerto {
    
    boolean existePedidoActivoPorCliente(
        Long clienteId
    );

    Pagina<Pedido> listarPorRestauranteYEstado(
            Long restauranteId,
            EstadoPedido estado,
            int pagina,
            int tamano
    );

    Pedido guardar(Pedido pedido);
}
