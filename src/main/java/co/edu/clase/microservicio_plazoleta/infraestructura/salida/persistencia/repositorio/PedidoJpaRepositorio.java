package co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.repositorio;

import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.entidad.PedidoEntidad;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface PedidoJpaRepositorio
        extends JpaRepository<PedidoEntidad, Long> {

    boolean existsByClienteIdAndEstadoIn(
            Long clienteId,
            Collection<EstadoPedido> estados
    );
}