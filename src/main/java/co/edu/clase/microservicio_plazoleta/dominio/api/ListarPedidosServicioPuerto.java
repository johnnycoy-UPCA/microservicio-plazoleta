package co.edu.clase.microservicio_plazoleta.dominio.api;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pagina;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pedido;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoPedido;

public interface ListarPedidosServicioPuerto {

    Pagina<Pedido> listar(
            Long empleadoId,
            String token,
            EstadoPedido estado,
            int pagina,
            int tamano
    );
}