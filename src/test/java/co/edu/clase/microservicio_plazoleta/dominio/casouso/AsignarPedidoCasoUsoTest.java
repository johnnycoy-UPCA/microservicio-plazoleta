package co.edu.clase.microservicio_plazoleta.dominio.casouso;

import co.edu.clase.microservicio_plazoleta.dominio.excepcion.ExcepcionPlazoleta;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.EmpleadoValidacion;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.EventoPendiente;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.EventoTrazabilidad;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pedido;
import co.edu.clase.microservicio_plazoleta.dominio.spi.EmpleadoConsultaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.EventoPendientePersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.EventoSerializadorPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.PedidoPersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.util.constantes.CodigosError;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoEnvioEvento;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoPedido;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.PerfilUsuario;
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
class AsignarPedidoCasoUsoTest {

    private static final Long PEDIDO_ID = 100L;
    private static final Long CLIENTE_ID = 20L;
    private static final Long EMPLEADO_ID = 30L;
    private static final Long RESTAURANTE_ID = 1L;
    private static final String TOKEN = "token-empleado";

    private static final Instant FECHA_FIJA =
            Instant.parse("2026-11-22T15:00:00Z");

    private static final String EVENTO_JSON =
            "{\"pedidoId\":100,"
                    + "\"clienteId\":20,"
                    + "\"restauranteId\":1,"
                    + "\"empleadoId\":30,"
                    + "\"estadoAnterior\":\"PENDIENTE\","
                    + "\"estadoNuevo\":\"EN_PREPARACION\"}";

    @Mock
    private PedidoPersistenciaPuerto
            pedidoPersistenciaPuerto;

    @Mock
    private EmpleadoConsultaPuerto
            empleadoConsultaPuerto;

    @Mock
    private EventoPendientePersistenciaPuerto
            eventoPendientePersistenciaPuerto;

    @Mock
    private EventoSerializadorPuerto
            eventoSerializadorPuerto;

    private AsignarPedidoCasoUso casoUso;

    @BeforeEach
    void configurar() {
        Clock reloj = Clock.fixed(
                FECHA_FIJA,
                ZoneOffset.UTC
        );

        casoUso = new AsignarPedidoCasoUso(
                pedidoPersistenciaPuerto,
                empleadoConsultaPuerto,
                eventoPendientePersistenciaPuerto,
                eventoSerializadorPuerto,
                reloj
        );
    }

    @Test
    void debeAsignarPedidoYGuardarEventoPendiente() {
        EmpleadoValidacion empleado =
                crearEmpleadoValido();

        Pedido pedido = crearPedidoPendiente();

        when(empleadoConsultaPuerto.validar(
                EMPLEADO_ID,
                TOKEN
        )).thenReturn(empleado);

        when(pedidoPersistenciaPuerto.buscarPorId(
                PEDIDO_ID
        )).thenReturn(Optional.of(pedido));

        when(pedidoPersistenciaPuerto.guardar(
                any(Pedido.class)
        )).thenAnswer(invocacion ->
                invocacion.getArgument(0)
        );

        when(eventoSerializadorPuerto.serializar(
                any(EventoTrazabilidad.class)
        )).thenReturn(EVENTO_JSON);

        Pedido resultado = casoUso.asignar(
                PEDIDO_ID,
                EMPLEADO_ID,
                TOKEN
        );

        assertEquals(
                EMPLEADO_ID,
                resultado.getEmpleadoAsignadoId()
        );

        assertEquals(
                EstadoPedido.EN_PREPARACION,
                resultado.getEstado()
        );

        assertEquals(
                FECHA_FIJA,
                resultado.getFechaActualizacion()
        );

        verify(pedidoPersistenciaPuerto)
                .guardar(pedido);

        verificarEventoTrazabilidad();

        verificarEventoPendiente();
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
                        () -> casoUso.asignar(
                                PEDIDO_ID,
                                EMPLEADO_ID,
                                TOKEN
                        )
                );

        assertEquals(
                CodigosError.PEDIDO_NO_ENCONTRADO,
                excepcion.getCodigo()
        );

        verificarQueNoSeGuardoPedidoNiEvento();
    }

    @Test
    void debeRechazarPedidoDeOtroRestaurante() {
        Pedido pedido = crearPedidoPendiente();
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
                        () -> casoUso.asignar(
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

        verificarQueNoSeGuardoPedidoNiEvento();
    }

    @Test
    void debeRechazarPedidoConEstadoNoAsignable() {
        Pedido pedido = crearPedidoPendiente();
        pedido.setEstado(
                EstadoPedido.EN_PREPARACION
        );

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
                        () -> casoUso.asignar(
                                PEDIDO_ID,
                                EMPLEADO_ID,
                                TOKEN
                        )
                );

        assertEquals(
                CodigosError
                        .PEDIDO_ESTADO_NO_ASIGNABLE,
                excepcion.getCodigo()
        );

        verificarQueNoSeGuardoPedidoNiEvento();
    }

    @Test
    void debeRechazarPedidoYaAsignado() {
        Pedido pedido = crearPedidoPendiente();
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
                        () -> casoUso.asignar(
                                PEDIDO_ID,
                                EMPLEADO_ID,
                                TOKEN
                        )
                );

        assertEquals(
                CodigosError.PEDIDO_YA_ASIGNADO,
                excepcion.getCodigo()
        );

        verificarQueNoSeGuardoPedidoNiEvento();
    }

    @Test
    void debeRechazarEmpleadoInvalido() {
        EmpleadoValidacion empleado =
                new EmpleadoValidacion(
                        EMPLEADO_ID,
                        PerfilUsuario.CLIENTE,
                        RESTAURANTE_ID,
                        Boolean.TRUE
                );

        when(empleadoConsultaPuerto.validar(
                EMPLEADO_ID,
                TOKEN
        )).thenReturn(empleado);

        ExcepcionPlazoleta excepcion =
                assertThrows(
                        ExcepcionPlazoleta.class,
                        () -> casoUso.asignar(
                                PEDIDO_ID,
                                EMPLEADO_ID,
                                TOKEN
                        )
                );

        assertEquals(
                CodigosError.EMPLEADO_NO_VALIDO,
                excepcion.getCodigo()
        );

        verify(
                pedidoPersistenciaPuerto,
                never()
        ).buscarPorId(any());

        verificarQueNoSeGuardoPedidoNiEvento();
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
                CLIENTE_ID,
                evento.getClienteId()
        );

        assertEquals(
                RESTAURANTE_ID,
                evento.getRestauranteId()
        );

        assertEquals(
                EMPLEADO_ID,
                evento.getEmpleadoId()
        );

        assertEquals(
                EstadoPedido.PENDIENTE,
                evento.getEstadoAnterior()
        );

        assertEquals(
                EstadoPedido.EN_PREPARACION,
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

        EventoPendiente eventoPendiente =
                captor.getValue();

        assertEquals(
                TipoEvento.PEDIDO_EN_PREPARACION,
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

    private EmpleadoValidacion crearEmpleadoValido() {
        return new EmpleadoValidacion(
                EMPLEADO_ID,
                PerfilUsuario.EMPLEADO,
                RESTAURANTE_ID,
                Boolean.TRUE
        );
    }

    private Pedido crearPedidoPendiente() {
        Pedido pedido = new Pedido();

        pedido.setId(PEDIDO_ID);
        pedido.setClienteId(CLIENTE_ID);
        pedido.setRestauranteId(RESTAURANTE_ID);
        pedido.setEmpleadoAsignadoId(null);
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setTotal(48000L);
        pedido.setFechaCreacion(
                Instant.parse(
                        "2026-11-22T14:00:00Z"
                )
        );
        pedido.setFechaActualizacion(
                Instant.parse(
                        "2026-11-22T14:00:00Z"
                )
        );
        pedido.setVersion(0L);

        return pedido;
    }
}