package co.edu.clase.microservicio_plazoleta.dominio.casouso;

import co.edu.clase.microservicio_plazoleta.dominio.excepcion.ExcepcionPlazoleta;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.DetallePedido;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.EventoPendiente;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.EventoTrazabilidad;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pedido;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Plato;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Restaurante;
import co.edu.clase.microservicio_plazoleta.dominio.spi.EventoPendientePersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.EventoSerializadorPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.PedidoPersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.PlatoPersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.RestaurantePersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.util.constantes.CodigosError;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RealizarPedidoCasoUsoTest {

    private static final Long CLIENTE_ID = 20L;
    private static final Long RESTAURANTE_ID = 1L;
    private static final Long PEDIDO_ID = 100L;

    private static final Instant FECHA_FIJA =
            Instant.parse("2026-11-19T18:00:00Z");

    private static final String EVENTO_JSON =
            "{\"pedidoId\":100,"
                    + "\"clienteId\":20,"
                    + "\"restauranteId\":1,"
                    + "\"empleadoId\":null,"
                    + "\"estadoAnterior\":null,"
                    + "\"estadoNuevo\":\"PENDIENTE\"}";

    @Mock
    private PedidoPersistenciaPuerto
            pedidoPersistenciaPuerto;

    @Mock
    private PlatoPersistenciaPuerto
            platoPersistenciaPuerto;

    @Mock
    private RestaurantePersistenciaPuerto
            restaurantePersistenciaPuerto;

    @Mock
    private EventoPendientePersistenciaPuerto
            eventoPendientePersistenciaPuerto;

    @Mock
    private EventoSerializadorPuerto
            eventoSerializadorPuerto;

    private RealizarPedidoCasoUso casoUso;

    @BeforeEach
    void configurar() {
        Clock reloj = Clock.fixed(
                FECHA_FIJA,
                ZoneOffset.UTC
        );

        casoUso = new RealizarPedidoCasoUso(
                pedidoPersistenciaPuerto,
                platoPersistenciaPuerto,
                restaurantePersistenciaPuerto,
                eventoPendientePersistenciaPuerto,
                eventoSerializadorPuerto,
                reloj
        );
    }

    @Test
    void debeRealizarPedidoYGuardarEventoPendiente() {
        Pedido pedido = crearPedido();

        when(pedidoPersistenciaPuerto
                .existePedidoActivoPorCliente(
                        CLIENTE_ID
                ))
                .thenReturn(false);

        when(restaurantePersistenciaPuerto
                .buscarPorId(RESTAURANTE_ID))
                .thenReturn(
                        Optional.of(
                                crearRestauranteActivo()
                        )
                );

        when(platoPersistenciaPuerto
                .buscarActivosPorIds(
                        List.of(1L, 2L)
                ))
                .thenReturn(
                        List.of(
                                crearPlato(
                                        1L,
                                        "Arroz",
                                        20000
                                ),
                                crearPlato(
                                        2L,
                                        "Limonada",
                                        8000
                                )
                        )
                );

        when(pedidoPersistenciaPuerto.guardar(
                any(Pedido.class)
        )).thenAnswer(invocacion -> {
            Pedido pedidoGuardado =
                    invocacion.getArgument(0);

            pedidoGuardado.setId(PEDIDO_ID);

            return pedidoGuardado;
        });

        when(eventoSerializadorPuerto.serializar(
                any(EventoTrazabilidad.class)
        )).thenReturn(EVENTO_JSON);

        Pedido resultado = casoUso.realizar(pedido);

        assertEquals(
                PEDIDO_ID,
                resultado.getId()
        );

        assertEquals(
                EstadoPedido.PENDIENTE,
                resultado.getEstado()
        );

        assertEquals(
                48000L,
                resultado.getTotal()
        );

        assertEquals(
                "Arroz",
                resultado.getDetalles()
                        .get(0)
                        .getNombrePlato()
        );

        assertEquals(
                20000,
                resultado.getDetalles()
                        .get(0)
                        .getPrecioUnitario()
        );

        assertEquals(
                "Limonada",
                resultado.getDetalles()
                        .get(1)
                        .getNombrePlato()
        );

        assertEquals(
                8000,
                resultado.getDetalles()
                        .get(1)
                        .getPrecioUnitario()
        );

        assertNotNull(resultado.getFechaCreacion());

        verify(pedidoPersistenciaPuerto)
                .guardar(pedido);

        verificarEventoTrazabilidadSerializado();

        verificarEventoPendienteGuardado();
    }

    @Test
    void debeRechazarPedidoVacio() {
        Pedido pedido = crearPedido();
        pedido.setDetalles(List.of());

        ExcepcionPlazoleta excepcion =
                assertThrows(
                        ExcepcionPlazoleta.class,
                        () -> casoUso.realizar(pedido)
                );

        assertEquals(
                CodigosError.PEDIDO_VACIO,
                excepcion.getCodigo()
        );

        verificarQueNoSeGuardoPedidoNiEvento();
    }

    @Test
    void debeRechazarPedidoActivoExistente() {
        Pedido pedido = crearPedido();

        when(pedidoPersistenciaPuerto
                .existePedidoActivoPorCliente(
                        CLIENTE_ID
                ))
                .thenReturn(true);

        ExcepcionPlazoleta excepcion =
                assertThrows(
                        ExcepcionPlazoleta.class,
                        () -> casoUso.realizar(pedido)
                );

        assertEquals(
                CodigosError.PEDIDO_ACTIVO_EXISTENTE,
                excepcion.getCodigo()
        );

        verificarQueNoSeGuardoPedidoNiEvento();
    }

    @Test
    void debeRechazarPlatoDeOtroRestaurante() {
        Pedido pedido = crearPedido();

        when(pedidoPersistenciaPuerto
                .existePedidoActivoPorCliente(
                        CLIENTE_ID
                ))
                .thenReturn(false);

        when(restaurantePersistenciaPuerto
                .buscarPorId(RESTAURANTE_ID))
                .thenReturn(
                        Optional.of(
                                crearRestauranteActivo()
                        )
                );

        Plato platoAjeno = crearPlato(
                1L,
                "Plato ajeno",
                20000
        );

        platoAjeno.setRestauranteId(99L);

        when(platoPersistenciaPuerto
                .buscarActivosPorIds(
                        List.of(1L, 2L)
                ))
                .thenReturn(
                        List.of(
                                platoAjeno,
                                crearPlato(
                                        2L,
                                        "Limonada",
                                        8000
                                )
                        )
                );

        ExcepcionPlazoleta excepcion =
                assertThrows(
                        ExcepcionPlazoleta.class,
                        () -> casoUso.realizar(pedido)
                );

        assertEquals(
                CodigosError.PLATO_PEDIDO_INVALIDO,
                excepcion.getCodigo()
        );

        verificarQueNoSeGuardoPedidoNiEvento();
    }

    @Test
    void debeRechazarPlatoRepetido() {
        Pedido pedido = crearPedido();

        pedido.setDetalles(
                List.of(
                        new DetallePedido(
                                null,
                                1L,
                                null,
                                null,
                                1
                        ),
                        new DetallePedido(
                                null,
                                1L,
                                null,
                                null,
                                2
                        )
                )
        );

        ExcepcionPlazoleta excepcion =
                assertThrows(
                        ExcepcionPlazoleta.class,
                        () -> casoUso.realizar(pedido)
                );

        assertEquals(
                CodigosError.PLATO_REPETIDO,
                excepcion.getCodigo()
        );

        verificarQueNoSeGuardoPedidoNiEvento();
    }

    @Test
    void debeRechazarCantidadInvalida() {
        Pedido pedido = crearPedido();

        pedido.getDetalles()
                .get(0)
                .setCantidad(0);

        ExcepcionPlazoleta excepcion =
                assertThrows(
                        ExcepcionPlazoleta.class,
                        () -> casoUso.realizar(pedido)
                );

        assertEquals(
                CodigosError.CANTIDAD_PLATO_INVALIDA,
                excepcion.getCodigo()
        );

        verificarQueNoSeGuardoPedidoNiEvento();
    }

    private void verificarEventoTrazabilidadSerializado() {
        ArgumentCaptor<EventoTrazabilidad>
                captorEventoTrazabilidad =
                ArgumentCaptor.forClass(
                        EventoTrazabilidad.class
                );

        verify(eventoSerializadorPuerto)
                .serializar(
                        captorEventoTrazabilidad
                                .capture()
                );

        EventoTrazabilidad evento =
                captorEventoTrazabilidad
                        .getValue();

        assertEquals(
                PEDIDO_ID,
                evento.getPedidoId()
        );

        assertEquals(
                CLIENTE_ID,
                evento.getClienteId()
        );

        assertEquals(
                RESTAURANTE_ID,
                evento.getRestauranteId()
        );

        assertNull(evento.getEmpleadoId());
        assertNull(evento.getEstadoAnterior());

        assertEquals(
                EstadoPedido.PENDIENTE,
                evento.getEstadoNuevo()
        );
    }

    private void verificarEventoPendienteGuardado() {
        ArgumentCaptor<EventoPendiente>
                captorEventoPendiente =
                ArgumentCaptor.forClass(
                        EventoPendiente.class
                );

        verify(eventoPendientePersistenciaPuerto)
                .guardar(
                        captorEventoPendiente
                                .capture()
                );

        EventoPendiente eventoPendiente =
                captorEventoPendiente.getValue();

        assertEquals(
                TipoEvento.PEDIDO_CREADO,
                eventoPendiente.getTipoEvento()
        );

        assertEquals(
                PEDIDO_ID,
                eventoPendiente.getAgregadoId()
        );

        assertEquals(
                EVENTO_JSON,
                eventoPendiente.getCargaJson()
        );

        assertEquals(
                EstadoEnvioEvento.PENDIENTE,
                eventoPendiente.getEstadoEnvio()
        );

        assertEquals(
                0,
                eventoPendiente.getIntentos()
        );

        assertEquals(
                FECHA_FIJA,
                eventoPendiente.getProximoIntento()
        );

        assertEquals(
                FECHA_FIJA,
                eventoPendiente.getFechaCreacion()
        );

        assertNull(eventoPendiente.getFechaEnvio());
        assertNull(eventoPendiente.getUltimoError());

        assertEquals(
                0L,
                eventoPendiente.getVersion()
        );
    }

    private void verificarQueNoSeGuardoPedidoNiEvento() {
        verify(pedidoPersistenciaPuerto, never())
                .guardar(any(Pedido.class));

        verify(eventoSerializadorPuerto, never())
                .serializar(
                        any(EventoTrazabilidad.class)
                );

        verify(
                eventoPendientePersistenciaPuerto,
                never()
        ).guardar(any(EventoPendiente.class));
    }

    private Pedido crearPedido() {
        Pedido pedido = new Pedido();

        pedido.setClienteId(CLIENTE_ID);
        pedido.setRestauranteId(RESTAURANTE_ID);

        pedido.setDetalles(
                List.of(
                        new DetallePedido(
                                null,
                                1L,
                                null,
                                null,
                                2
                        ),
                        new DetallePedido(
                                null,
                                2L,
                                null,
                                null,
                                1
                        )
                )
        );

        return pedido;
    }

    private Restaurante crearRestauranteActivo() {
        Restaurante restaurante =
                new Restaurante();

        restaurante.setId(RESTAURANTE_ID);
        restaurante.setActivo(Boolean.TRUE);

        return restaurante;
    }

    private Plato crearPlato(
            Long id,
            String nombre,
            Integer precio) {

        Plato plato = new Plato();

        plato.setId(id);
        plato.setNombre(nombre);
        plato.setPrecio(precio);
        plato.setRestauranteId(
                RESTAURANTE_ID
        );
        plato.setActivo(Boolean.TRUE);

        return plato;
    }
}