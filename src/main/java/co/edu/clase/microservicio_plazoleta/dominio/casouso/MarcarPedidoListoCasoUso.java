package co.edu.clase.microservicio_plazoleta.dominio.casouso;

import co.edu.clase.microservicio_plazoleta.dominio.api.MarcarPedidoListoServicioPuerto;
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
// import co.edu.clase.microservicio_plazoleta.infraestructura.configuracion.PinPropiedades;
import co.edu.clase.microservicio_plazoleta.util.constantes.CodigosError;
import co.edu.clase.microservicio_plazoleta.util.constantes.MensajesError;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.CategoriaError;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoEnvioEvento;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoPedido;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.PerfilUsuario;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.TipoEvento;

import java.time.Clock;
import java.time.Instant;

public class MarcarPedidoListoCasoUso
        implements MarcarPedidoListoServicioPuerto {

    private static final String MENSAJE_BASE =
            "Pedido listo para recoger. "
            + "PIN para reclamar pedido: ";

    private final PedidoPersistenciaPuerto pedidoPersistenciaPuerto;
    private final EmpleadoConsultaPuerto empleadoConsultaPuerto;
    private final ClienteContactoConsultaPuerto clienteContactoConsultaPuerto;
    private final NotificacionPedidoPuerto notificacionPedidoPuerto;
    private final GeneradorPinPuerto generadorPinPuerto;
    private final ProtectorPinPuerto protectorPinPuerto;
    private final EventoSerializadorPuerto eventoSerializadorPuerto;
    private final EventoPendientePersistenciaPuerto eventoPendientePersistenciaPuerto;
    // private final PinPropiedades pinPropiedades;
    private final ConfiguracionPin configuracionPin;

    private final Clock reloj;

    public MarcarPedidoListoCasoUso(
            PedidoPersistenciaPuerto pedidoPersistenciaPuerto,
            EmpleadoConsultaPuerto empleadoConsultaPuerto,
            ClienteContactoConsultaPuerto clienteContactoConsultaPuerto,
            NotificacionPedidoPuerto notificacionPedidoPuerto,
            GeneradorPinPuerto generadorPinPuerto,
            ProtectorPinPuerto protectorPinPuerto,
            EventoSerializadorPuerto eventoSerializadorPuerto,
            EventoPendientePersistenciaPuerto eventoPendientePersistenciaPuerto,
            // PinPropiedades pinPropiedades,
            ConfiguracionPin configuracionPin,
            Clock reloj) {

        this.pedidoPersistenciaPuerto = pedidoPersistenciaPuerto;
        this.empleadoConsultaPuerto = empleadoConsultaPuerto;
        this.clienteContactoConsultaPuerto = clienteContactoConsultaPuerto;
        this.notificacionPedidoPuerto = notificacionPedidoPuerto;
        this.generadorPinPuerto = generadorPinPuerto;
        this.protectorPinPuerto = protectorPinPuerto;
        this.eventoSerializadorPuerto = eventoSerializadorPuerto;
        this.eventoPendientePersistenciaPuerto = eventoPendientePersistenciaPuerto;
        // this.pinPropiedades = pinPropiedades;
        this.configuracionPin = configuracionPin;
        this.reloj = reloj;
    }

    @Override
    public Pedido marcarListo(
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
        validarEstado(pedido);
        validarEmpleadoAsignado(pedido, empleadoId);

        ContactoCliente contacto =
                clienteContactoConsultaPuerto.consultar(
                        pedido.getClienteId()
                );

        validarContacto(contacto);

        String pin = generadorPinPuerto.generar();

        String pinHash = protectorPinPuerto.proteger(
                pedido.getId(),
                pin
        );

        notificacionPedidoPuerto.notificar(
                new NotificacionPedido(
                        pedido.getId(),
                        contacto.getCelular(),
                        MENSAJE_BASE + pin
                )
        );

        Instant fechaActual = reloj.instant();

        pedido.setEstado(EstadoPedido.LISTO);
        pedido.setPinHash(pinHash);
        pedido.setPinGeneradoEn(fechaActual);
        pedido.setPinExpiraEn(
                fechaActual.plusSeconds(
                        // pinPropiedades.getDuracionMinutos()
                        configuracionPin.getDuracionMinutos()
                                * 60L
                )
        );
        pedido.setPinIntentosFallidos(0);
        pedido.setFechaActualizacion(fechaActual);

        Pedido guardado =
                pedidoPersistenciaPuerto.guardar(pedido);

        guardarEventoPendiente(
                guardado,
                empleadoId,
                fechaActual
        );

        return guardado;
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
                    CodigosError.PEDIDO_NO_PERTENECE_RESTAURANTE,
                    MensajesError.PEDIDO_NO_PERTENECE_RESTAURANTE,
                    CategoriaError.ACCESO_DENEGADO
            );
        }
    }

    private void validarEstado(Pedido pedido) {
        if (!EstadoPedido.EN_PREPARACION.equals(
                pedido.getEstado()
        )) {
            throw new ExcepcionPlazoleta(
                    CodigosError.PEDIDO_ESTADO_NO_PREPARABLE,
                    MensajesError.PEDIDO_ESTADO_NO_PREPARABLE,
                    CategoriaError.CONFLICTO
            );
        }
    }

    private void validarEmpleadoAsignado(
            Pedido pedido,
            Long empleadoId) {

        if (!empleadoId.equals(
                pedido.getEmpleadoAsignadoId()
        )) {
            throw new ExcepcionPlazoleta(
                    CodigosError.EMPLEADO_NO_ASIGNADO_AL_PEDIDO,
                    MensajesError.EMPLEADO_NO_ASIGNADO_AL_PEDIDO,
                    CategoriaError.ACCESO_DENEGADO
            );
        }
    }

    private void validarContacto(
            ContactoCliente contacto) {

        if (contacto == null
                || contacto.getCelular() == null
                || contacto.getCelular().isBlank()
                || !Boolean.TRUE.equals(
                contacto.getActivo()
        )) {
            throw new ExcepcionPlazoleta(
                    CodigosError.CONTACTO_CLIENTE_NO_DISPONIBLE,
                    MensajesError.CONTACTO_CLIENTE_NO_DISPONIBLE,
                    CategoriaError.VALIDACION
            );
        }
    }

    private void guardarEventoPendiente(
            Pedido pedido,
            Long empleadoId,
            Instant fechaActual) {

        EventoTrazabilidad evento =
                new EventoTrazabilidad(
                        pedido.getId(),
                        pedido.getClienteId(),
                        pedido.getRestauranteId(),
                        empleadoId,
                        EstadoPedido.EN_PREPARACION,
                        EstadoPedido.LISTO
                );

        EventoPendiente pendiente =
                new EventoPendiente();

        pendiente.setTipoEvento(
                TipoEvento.PEDIDO_LISTO
        );
        pendiente.setAgregadoId(pedido.getId());
        pendiente.setCargaJson(
                eventoSerializadorPuerto.serializar(
                        evento
                )
        );
        pendiente.setEstadoEnvio(
                EstadoEnvioEvento.PENDIENTE
        );
        pendiente.setIntentos(0);
        pendiente.setProximoIntento(fechaActual);
        pendiente.setFechaCreacion(fechaActual);
        pendiente.setFechaEnvio(null);
        pendiente.setUltimoError(null);
        pendiente.setVersion(0L);

        eventoPendientePersistenciaPuerto.guardar(
                pendiente
        );
    }
}