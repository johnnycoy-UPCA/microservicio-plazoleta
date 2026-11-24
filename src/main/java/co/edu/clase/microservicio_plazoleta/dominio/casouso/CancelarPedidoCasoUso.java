package co.edu.clase.microservicio_plazoleta.dominio.casouso;

import co.edu.clase.microservicio_plazoleta.dominio.api.CancelarPedidoServicioPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.excepcion.ExcepcionPlazoleta;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.EventoPendiente;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.EventoTrazabilidad;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pedido;
import co.edu.clase.microservicio_plazoleta.dominio.spi.EventoPendientePersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.EventoSerializadorPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.PedidoPersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.util.constantes.CodigosError;
import co.edu.clase.microservicio_plazoleta.util.constantes.MensajesError;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.CategoriaError;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoEnvioEvento;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoPedido;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.TipoEvento;

import java.time.Clock;
import java.time.Instant;

public class CancelarPedidoCasoUso
        implements CancelarPedidoServicioPuerto {

    private final PedidoPersistenciaPuerto pedidoPersistenciaPuerto;
    private final EventoPendientePersistenciaPuerto eventoPendientePersistenciaPuerto;
    private final EventoSerializadorPuerto eventoSerializadorPuerto;
    private final Clock reloj;

    public CancelarPedidoCasoUso(
            PedidoPersistenciaPuerto pedidoPersistenciaPuerto,
            EventoPendientePersistenciaPuerto eventoPendientePersistenciaPuerto,
            EventoSerializadorPuerto eventoSerializadorPuerto,
            Clock reloj) {

        this.pedidoPersistenciaPuerto = pedidoPersistenciaPuerto;
        this.eventoPendientePersistenciaPuerto = eventoPendientePersistenciaPuerto;
        this.eventoSerializadorPuerto = eventoSerializadorPuerto;
        this.reloj = reloj;
    }

    @Override
    public Pedido cancelar(
            Long pedidoId,
            Long clienteId) {

        Pedido pedido = pedidoPersistenciaPuerto
                .buscarPorId(pedidoId)
                .orElseThrow(() ->
                        new ExcepcionPlazoleta(
                                CodigosError.PEDIDO_NO_ENCONTRADO,
                                MensajesError.PEDIDO_NO_ENCONTRADO,
                                CategoriaError.NO_ENCONTRADO
                        )
                );

        validarPropietarioPedido(pedido, clienteId);
        validarEstadoCancelable(pedido);

        Instant fechaActual = reloj.instant();

        pedido.setEstado(EstadoPedido.CANCELADO);
        pedido.setFechaActualizacion(fechaActual);

        Pedido pedidoGuardado =
                pedidoPersistenciaPuerto.guardar(pedido);

        guardarEventoPendiente(
                pedidoGuardado,
                fechaActual
        );

        return pedidoGuardado;
    }

    private void validarPropietarioPedido(
            Pedido pedido,
            Long clienteId) {

        if (!clienteId.equals(pedido.getClienteId())) {
            throw new ExcepcionPlazoleta(
                    CodigosError.PEDIDO_NO_PERTENECE_CLIENTE,
                    MensajesError.PEDIDO_NO_PERTENECE_CLIENTE,
                    CategoriaError.ACCESO_DENEGADO
            );
        }
    }

    private void validarEstadoCancelable(Pedido pedido) {
        if (!EstadoPedido.PENDIENTE.equals(pedido.getEstado())) {
            throw new ExcepcionPlazoleta(
                    CodigosError.PEDIDO_NO_CANCELABLE,
                    String.format(
                            MensajesError.PEDIDO_NO_CANCELABLE,
                            pedido.getEstado().name()
                    ),
                    CategoriaError.CONFLICTO
            );
        }
    }

    private void guardarEventoPendiente(
            Pedido pedido,
            Instant fechaActual) {

        EventoTrazabilidad evento =
                new EventoTrazabilidad(
                        pedido.getId(),
                        pedido.getClienteId(),
                        pedido.getRestauranteId(),
                        null,
                        EstadoPedido.PENDIENTE,
                        EstadoPedido.CANCELADO
                );

        EventoPendiente pendiente = new EventoPendiente();

        pendiente.setTipoEvento(TipoEvento.PEDIDO_CANCELADO);
        pendiente.setAgregadoId(pedido.getId());
        pendiente.setCargaJson(
                eventoSerializadorPuerto.serializar(evento)
        );
        pendiente.setEstadoEnvio(EstadoEnvioEvento.PENDIENTE);
        pendiente.setIntentos(0);
        pendiente.setProximoIntento(fechaActual);
        pendiente.setFechaCreacion(fechaActual);
        pendiente.setFechaEnvio(null);
        pendiente.setUltimoError(null);
        pendiente.setVersion(0L);

        eventoPendientePersistenciaPuerto.guardar(pendiente);
    }
}
