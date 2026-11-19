package co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.adaptador;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pedido;
import co.edu.clase.microservicio_plazoleta.dominio.spi.PedidoPersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.entidad.PedidoEntidad;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.mapeador.PedidoPersistenciaMapeador;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.repositorio.PedidoJpaRepositorio;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoPedido;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;

@Component
public class PedidoPersistenciaAdaptador
        implements PedidoPersistenciaPuerto {

    private static final EnumSet<EstadoPedido>
            ESTADOS_ACTIVOS = EnumSet.of(
                    EstadoPedido.PENDIENTE,
                    EstadoPedido.EN_PREPARACION,
                    EstadoPedido.LISTO
            );

    private final PedidoJpaRepositorio
            pedidoJpaRepositorio;

    private final PedidoPersistenciaMapeador
            pedidoPersistenciaMapeador;

    public PedidoPersistenciaAdaptador(
            PedidoJpaRepositorio pedidoJpaRepositorio,
            PedidoPersistenciaMapeador
                    pedidoPersistenciaMapeador) {

        this.pedidoJpaRepositorio =
                pedidoJpaRepositorio;
        this.pedidoPersistenciaMapeador =
                pedidoPersistenciaMapeador;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePedidoActivoPorCliente(
            Long clienteId) {

        return pedidoJpaRepositorio
                .existsByClienteIdAndEstadoIn(
                        clienteId,
                        ESTADOS_ACTIVOS
                );
    }

    @Override
    @Transactional
    public Pedido guardar(Pedido pedido) {
        PedidoEntidad entidad =
                pedidoPersistenciaMapeador.aEntidad(
                        pedido
                );

        PedidoEntidad guardada =
                pedidoJpaRepositorio.save(entidad);

        return pedidoPersistenciaMapeador.aModelo(
                guardada
        );
    }
}