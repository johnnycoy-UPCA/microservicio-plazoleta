package co.edu.clase.microservicio_plazoleta.dominio.casouso;

import co.edu.clase.microservicio_plazoleta.dominio.excepcion.ExcepcionPlazoleta;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.DetallePedido;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.EventoTrazabilidad;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pedido;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Plato;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Restaurante;
import co.edu.clase.microservicio_plazoleta.dominio.spi.PedidoPersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.PlatoPersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.RestaurantePersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.TrazabilidadRegistroPuerto;
import co.edu.clase.microservicio_plazoleta.util.constantes.CodigosError;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoPedido;
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

    @Mock
    private PedidoPersistenciaPuerto pedidoPersistenciaPuerto;
    @Mock
    private PlatoPersistenciaPuerto platoPersistenciaPuerto;
    @Mock
    private RestaurantePersistenciaPuerto restaurantePersistenciaPuerto;
	@Mock
	private TrazabilidadRegistroPuerto trazabilidadRegistroPuerto;

    private RealizarPedidoCasoUso casoUso;

    @BeforeEach
    void configurar() {
        Clock reloj = Clock.fixed(
                Instant.parse("2026-11-19T18:00:00Z"),
                ZoneOffset.UTC
        );

        casoUso = new RealizarPedidoCasoUso(
                pedidoPersistenciaPuerto,
                platoPersistenciaPuerto,
                restaurantePersistenciaPuerto,
				trazabilidadRegistroPuerto,
                reloj
        );
    }

    @Test
    void debeRealizarPedidoCorrectamente() {
        Pedido pedido = crearPedido();

        when(pedidoPersistenciaPuerto
                .existePedidoActivoPorCliente(
                        CLIENTE_ID
                ))
                .thenReturn(false);

        when(restaurantePersistenciaPuerto
                .buscarPorId(RESTAURANTE_ID))
                .thenReturn(Optional.of(
                        crearRestauranteActivo()
                ));

        when(platoPersistenciaPuerto
                .buscarActivosPorIds(
                        List.of(1L, 2L)
                ))
                .thenReturn(List.of(
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
                ));

        when(pedidoPersistenciaPuerto.guardar(
                any(Pedido.class)
        )).thenAnswer(invocacion -> {
            Pedido guardado =
                    invocacion.getArgument(0);

            guardado.setId(100L);
            return guardado;
        });

        Pedido resultado = casoUso.realizar(pedido);

        assertEquals(100L, resultado.getId());
        assertEquals(
                EstadoPedido.PENDIENTE,
                resultado.getEstado()
        );
        assertEquals(48000L, resultado.getTotal());
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
        assertNotNull(resultado.getFechaCreacion());

        verify(pedidoPersistenciaPuerto)
                .guardar(pedido);

		// verify(trazabilidadRegistroPuerto)
		// 		.registrar(any(EventoTrazabilidad.class));

		ArgumentCaptor<EventoTrazabilidad> captorEvento =
				ArgumentCaptor.forClass(
						EventoTrazabilidad.class
				);

		verify(trazabilidadRegistroPuerto)
				.registrar(captorEvento.capture());

		EventoTrazabilidad eventoRegistrado =
				captorEvento.getValue();

		assertEquals(
				100L,
				eventoRegistrado.getPedidoId()
		);
		assertEquals(
				CLIENTE_ID,
				eventoRegistrado.getClienteId()
		);
		assertEquals(
				RESTAURANTE_ID,
				eventoRegistrado.getRestauranteId()
		);
		assertNull(
				eventoRegistrado.getEmpleadoId()
		);
		assertNull(
				eventoRegistrado.getEstadoAnterior()
		);
		assertEquals(
				EstadoPedido.PENDIENTE,
				eventoRegistrado.getEstadoNuevo()
		);
    }

    @Test
    void debeRechazarPedidoVacio() {
        Pedido pedido = crearPedido();
        pedido.setDetalles(List.of());

        ExcepcionPlazoleta excepcion = assertThrows(
                ExcepcionPlazoleta.class,
                () -> casoUso.realizar(pedido)
        );

        assertEquals(
                CodigosError.PEDIDO_VACIO,
                excepcion.getCodigo()
        );

        verify(pedidoPersistenciaPuerto, never())
                .guardar(any());

		verify(trazabilidadRegistroPuerto, never())
				.registrar(any(EventoTrazabilidad.class));
    }

    @Test
    void debeRechazarPedidoActivoExistente() {
        Pedido pedido = crearPedido();

        when(pedidoPersistenciaPuerto
                .existePedidoActivoPorCliente(
                        CLIENTE_ID
                ))
                .thenReturn(true);

        ExcepcionPlazoleta excepcion = assertThrows(
                ExcepcionPlazoleta.class,
                () -> casoUso.realizar(pedido)
        );

        assertEquals(
                CodigosError.PEDIDO_ACTIVO_EXISTENTE,
                excepcion.getCodigo()
        );

		verify(trazabilidadRegistroPuerto, never())
				.registrar(any(EventoTrazabilidad.class));
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
                .thenReturn(Optional.of(
                        crearRestauranteActivo()
                ));

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
                .thenReturn(List.of(
                        platoAjeno,
                        crearPlato(
                                2L,
                                "Limonada",
                                8000
                        )
                ));

        ExcepcionPlazoleta excepcion = assertThrows(
                ExcepcionPlazoleta.class,
                () -> casoUso.realizar(pedido)
        );

        assertEquals(
                CodigosError.PLATO_PEDIDO_INVALIDO,
                excepcion.getCodigo()
        );

		verify(trazabilidadRegistroPuerto, never())
				.registrar(any(EventoTrazabilidad.class));
    }

    private Pedido crearPedido() {
        Pedido pedido = new Pedido();

        pedido.setClienteId(CLIENTE_ID);
        pedido.setRestauranteId(RESTAURANTE_ID);
        pedido.setDetalles(List.of(
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
        ));

        return pedido;
    }

    private Restaurante crearRestauranteActivo() {
        Restaurante restaurante = new Restaurante();

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
        plato.setRestauranteId(RESTAURANTE_ID);
        plato.setActivo(Boolean.TRUE);

        return plato;
    }
}