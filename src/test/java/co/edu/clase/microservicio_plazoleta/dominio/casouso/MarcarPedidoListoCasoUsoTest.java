package co.edu.clase.microservicio_plazoleta.dominio.casouso;

import co.edu.clase.microservicio_plazoleta.dominio.excepcion.ExcepcionPlazoleta;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.ConfiguracionPin;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.ContactoCliente;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.EmpleadoValidacion;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.EventoPendiente;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.EventoTrazabilidad;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.NotificacionPedido;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pedido;
import co.edu.clase.microservicio_plazoleta.dominio.spi.ClienteContactoConsultaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.EmpleadoConsultaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.EventoPendientePersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.EventoSerializadorPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.GeneradorPinPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.NotificacionPedidoPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.PedidoPersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.ProtectorPinPuerto;
import co.edu.clase.microservicio_plazoleta.util.constantes.CodigosError;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoEnvioEvento;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoPedido;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.PerfilUsuario;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.TipoEvento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
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
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MarcarPedidoListoCasoUsoTest {

    private static final Long PEDIDO_ID = 100L;
    private static final Long CLIENTE_ID = 20L;
    private static final Long EMPLEADO_ID = 30L;
    private static final Long RESTAURANTE_ID = 1L;
    private static final String TOKEN = "token-empleado";
    private static final String PIN = "482193";
    private static final String PIN_HASH = "pin-hash-protegido";

    private static final Instant FECHA_FIJA =
            Instant.parse("2026-11-22T21:00:00Z");

    private static final String EVENTO_JSON =
            "{\"pedidoId\":100,"
                    + "\"estadoAnterior\":\"EN_PREPARACION\","
                    + "\"estadoNuevo\":\"LISTO\"}";

    @Mock
    private PedidoPersistenciaPuerto
            pedidoPersistenciaPuerto;

    @Mock
    private EmpleadoConsultaPuerto
            empleadoConsultaPuerto;

    @Mock
    private ClienteContactoConsultaPuerto
            clienteContactoConsultaPuerto;

    @Mock
    private NotificacionPedidoPuerto
            notificacionPedidoPuerto;

    @Mock
    private GeneradorPinPuerto generadorPinPuerto;

    @Mock
    private ProtectorPinPuerto protectorPinPuerto;

    @Mock
    private EventoSerializadorPuerto
            eventoSerializadorPuerto;

    @Mock
    private EventoPendientePersistenciaPuerto
            eventoPendientePersistenciaPuerto;

    private MarcarPedidoListoCasoUso casoUso;

    @BeforeEach
    void configurar() {
        Clock reloj = Clock.fixed(
                FECHA_FIJA,
                ZoneOffset.UTC
        );

        ConfiguracionPin configuracionPin =
                new ConfiguracionPin(
                        30,
                        5
                );

        casoUso = new MarcarPedidoListoCasoUso(
                pedidoPersistenciaPuerto,
                empleadoConsultaPuerto,
                clienteContactoConsultaPuerto,
                notificacionPedidoPuerto,
                generadorPinPuerto,
                protectorPinPuerto,
                eventoSerializadorPuerto,
                eventoPendientePersistenciaPuerto,
                configuracionPin,
                reloj
        );
    }

    @Test
    void debeMarcarPedidoListoNotificarYGuardarEvento() {
        Pedido pedido = crearPedidoEnPreparacion();

        when(empleadoConsultaPuerto.validar(
                EMPLEADO_ID,
                TOKEN
        )).thenReturn(crearEmpleadoValido());

        when(pedidoPersistenciaPuerto.buscarPorId(
                PEDIDO_ID
        )).thenReturn(Optional.of(pedido));

        when(clienteContactoConsultaPuerto.consultar(
                CLIENTE_ID
        )).thenReturn(crearContactoValido());

        when(generadorPinPuerto.generar())
                .thenReturn(PIN);

        when(protectorPinPuerto.proteger(
                PEDIDO_ID,
                PIN
        )).thenReturn(PIN_HASH);

        when(pedidoPersistenciaPuerto.guardar(
                any(Pedido.class)
        )).thenAnswer(invocacion ->
                invocacion.getArgument(0)
        );

        when(eventoSerializadorPuerto.serializar(
                any(EventoTrazabilidad.class)
        )).thenReturn(EVENTO_JSON);

        Pedido resultado = casoUso.marcarListo(
                PEDIDO_ID,
                EMPLEADO_ID,
                TOKEN
        );

        assertEquals(
                EstadoPedido.LISTO,
                resultado.getEstado()
        );

        assertEquals(
                PIN_HASH,
                resultado.getPinHash()
        );

        assertEquals(
                FECHA_FIJA,
                resultado.getPinGeneradoEn()
        );

        assertEquals(
                FECHA_FIJA.plusSeconds(1800),
                resultado.getPinExpiraEn()
        );

        assertEquals(
                0,
                resultado.getPinIntentosFallidos()
        );

        assertEquals(
                FECHA_FIJA,
                resultado.getFechaActualizacion()
        );

        verificarNotificacion();
        verificarEventoTrazabilidad();
        verificarEventoPendiente();

        InOrder orden = inOrder(
                notificacionPedidoPuerto,
                pedidoPersistenciaPuerto,
                eventoPendientePersistenciaPuerto
        );

        orden.verify(notificacionPedidoPuerto)
                .notificar(any(NotificacionPedido.class));

        orden.verify(pedidoPersistenciaPuerto)
                .guardar(pedido);

        orden.verify(eventoPendientePersistenciaPuerto)
                .guardar(any(EventoPendiente.class));
    }

    @Test
    void debeRechazarPedidoInexistente() {
        when(empleadoConsultaPuerto.validar(
                EMPLEADO_ID,
                TOKEN
        )).thenReturn(crearEmpleadoValido());

        when(pedidoPersistenciaPuerto.buscarPorId(
                PEDIDO_ID
        )).thenReturn(Optional.empty());

        ExcepcionPlazoleta excepcion =
                assertThrows(
                        ExcepcionPlazoleta.class,
                        () -> casoUso.marcarListo(
                                PEDIDO_ID,
                                EMPLEADO_ID,
                                TOKEN
                        )
                );

        assertEquals(
                CodigosError.PEDIDO_NO_ENCONTRADO,
                excepcion.getCodigo()
        );

        verificarQueNoNotificoNiGuardo();
    }

    @Test
    void debeRechazarPedidoDeOtroRestaurante() {
        Pedido pedido = crearPedidoEnPreparacion();
        pedido.setRestauranteId(99L);

        when(empleadoConsultaPuerto.validar(
                EMPLEADO_ID,
                TOKEN
        )).thenReturn(crearEmpleadoValido());

        when(pedidoPersistenciaPuerto.buscarPorId(
                PEDIDO_ID
        )).thenReturn(Optional.of(pedido));

        ExcepcionPlazoleta excepcion =
                assertThrows(
                        ExcepcionPlazoleta.class,
                        () -> casoUso.marcarListo(
                                PEDIDO_ID,
                                EMPLEADO_ID,
                                TOKEN
                        )
                );

        assertEquals(
                CodigosError
                        .PEDIDO_NO_PERTENECE_RESTAURANTE,
                excepcion.getCodigo()
        );

        verificarQueNoNotificoNiGuardo();
    }

    @Test
    void debeRechazarPedidoQueNoEstaEnPreparacion() {
        Pedido pedido = crearPedidoEnPreparacion();
        pedido.setEstado(EstadoPedido.PENDIENTE);

        when(empleadoConsultaPuerto.validar(
                EMPLEADO_ID,
                TOKEN
        )).thenReturn(crearEmpleadoValido());

        when(pedidoPersistenciaPuerto.buscarPorId(
                PEDIDO_ID
        )).thenReturn(Optional.of(pedido));

        ExcepcionPlazoleta excepcion =
                assertThrows(
                        ExcepcionPlazoleta.class,
                        () -> casoUso.marcarListo(
                                PEDIDO_ID,
                                EMPLEADO_ID,
                                TOKEN
                        )
                );

        assertEquals(
                CodigosError
                        .PEDIDO_ESTADO_NO_PREPARABLE,
                excepcion.getCodigo()
        );

        verificarQueNoNotificoNiGuardo();
    }

    @Test
    void debeRechazarEmpleadoNoAsignado() {
        Pedido pedido = crearPedidoEnPreparacion();
        pedido.setEmpleadoAsignadoId(88L);

        when(empleadoConsultaPuerto.validar(
                EMPLEADO_ID,
                TOKEN
        )).thenReturn(crearEmpleadoValido());

        when(pedidoPersistenciaPuerto.buscarPorId(
                PEDIDO_ID
        )).thenReturn(Optional.of(pedido));

        ExcepcionPlazoleta excepcion =
                assertThrows(
                        ExcepcionPlazoleta.class,
                        () -> casoUso.marcarListo(
                                PEDIDO_ID,
                                EMPLEADO_ID,
                                TOKEN
                        )
                );

        assertEquals(
                CodigosError
                        .EMPLEADO_NO_ASIGNADO_AL_PEDIDO,
                excepcion.getCodigo()
        );

        verificarQueNoNotificoNiGuardo();
    }

    @Test
    void debeMantenerPedidoSinCambiosSiMensajeriaFalla() {
        Pedido pedido = crearPedidoEnPreparacion();

        when(empleadoConsultaPuerto.validar(
                EMPLEADO_ID,
                TOKEN
        )).thenReturn(crearEmpleadoValido());

        when(pedidoPersistenciaPuerto.buscarPorId(
                PEDIDO_ID
        )).thenReturn(Optional.of(pedido));

        when(clienteContactoConsultaPuerto.consultar(
                CLIENTE_ID
        )).thenReturn(crearContactoValido());

        when(generadorPinPuerto.generar())
                .thenReturn(PIN);

        when(protectorPinPuerto.proteger(
                PEDIDO_ID,
                PIN
        )).thenReturn(PIN_HASH);

        ExcepcionPlazoleta errorMensajeria =
                new ExcepcionPlazoleta(
                        CodigosError
                                .SERVICIO_MENSAJERIA_NO_DISPONIBLE,
                        "Mensajería no disponible",
                        co.edu.clase.microservicio_plazoleta
                                .util.enumeraciones.CategoriaError
                                .DEPENDENCIA_EXTERNA
                );

        org.mockito.Mockito.doThrow(
                errorMensajeria
        ).when(notificacionPedidoPuerto)
                .notificar(any(NotificacionPedido.class));

        assertThrows(
                ExcepcionPlazoleta.class,
                () -> casoUso.marcarListo(
                        PEDIDO_ID,
                        EMPLEADO_ID,
                        TOKEN
                )
        );

        assertEquals(
                EstadoPedido.EN_PREPARACION,
                pedido.getEstado()
        );

        assertNull(pedido.getPinHash());
        assertNull(pedido.getPinGeneradoEn());
        assertNull(pedido.getPinExpiraEn());

        verify(pedidoPersistenciaPuerto, never())
                .guardar(any(Pedido.class));

        verify(
                eventoPendientePersistenciaPuerto,
                never()
        ).guardar(any(EventoPendiente.class));
    }

    private void verificarNotificacion() {
        ArgumentCaptor<NotificacionPedido> captor =
                ArgumentCaptor.forClass(
                        NotificacionPedido.class
                );

        verify(notificacionPedidoPuerto)
                .notificar(captor.capture());

        NotificacionPedido notificacion =
                captor.getValue();

        assertEquals(
                PEDIDO_ID,
                notificacion.getPedidoId()
        );

        assertEquals(
                "+573001234567",
                notificacion.getCelular()
        );

        assertEquals(
                "Pedido listo para recoger. "
                        + "PIN para reclamar pedido: "
                        + PIN,
                notificacion.getMensaje()
        );
    }

    private void verificarEventoTrazabilidad() {
        ArgumentCaptor<EventoTrazabilidad> captor =
                ArgumentCaptor.forClass(
                        EventoTrazabilidad.class
                );

        verify(eventoSerializadorPuerto)
                .serializar(captor.capture());

        EventoTrazabilidad evento =
                captor.getValue();

        assertEquals(
                PEDIDO_ID,
                evento.getPedidoId()
        );

        assertEquals(
                EMPLEADO_ID,
                evento.getEmpleadoId()
        );

        assertEquals(
                EstadoPedido.EN_PREPARACION,
                evento.getEstadoAnterior()
        );

        assertEquals(
                EstadoPedido.LISTO,
                evento.getEstadoNuevo()
        );
    }

    private void verificarEventoPendiente() {
        ArgumentCaptor<EventoPendiente> captor =
                ArgumentCaptor.forClass(
                        EventoPendiente.class
                );

        verify(eventoPendientePersistenciaPuerto)
                .guardar(captor.capture());

        EventoPendiente pendiente =
                captor.getValue();

        assertEquals(
                TipoEvento.PEDIDO_LISTO,
                pendiente.getTipoEvento()
        );

        assertEquals(
                PEDIDO_ID,
                pendiente.getAgregadoId()
        );

        assertEquals(
                EVENTO_JSON,
                pendiente.getCargaJson()
        );

        assertEquals(
                EstadoEnvioEvento.PENDIENTE,
                pendiente.getEstadoEnvio()
        );

        assertEquals(0, pendiente.getIntentos());
        assertEquals(
                FECHA_FIJA,
                pendiente.getFechaCreacion()
        );
    }

    private void verificarQueNoNotificoNiGuardo() {
        verify(notificacionPedidoPuerto, never())
                .notificar(any(NotificacionPedido.class));

        verify(pedidoPersistenciaPuerto, never())
                .guardar(any(Pedido.class));

        verify(
                eventoPendientePersistenciaPuerto,
                never()
        ).guardar(any(EventoPendiente.class));
    }

    private EmpleadoValidacion crearEmpleadoValido() {
        return new EmpleadoValidacion(
                EMPLEADO_ID,
                PerfilUsuario.EMPLEADO,
                RESTAURANTE_ID,
                Boolean.TRUE
        );
    }

    private ContactoCliente crearContactoValido() {
        return new ContactoCliente(
                CLIENTE_ID,
                "+573001234567",
                Boolean.TRUE
        );
    }

    private Pedido crearPedidoEnPreparacion() {
        Pedido pedido = new Pedido();

        pedido.setId(PEDIDO_ID);
        pedido.setClienteId(CLIENTE_ID);
        pedido.setRestauranteId(RESTAURANTE_ID);
        pedido.setEmpleadoAsignadoId(EMPLEADO_ID);
        pedido.setEstado(
                EstadoPedido.EN_PREPARACION
        );
        pedido.setTotal(48000L);
        pedido.setFechaCreacion(
                Instant.parse(
                        "2026-11-22T19:00:00Z"
                )
        );
        pedido.setFechaActualizacion(
                Instant.parse(
                        "2026-11-22T20:00:00Z"
                )
        );
        pedido.setPinHash(null);
        pedido.setPinGeneradoEn(null);
        pedido.setPinExpiraEn(null);
        pedido.setPinIntentosFallidos(0);
        pedido.setVersion(1L);

        return pedido;
    }
}