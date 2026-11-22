package co.edu.clase.microservicio_plazoleta.dominio.casouso;

import co.edu.clase.microservicio_plazoleta.dominio.api.ListarPedidosServicioPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.excepcion.ExcepcionPlazoleta;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.EmpleadoValidacion;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pagina;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pedido;
import co.edu.clase.microservicio_plazoleta.dominio.spi.EmpleadoConsultaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.PedidoPersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.util.constantes.CodigosError;
import co.edu.clase.microservicio_plazoleta.util.constantes.ConstantesValidacion;
import co.edu.clase.microservicio_plazoleta.util.constantes.MensajesError;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.CategoriaError;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoPedido;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.PerfilUsuario;

public class ListarPedidosCasoUso
        implements ListarPedidosServicioPuerto {

    private final PedidoPersistenciaPuerto
            pedidoPersistenciaPuerto;

    private final EmpleadoConsultaPuerto
            empleadoConsultaPuerto;

    public ListarPedidosCasoUso(
            PedidoPersistenciaPuerto
                    pedidoPersistenciaPuerto,
            EmpleadoConsultaPuerto
                    empleadoConsultaPuerto) {

        this.pedidoPersistenciaPuerto =
                pedidoPersistenciaPuerto;

        this.empleadoConsultaPuerto =
                empleadoConsultaPuerto;
    }

    @Override
    public Pagina<Pedido> listar(
            Long empleadoId,
            String token,
            EstadoPedido estado,
            int pagina,
            int tamano) {

        validarEstado(estado);
        validarPagina(pagina);
        validarTamano(tamano);

        EmpleadoValidacion empleado =
                empleadoConsultaPuerto.validar(
                        empleadoId,
                        token
                );

        validarEmpleado(empleado);

        return pedidoPersistenciaPuerto
                .listarPorRestauranteYEstado(
                        empleado.getRestauranteId(),
                        estado,
                        pagina,
                        tamano
                );
    }

    private void validarEstado(EstadoPedido estado) {
        if (estado == null) {
            throw new ExcepcionPlazoleta(
                    CodigosError
                            .ESTADO_PEDIDO_OBLIGATORIO,
                    MensajesError
                            .ESTADO_PEDIDO_OBLIGATORIO,
                    CategoriaError.VALIDACION
            );
        }
    }

    private void validarPagina(int pagina) {
        if (pagina
                < ConstantesValidacion.PAGINA_INICIAL) {

            throw new ExcepcionPlazoleta(
                    CodigosError.PAGINA_INVALIDA,
                    MensajesError.PAGINA_INVALIDA,
                    CategoriaError.VALIDACION
            );
        }
    }

    private void validarTamano(int tamano) {
        if (tamano
                < ConstantesValidacion
                .TAMANO_PAGINA_MINIMO
                || tamano
                > ConstantesValidacion
                .TAMANO_PAGINA_MAXIMO) {

            throw new ExcepcionPlazoleta(
                    CodigosError
                            .TAMANO_PAGINA_INVALIDO,
                    MensajesError
                            .TAMANO_PAGINA_INVALIDO,
                    CategoriaError.VALIDACION
            );
        }
    }

    private void validarEmpleado(
            EmpleadoValidacion empleado) {

        boolean invalido =
                empleado == null
                        || empleado.getRestauranteId() == null
                        || !Boolean.TRUE.equals(
                        empleado.getActivo()
                )
                        || !PerfilUsuario.EMPLEADO.equals(
                        empleado.getPerfil()
                );

        if (invalido) {
            throw new ExcepcionPlazoleta(
                    CodigosError.EMPLEADO_NO_VALIDO,
                    MensajesError.EMPLEADO_NO_VALIDO,
                    CategoriaError.ACCESO_DENEGADO
            );
        }
    }
}