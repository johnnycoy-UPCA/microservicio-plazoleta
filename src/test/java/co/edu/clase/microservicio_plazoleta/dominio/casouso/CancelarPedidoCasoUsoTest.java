package co.edu.clase.microservicio_plazoleta.dominio.casouso;

import co.edu.clase.microservicio_plazoleta.dominio.excepcion.ExcepcionPlazoleta;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.EventoPendiente;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.EventoTrazabilidad;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pedido;
import co.edu.clase.microservicio_plazoleta.dominio.spi.EventoPendientePersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.EventoSerializadorPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.PedidoPersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.util.constantes.CodigosError;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.CategoriaError;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoEnvioEvento;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoPedido;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.TipoEvento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CancelarPedidoCasoUsoTest {

    private static final Long PEDIDO_ID = 100L;
    private static final Long CLIENTE_ID = 20L;
    private static final Long RESTAURANTE_ID = 1L;

    private static final Instant FECHA_FIJA =
            Instant.parse("2026-11-23T18:00:00Z");

    private static final String EVENTO_JSON =
            "{\"pedidoId\":100,"
                    + "\"clienteId\":20,"
                    + "\"restauranteId\":1,"
                    + "\"empleadoId\":null,"
                    + "\"estadoAnterior\":\"PENDIENTE\","
                    + "\"estadoNuevo\":\"CANCELADO\"}";

    @Mock
    private PedidoPersistenciaPuerto pedidoPersistenciaPuerto;

    @Mock
    private EventoPendientePersistenciaPuerto eventoPendientePersistenciaPuerto;

    @Mock
    private EventoSerializadorPuerto eventoSerializadorPuerto;

    private CancelarPedidoCasoUso casoUso;

    @BeforeEach
    void configurar() {
        Clock reloj = Clock.fixed(
                FECHA_FIJA,
                ZoneOffset.UTC
        );

        casoUso = new CancelarPedidoCasoUso(
                pedidoPersistenciaPuerto,
                eventoPendientePersistenciaPuerto,
                eventoSerializadorPuerto,
                reloj
        );
    }

    @Test
    void debeCancelarPedidoPendienteYGuardarEventoOutbox() {
        Pedido pedido = crearPedidoPendiente();

        when(pedidoPersistenciaPuerto.buscarPorId(PEDIDO_ID))
                .thenReturn(Optional.of(pedido));

        when(pedidoPersistenciaPuerto.guardar(any(Pedido.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        when(eventoSerializadorPuerto.serializar(any(EventoTrazabilidad.class)))
                .thenReturn(EVENTO_JSON);

        Pedido resultado = casoUso.cancelar(PEDIDO_ID, CLIENTE_ID);

        assertEquals(EstadoPedido.CANCELADO, resultado.getEstado());
        assertEquals(FECHA_FIJA, resultado.getFechaActualizacion());

        verify(pedidoPersistenciaPuerto).guardar(pedido);
        verificarEventoTrazabilidad();
        verificarEventoPendiente();
    }

    @Test
    void debeRechazarPedidoInexistente() {
        when(pedidoPersistenciaPuerto.buscarPorId(PEDIDO_ID))
                .thenReturn(Optional.empty());

        ExcepcionPlazoleta excepcion = assertThrows(
                ExcepcionPlazoleta.class,
                () -> casoUso.cancelar(PEDIDO_ID, CLIENTE_ID)
        );

        assertEquals(CodigosError.PEDIDO_NO_ENCONTRADO, excepcion.getCodigo());
        assertEquals(CategoriaError.NO_ENCONTRADO, excepcion.getCategoria());
        verificarQueNoSeGuardoNiCreoEvento();
    }

    @Test
    void debeRechazarPedidoDeOtroCliente() {
        Pedido pedido = crearPedidoPendiente();
        pedido.setClienteId(99L);

        when(pedidoPersistenciaPuerto.buscarPorId(PEDIDO_ID))
                .thenReturn(Optional.of(pedido));

        ExcepcionPlazoleta excepcion = assertThrows(
                ExcepcionPlazoleta.class,
                () -> casoUso.cancelar(PEDIDO_ID, CLIENTE_ID)
        );

        assertEquals(CodigosError.PEDIDO_NO_PERTENECE_CLIENTE, excepcion.getCodigo());
        assertEquals(CategoriaError.ACCESO_DENEGADO, excepcion.getCategoria());
        verificarQueNoSeGuardoNiCreoEvento();
    }

    @Test
    void debeRechazarPedidoEnPreparacionConEstadoDinamico() {
        Pedido pedido = crearPedidoPendiente();
        pedido.setEstado(EstadoPedido.EN_PREPARACION);

        when(pedidoPersistenciaPuerto.buscarPorId(PEDIDO_ID))
                .thenReturn(Optional.of(pedido));

        ExcepcionPlazoleta excepcion = assertThrows(
                ExcepcionPlazoleta.class,
                () -> casoUso.cancelar(PEDIDO_ID, CLIENTE_ID)
        );

        assertEquals(CodigosError.PEDIDO_NO_CANCELABLE, excepcion.getCodigo());
        assertEquals(CategoriaError.CONFLICTO, excepcion.getCategoria());
        assertEquals(
                "Lo sentimos, tu pedido se encuentra en estado EN_PREPARACION y no puede cancelarse",
                excepcion.getMessage()
        );
        verificarQueNoSeGuardoNiCreoEvento();
    }

    private void verificarEventoTrazabilidad() {
        ArgumentCaptor<EventoTrazabilidad> captor =
                ArgumentCaptor.forClass(EventoTrazabilidad.class);

        verify(eventoSerializadorPuerto).serializar(captor.capture());

        EventoTrazabilidad evento = captor.getValue();
        assertEquals(PEDIDO_ID, evento.getPedidoId());
        assertEquals(CLIENTE_ID, evento.getClienteId());
        assertEquals(RESTAURANTE_ID, evento.getRestauranteId());
        assertNull(evento.getEmpleadoId());
        assertEquals(EstadoPedido.PENDIENTE, evento.getEstadoAnterior());
        assertEquals(EstadoPedido.CANCELADO, evento.getEstadoNuevo());
    }

    private void verificarEventoPendiente() {
        ArgumentCaptor<EventoPendiente> captor =
                ArgumentCaptor.forClass(EventoPendiente.class);

        verify(eventoPendientePersistenciaPuerto).guardar(captor.capture());

        EventoPendiente pendiente = captor.getValue();
        assertEquals(TipoEvento.PEDIDO_CANCELADO, pendiente.getTipoEvento());
        assertEquals(PEDIDO_ID, pendiente.getAgregadoId());
        assertEquals(EVENTO_JSON, pendiente.getCargaJson());
        assertEquals(EstadoEnvioEvento.PENDIENTE, pendiente.getEstadoEnvio());
        assertEquals(0, pendiente.getIntentos());
        assertEquals(FECHA_FIJA, pendiente.getFechaCreacion());
    }

    private void verificarQueNoSeGuardoNiCreoEvento() {
        verify(pedidoPersistenciaPuerto, never()).guardar(any(Pedido.class));
        verify(eventoSerializadorPuerto, never()).serializar(any(EventoTrazabilidad.class));
        verify(eventoPendientePersistenciaPuerto, never()).guardar(any(EventoPendiente.class));
    }

    private Pedido crearPedidoPendiente() {
        Pedido pedido = new Pedido();
        pedido.setId(PEDIDO_ID);
        pedido.setClienteId(CLIENTE_ID);
        pedido.setRestauranteId(RESTAURANTE_ID);
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setTotal(48000L);
        pedido.setFechaCreacion(FECHA_FIJA.minusSeconds(600));
        pedido.setFechaActualizacion(FECHA_FIJA.minusSeconds(600));
        pedido.setVersion(0L);
        return pedido;
    }
}
