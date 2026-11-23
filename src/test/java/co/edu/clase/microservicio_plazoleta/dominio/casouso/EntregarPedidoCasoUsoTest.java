package co.edu.clase.microservicio_plazoleta.dominio.casouso;

import co.edu.clase.microservicio_plazoleta.dominio.excepcion.ExcepcionPlazoleta;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.ConfiguracionPin;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.EmpleadoValidacion;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.EventoPendiente;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.EventoTrazabilidad;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pedido;
import co.edu.clase.microservicio_plazoleta.dominio.spi.EmpleadoConsultaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.EventoPendientePersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.EventoSerializadorPuerto;
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
class EntregarPedidoCasoUsoTest {

    private static final Long PEDIDO_ID = 100L;
    private static final Long CLIENTE_ID = 20L;
    private static final Long EMPLEADO_ID = 30L;
    private static final Long RESTAURANTE_ID = 1L;

    private static final String TOKEN =
            "token-empleado";

    private static final String PIN =
            "482193";

    private static final String PIN_HASH =
            "pin-hash-protegido";

    private static final Instant FECHA_FIJA =
            Instant.parse("2026-11-23T13:00:00Z");

    private static final String EVENTO_JSON =
            "{\"pedidoId\":100,"
                    + "\"estadoAnterior\":\"LISTO\","
                    + "\"estadoNuevo\":\"ENTREGADO\"}";

    @Mock
    private PedidoPersistenciaPuerto
            pedidoPersistenciaPuerto;

    @Mock
    private EmpleadoConsultaPuerto
            empleadoConsultaPuerto;

    @Mock
    private ProtectorPinPuerto
            protectorPinPuerto;

    @Mock
    private EventoSerializadorPuerto
            eventoSerializadorPuerto;

    @Mock
    private EventoPendientePersistenciaPuerto
            eventoPendientePersistenciaPuerto;

    private EntregarPedidoCasoUso casoUso;

    @BeforeEach
    void configurar() {
        ConfiguracionPin configuracionPin =
                new ConfiguracionPin(
                        30,
                        5
                );

        Clock reloj = Clock.fixed(
                FECHA_FIJA,
                ZoneOffset.UTC
        );

        casoUso = new EntregarPedidoCasoUso(
                pedidoPersistenciaPuerto,
                empleadoConsultaPuerto,
                protectorPinPuerto,
                eventoSerializadorPuerto,
                eventoPendientePersistenciaPuerto,
                configuracionPin,
                reloj
        );
    }

    @Test
    void debeEntregarPedidoConPinCorrecto() {
        Pedido pedido = crearPedidoListo();

        configurarEmpleadoYPedido(pedido);

        when(protectorPinPuerto.coincide(
                PEDIDO_ID,
                PIN,
                PIN_HASH
        )).thenReturn(true);

        when(pedidoPersistenciaPuerto.guardar(
                any(Pedido.class)
        )).thenAnswer(invocacion ->
                invocacion.getArgument(0)
        );

        when(eventoSerializadorPuerto.serializar(
                any(EventoTrazabilidad.class)
        )).thenReturn(EVENTO_JSON);

        Pedido resultado = casoUso.entregar(
                PEDIDO_ID,
                EMPLEADO_ID,
                TOKEN,
                PIN
        );

        assertEquals(
                EstadoPedido.ENTREGADO,
                resultado.getEstado()
        );

        assertNull(resultado.getPinHash());
        assertNull(resultado.getPinGeneradoEn());
        assertNull(resultado.getPinExpiraEn());

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
    void debeIncrementarIntentosSiPinEsInvalido() {
        Pedido pedido = crearPedidoListo();

        configurarEmpleadoYPedido(pedido);

        when(protectorPinPuerto.coincide(
                PEDIDO_ID,
                "111111",
                PIN_HASH
        )).thenReturn(false);

        ExcepcionPlazoleta excepcion =
                assertThrows(
                        ExcepcionPlazoleta.class,
                        () -> casoUso.entregar(
                                PEDIDO_ID,
                                EMPLEADO_ID,
                                TOKEN,
                                "111111"
                        )
                );

        assertEquals(
                CodigosError.PIN_INVALIDO,
                excepcion.getCodigo()
        );

        assertEquals(
                1,
                pedido.getPinIntentosFallidos()
        );

        assertEquals(
                EstadoPedido.LISTO,
                pedido.getEstado()
        );

        assertEquals(
                PIN_HASH,
                pedido.getPinHash()
        );

        verify(pedidoPersistenciaPuerto)
                .guardar(pedido);

        verify(
                eventoPendientePersistenciaPuerto,
                never()
        ).guardar(any(EventoPendiente.class));
    }

    @Test
    void debeRechazarPinExpirado() {
        Pedido pedido = crearPedidoListo();

        pedido.setPinExpiraEn(
                FECHA_FIJA
        );

        configurarEmpleadoYPedido(pedido);

        ExcepcionPlazoleta excepcion =
                assertThrows(
                        ExcepcionPlazoleta.class,
                        () -> casoUso.entregar(
                                PEDIDO_ID,
                                EMPLEADO_ID,
                                TOKEN,
                                PIN
                        )
                );

        assertEquals(
                CodigosError.PIN_EXPIRADO,
                excepcion.getCodigo()
        );

        verify(protectorPinPuerto, never())
                .coincide(
                        any(),
                        any(),
                        any()
                );

        verificarQueNoSeGuardoNiCreoEvento();
    }

    @Test
    void debeRechazarIntentosAgotados() {
        Pedido pedido = crearPedidoListo();

        pedido.setPinIntentosFallidos(5);

        configurarEmpleadoYPedido(pedido);

        ExcepcionPlazoleta excepcion =
                assertThrows(
                        ExcepcionPlazoleta.class,
                        () -> casoUso.entregar(
                                PEDIDO_ID,
                                EMPLEADO_ID,
                                TOKEN,
                                PIN
                        )
                );

        assertEquals(
                CodigosError.PIN_INTENTOS_AGOTADOS,
                excepcion.getCodigo()
        );

        verificarQueNoSeGuardoNiCreoEvento();
    }

    @Test
    void debeRechazarPedidoQueNoEstaListo() {
        Pedido pedido = crearPedidoListo();

        pedido.setEstado(
                EstadoPedido.EN_PREPARACION
        );

        configurarEmpleadoYPedido(pedido);

        ExcepcionPlazoleta excepcion =
                assertThrows(
                        ExcepcionPlazoleta.class,
                        () -> casoUso.entregar(
                                PEDIDO_ID,
                                EMPLEADO_ID,
                                TOKEN,
                                PIN
                        )
                );

        assertEquals(
                CodigosError.PEDIDO_NO_LISTO,
                excepcion.getCodigo()
        );

        verificarQueNoSeGuardoNiCreoEvento();
    }

    @Test
    void debeRechazarEmpleadoNoAsignado() {
        Pedido pedido = crearPedidoListo();

        pedido.setEmpleadoAsignadoId(88L);

        configurarEmpleadoYPedido(pedido);

        ExcepcionPlazoleta excepcion =
                assertThrows(
                        ExcepcionPlazoleta.class,
                        () -> casoUso.entregar(
                                PEDIDO_ID,
                                EMPLEADO_ID,
                                TOKEN,
                                PIN
                        )
                );

        assertEquals(
                CodigosError
                        .EMPLEADO_NO_ASIGNADO_AL_PEDIDO,
                excepcion.getCodigo()
        );

        verificarQueNoSeGuardoNiCreoEvento();
    }

    @Test
    void debeRechazarPedidoSinPinGenerado() {
        Pedido pedido = crearPedidoListo();

        pedido.setPinHash(null);

        configurarEmpleadoYPedido(pedido);

        ExcepcionPlazoleta excepcion =
                assertThrows(
                        ExcepcionPlazoleta.class,
                        () -> casoUso.entregar(
                                PEDIDO_ID,
                                EMPLEADO_ID,
                                TOKEN,
                                PIN
                        )
                );

        assertEquals(
                CodigosError.PIN_NO_GENERADO,
                excepcion.getCodigo()
        );

        verificarQueNoSeGuardoNiCreoEvento();
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
                        () -> casoUso.entregar(
                                PEDIDO_ID,
                                EMPLEADO_ID,
                                TOKEN,
                                PIN
                        )
                );

        assertEquals(
                CodigosError.PEDIDO_NO_ENCONTRADO,
                excepcion.getCodigo()
        );

        verificarQueNoSeGuardoNiCreoEvento();
    }

    private void configurarEmpleadoYPedido(
            Pedido pedido) {

        when(empleadoConsultaPuerto.validar(
                EMPLEADO_ID,
                TOKEN
        )).thenReturn(crearEmpleadoValido());

        when(pedidoPersistenciaPuerto.buscarPorId(
                PEDIDO_ID
        )).thenReturn(Optional.of(pedido));
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
                EstadoPedido.LISTO,
                evento.getEstadoAnterior()
        );

        assertEquals(
                EstadoPedido.ENTREGADO,
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
                TipoEvento.PEDIDO_ENTREGADO,
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

        assertEquals(
                0,
                pendiente.getIntentos()
        );

        assertEquals(
                FECHA_FIJA,
                pendiente.getFechaCreacion()
        );
    }

    private void verificarQueNoSeGuardoNiCreoEvento() {
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

    private Pedido crearPedidoListo() {
        Pedido pedido = new Pedido();

        pedido.setId(PEDIDO_ID);
        pedido.setClienteId(CLIENTE_ID);
        pedido.setRestauranteId(RESTAURANTE_ID);
        pedido.setEmpleadoAsignadoId(EMPLEADO_ID);
        pedido.setEstado(EstadoPedido.LISTO);
        pedido.setTotal(48000L);
        pedido.setPinHash(PIN_HASH);

        pedido.setPinGeneradoEn(
                FECHA_FIJA.minusSeconds(600)
        );

        pedido.setPinExpiraEn(
                FECHA_FIJA.plusSeconds(1200)
        );

        pedido.setPinIntentosFallidos(0);
        pedido.setFechaCreacion(
                FECHA_FIJA.minusSeconds(3600)
        );
        pedido.setFechaActualizacion(
                FECHA_FIJA.minusSeconds(600)
        );
        pedido.setVersion(2L);

        return pedido;
    }
}