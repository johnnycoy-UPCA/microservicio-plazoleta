package co.edu.clase.microservicio_plazoleta.dominio.casouso;

import co.edu.clase.microservicio_plazoleta.dominio.api.AsignarPedidoServicioPuerto;
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
import co.edu.clase.microservicio_plazoleta.util.constantes.MensajesError;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.CategoriaError;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoEnvioEvento;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoPedido;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.PerfilUsuario;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.TipoEvento;

import java.time.Clock;
import java.time.Instant;

public class AsignarPedidoCasoUso
        implements AsignarPedidoServicioPuerto {

    private final PedidoPersistenciaPuerto pedidoPersistenciaPuerto;
    private final EmpleadoConsultaPuerto empleadoConsultaPuerto;
    private final EventoPendientePersistenciaPuerto eventoPendientePersistenciaPuerto;
    private final EventoSerializadorPuerto eventoSerializadorPuerto;

    private final Clock reloj;

    public AsignarPedidoCasoUso(
            PedidoPersistenciaPuerto pedidoPersistenciaPuerto,
            EmpleadoConsultaPuerto empleadoConsultaPuerto,
            EventoPendientePersistenciaPuerto eventoPendientePersistenciaPuerto,
            EventoSerializadorPuerto eventoSerializadorPuerto,
            Clock reloj) {

        this.pedidoPersistenciaPuerto = pedidoPersistenciaPuerto;
        this.empleadoConsultaPuerto = empleadoConsultaPuerto;
        this.eventoPendientePersistenciaPuerto = eventoPendientePersistenciaPuerto;
        this.eventoSerializadorPuerto = eventoSerializadorPuerto;
        this.reloj = reloj;
    }

    @Override
    public Pedido asignar(
            Long pedidoId,
            Long empleadoId,
            String token) {

        EmpleadoValidacion empleado =
                empleadoConsultaPuerto.validar(
                        empleadoId,
                        token
                );

        validarEmpleado(empleado);

        Pedido pedido = pedidoPersistenciaPuerto
                .buscarPorId(pedidoId)
                .orElseThrow(() ->
                        new ExcepcionPlazoleta(
                                CodigosError.PEDIDO_NO_ENCONTRADO,
                                MensajesError.PEDIDO_NO_ENCONTRADO,
                                CategoriaError.NO_ENCONTRADO
                        )
                );

        validarRestaurante(pedido, empleado);
        validarSinAsignacion(pedido);
        validarEstado(pedido);

        pedido.setEmpleadoAsignadoId(empleadoId);
        pedido.setEstado(
                EstadoPedido.EN_PREPARACION
        );
        pedido.setFechaActualizacion(
                reloj.instant()
        );

        Pedido pedidoGuardado =
                pedidoPersistenciaPuerto.guardar(
                        pedido
                );

        guardarEventoPendiente(
                pedidoGuardado,
                empleadoId
        );

        return pedidoGuardado;
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

    private void validarRestaurante(
            Pedido pedido,
            EmpleadoValidacion empleado) {

        if (!empleado.getRestauranteId().equals(
                pedido.getRestauranteId()
        )) {
            throw new ExcepcionPlazoleta(
                    CodigosError
                            .PEDIDO_NO_PERTENECE_RESTAURANTE,
                    MensajesError
                            .PEDIDO_NO_PERTENECE_RESTAURANTE,
                    CategoriaError.ACCESO_DENEGADO
            );
        }
    }

    private void validarEstado(Pedido pedido) {
        if (!EstadoPedido.PENDIENTE.equals(
                pedido.getEstado()
        )) {
            throw new ExcepcionPlazoleta(
                    CodigosError
                            .PEDIDO_ESTADO_NO_ASIGNABLE,
                    MensajesError
                            .PEDIDO_ESTADO_NO_ASIGNABLE,
                    CategoriaError.CONFLICTO
            );
        }
    }

    private void validarSinAsignacion(
            Pedido pedido) {

        if (pedido.getEmpleadoAsignadoId() != null) {
            throw new ExcepcionPlazoleta(
                    CodigosError.PEDIDO_YA_ASIGNADO,
                    MensajesError.PEDIDO_YA_ASIGNADO,
                    CategoriaError.CONFLICTO
            );
        }
    }

    private void guardarEventoPendiente(
            Pedido pedido,
            Long empleadoId) {

        EventoTrazabilidad evento =
                new EventoTrazabilidad(
                        pedido.getId(),
                        pedido.getClienteId(),
                        pedido.getRestauranteId(),
                        empleadoId,
                        EstadoPedido.PENDIENTE,
                        EstadoPedido.EN_PREPARACION
                );

        String cargaJson =
                eventoSerializadorPuerto.serializar(
                        evento
                );

        Instant fechaActual = reloj.instant();

        EventoPendiente eventoPendiente =
                new EventoPendiente();

        eventoPendiente.setTipoEvento(
                TipoEvento.PEDIDO_EN_PREPARACION
        );
        eventoPendiente.setAgregadoId(
                pedido.getId()
        );
        eventoPendiente.setCargaJson(cargaJson);
        eventoPendiente.setEstadoEnvio(
                EstadoEnvioEvento.PENDIENTE
        );
        eventoPendiente.setIntentos(0);
        eventoPendiente.setProximoIntento(
                fechaActual
        );
        eventoPendiente.setFechaCreacion(
                fechaActual
        );
        eventoPendiente.setFechaEnvio(null);
        eventoPendiente.setUltimoError(null);
        eventoPendiente.setVersion(0L);

        eventoPendientePersistenciaPuerto.guardar(
                eventoPendiente
        );
    }
}