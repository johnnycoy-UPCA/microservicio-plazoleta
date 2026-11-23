package co.edu.clase.microservicio_plazoleta.dominio.casouso;

import co.edu.clase.microservicio_plazoleta.dominio.api.EntregarPedidoServicioPuerto;
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
import co.edu.clase.microservicio_plazoleta.util.constantes.MensajesError;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.CategoriaError;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoEnvioEvento;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoPedido;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.PerfilUsuario;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.TipoEvento;

import java.time.Clock;
import java.time.Instant;

public class EntregarPedidoCasoUso
        implements EntregarPedidoServicioPuerto {

    private final PedidoPersistenciaPuerto
            pedidoPersistenciaPuerto;

    private final EmpleadoConsultaPuerto
            empleadoConsultaPuerto;

    private final ProtectorPinPuerto
            protectorPinPuerto;

    private final EventoSerializadorPuerto
            eventoSerializadorPuerto;

    private final EventoPendientePersistenciaPuerto
            eventoPendientePersistenciaPuerto;

    private final ConfiguracionPin configuracionPin;

    private final Clock reloj;

    public EntregarPedidoCasoUso(
            PedidoPersistenciaPuerto
                    pedidoPersistenciaPuerto,
            EmpleadoConsultaPuerto
                    empleadoConsultaPuerto,
            ProtectorPinPuerto protectorPinPuerto,
            EventoSerializadorPuerto
                    eventoSerializadorPuerto,
            EventoPendientePersistenciaPuerto
                    eventoPendientePersistenciaPuerto,
            ConfiguracionPin configuracionPin,
            Clock reloj) {

        this.pedidoPersistenciaPuerto =
                pedidoPersistenciaPuerto;

        this.empleadoConsultaPuerto =
                empleadoConsultaPuerto;

        this.protectorPinPuerto =
                protectorPinPuerto;

        this.eventoSerializadorPuerto =
                eventoSerializadorPuerto;

        this.eventoPendientePersistenciaPuerto =
                eventoPendientePersistenciaPuerto;

        this.configuracionPin = configuracionPin;
        this.reloj = reloj;
    }

    @Override
    public Pedido entregar(
            Long pedidoId,
            Long empleadoId,
            String token,
            String pin) {

        EmpleadoValidacion empleado =
                empleadoConsultaPuerto.validar(
                        empleadoId,
                        token
                );

        validarEmpleado(empleado);

        Pedido pedido = pedidoPersistenciaPuerto
                .buscarPorId(pedidoId)
                .orElseThrow(() ->
                        crearExcepcion(
                                CodigosError
                                        .PEDIDO_NO_ENCONTRADO,
                                MensajesError
                                        .PEDIDO_NO_ENCONTRADO,
                                CategoriaError
                                        .NO_ENCONTRADO
                        )
                );

        validarRestaurante(pedido, empleado);
        validarEmpleadoAsignado(pedido, empleadoId);
        validarEstado(pedido);
        validarPinGenerado(pedido);
        validarIntentos(pedido);

        Instant fechaActual = reloj.instant();

        validarExpiracion(pedido, fechaActual);

        if (!protectorPinPuerto.coincide(
                pedido.getId(),
                pin,
                pedido.getPinHash()
        )) {
            registrarIntentoFallido(pedido, fechaActual);

            throw crearExcepcion(
                    CodigosError.PIN_INVALIDO,
                    MensajesError.PIN_INVALIDO,
                    CategoriaError.VALIDACION
            );
        }

        pedido.setEstado(EstadoPedido.ENTREGADO);
        pedido.setPinHash(null);
        pedido.setPinGeneradoEn(null);
        pedido.setPinExpiraEn(null);
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
            throw crearExcepcion(
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
            throw crearExcepcion(
                    CodigosError
                            .PEDIDO_NO_PERTENECE_RESTAURANTE,
                    MensajesError
                            .PEDIDO_NO_PERTENECE_RESTAURANTE,
                    CategoriaError.ACCESO_DENEGADO
            );
        }
    }

    private void validarEmpleadoAsignado(
            Pedido pedido,
            Long empleadoId) {

        if (!empleadoId.equals(
                pedido.getEmpleadoAsignadoId()
        )) {
            throw crearExcepcion(
                    CodigosError
                            .EMPLEADO_NO_ASIGNADO_AL_PEDIDO,
                    MensajesError
                            .EMPLEADO_NO_ASIGNADO_AL_PEDIDO,
                    CategoriaError.ACCESO_DENEGADO
            );
        }
    }

    private void validarEstado(Pedido pedido) {
        if (!EstadoPedido.LISTO.equals(
                pedido.getEstado()
        )) {
            throw crearExcepcion(
                    CodigosError.PEDIDO_NO_LISTO,
                    MensajesError.PEDIDO_NO_LISTO,
                    CategoriaError.CONFLICTO
            );
        }
    }

    private void validarPinGenerado(Pedido pedido) {
        if (pedido.getPinHash() == null
                || pedido.getPinHash().isBlank()
                || pedido.getPinExpiraEn() == null) {

            throw crearExcepcion(
                    CodigosError.PIN_NO_GENERADO,
                    MensajesError.PIN_NO_GENERADO,
                    CategoriaError.CONFLICTO
            );
        }
    }

    private void validarIntentos(Pedido pedido) {
        int intentos =
                pedido.getPinIntentosFallidos() == null
                        ? 0
                        : pedido.getPinIntentosFallidos();

        if (intentos
                >= configuracionPin
                .getMaximoIntentos()) {

            throw crearExcepcion(
                    CodigosError
                            .PIN_INTENTOS_AGOTADOS,
                    MensajesError
                            .PIN_INTENTOS_AGOTADOS,
                    CategoriaError.CONFLICTO
            );
        }
    }

    private void validarExpiracion(
            Pedido pedido,
            Instant fechaActual) {

        if (!fechaActual.isBefore(
                pedido.getPinExpiraEn()
        )) {
            throw crearExcepcion(
                    CodigosError.PIN_EXPIRADO,
                    MensajesError.PIN_EXPIRADO,
                    CategoriaError.CONFLICTO
            );
        }
    }

    private void registrarIntentoFallido(
            Pedido pedido,
            Instant fechaActual) {

        int intentosActuales =
                pedido.getPinIntentosFallidos() == null
                        ? 0
                        : pedido.getPinIntentosFallidos();

        pedido.setPinIntentosFallidos(
                intentosActuales + 1
        );

        pedido.setFechaActualizacion(fechaActual);

        pedidoPersistenciaPuerto.guardar(pedido);
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
                        EstadoPedido.LISTO,
                        EstadoPedido.ENTREGADO
                );

        EventoPendiente pendiente =
                new EventoPendiente();

        pendiente.setTipoEvento(
                TipoEvento.PEDIDO_ENTREGADO
        );
        pendiente.setAgregadoId(pedido.getId());
        pendiente.setCargaJson(
                eventoSerializadorPuerto
                        .serializar(evento)
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

    private ExcepcionPlazoleta crearExcepcion(
            String codigo,
            String mensaje,
            CategoriaError categoria) {

        return new ExcepcionPlazoleta(
                codigo,
                mensaje,
                categoria
        );
    }
}