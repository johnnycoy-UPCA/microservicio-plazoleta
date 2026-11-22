package co.edu.clase.microservicio_plazoleta.dominio.casouso;

import co.edu.clase.microservicio_plazoleta.dominio.excepcion.ExcepcionPlazoleta;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.EmpleadoValidacion;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pagina;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pedido;
import co.edu.clase.microservicio_plazoleta.dominio.spi.EmpleadoConsultaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.PedidoPersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.util.constantes.CodigosError;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoPedido;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.PerfilUsuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarPedidosCasoUsoTest {

    private static final Long EMPLEADO_ID = 30L;
    private static final Long RESTAURANTE_ID = 1L;
    private static final String TOKEN = "token-empleado";

    @Mock
    private PedidoPersistenciaPuerto
            pedidoPersistenciaPuerto;

    @Mock
    private EmpleadoConsultaPuerto
            empleadoConsultaPuerto;

    @Test
    void debeListarPedidosDelRestaurante() {
        EmpleadoValidacion empleado =
                crearEmpleadoValido();

        Pagina<Pedido> paginaEsperada =
                new Pagina<>(
                        List.of(new Pedido()),
                        0,
                        10,
                        1L,
                        1,
                        true,
                        true
                );

        when(empleadoConsultaPuerto.validar(
                EMPLEADO_ID,
                TOKEN
        )).thenReturn(empleado);

        when(pedidoPersistenciaPuerto
                .listarPorRestauranteYEstado(
                        RESTAURANTE_ID,
                        EstadoPedido.PENDIENTE,
                        0,
                        10
                ))
                .thenReturn(paginaEsperada);

        ListarPedidosCasoUso casoUso =
                new ListarPedidosCasoUso(
                        pedidoPersistenciaPuerto,
                        empleadoConsultaPuerto
                );

        Pagina<Pedido> resultado =
                casoUso.listar(
                        EMPLEADO_ID,
                        TOKEN,
                        EstadoPedido.PENDIENTE,
                        0,
                        10
                );

        assertEquals(
                1,
                resultado.getContenido().size()
        );

        verify(pedidoPersistenciaPuerto)
                .listarPorRestauranteYEstado(
                        RESTAURANTE_ID,
                        EstadoPedido.PENDIENTE,
                        0,
                        10
                );
    }

    @Test
    void debeRechazarEstadoNulo() {
        ListarPedidosCasoUso casoUso =
                new ListarPedidosCasoUso(
                        pedidoPersistenciaPuerto,
                        empleadoConsultaPuerto
                );

        ExcepcionPlazoleta excepcion =
                assertThrows(
                        ExcepcionPlazoleta.class,
                        () -> casoUso.listar(
                                EMPLEADO_ID,
                                TOKEN,
                                null,
                                0,
                                10
                        )
                );

        assertEquals(
                CodigosError
                        .ESTADO_PEDIDO_OBLIGATORIO,
                excepcion.getCodigo()
        );

        verify(empleadoConsultaPuerto, never())
                .validar(EMPLEADO_ID, TOKEN);
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

        ListarPedidosCasoUso casoUso =
                new ListarPedidosCasoUso(
                        pedidoPersistenciaPuerto,
                        empleadoConsultaPuerto
                );

        ExcepcionPlazoleta excepcion =
                assertThrows(
                        ExcepcionPlazoleta.class,
                        () -> casoUso.listar(
                                EMPLEADO_ID,
                                TOKEN,
                                EstadoPedido.PENDIENTE,
                                0,
                                10
                        )
                );

        assertEquals(
                CodigosError.EMPLEADO_NO_VALIDO,
                excepcion.getCodigo()
        );
    }

    private EmpleadoValidacion crearEmpleadoValido() {
        return new EmpleadoValidacion(
                EMPLEADO_ID,
                PerfilUsuario.EMPLEADO,
                RESTAURANTE_ID,
                Boolean.TRUE
        );
    }
}