package co.edu.clase.microservicio_plazoleta.dominio.casouso;

import co.edu.clase.microservicio_plazoleta.dominio.api.RealizarPedidoServicioPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.excepcion.ExcepcionPlazoleta;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.DetallePedido;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pedido;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Plato;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Restaurante;
import co.edu.clase.microservicio_plazoleta.dominio.spi.PedidoPersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.PlatoPersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.RestaurantePersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.util.constantes.CodigosError;
import co.edu.clase.microservicio_plazoleta.util.constantes.ConstantesValidacion;
import co.edu.clase.microservicio_plazoleta.util.constantes.MensajesError;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.CategoriaError;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoPedido;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RealizarPedidoCasoUso
        implements RealizarPedidoServicioPuerto {

    private final PedidoPersistenciaPuerto
            pedidoPersistenciaPuerto;

    private final PlatoPersistenciaPuerto
            platoPersistenciaPuerto;

    private final RestaurantePersistenciaPuerto
            restaurantePersistenciaPuerto;

    private final Clock reloj;

    public RealizarPedidoCasoUso(
            PedidoPersistenciaPuerto
                    pedidoPersistenciaPuerto,
            PlatoPersistenciaPuerto
                    platoPersistenciaPuerto,
            RestaurantePersistenciaPuerto
                    restaurantePersistenciaPuerto,
            Clock reloj) {

        this.pedidoPersistenciaPuerto =
                pedidoPersistenciaPuerto;
        this.platoPersistenciaPuerto =
                platoPersistenciaPuerto;
        this.restaurantePersistenciaPuerto =
                restaurantePersistenciaPuerto;
        this.reloj = reloj;
    }

    @Override
    public Pedido realizar(Pedido pedido) {
        validarDetalles(pedido.getDetalles());
        validarPedidoActivo(pedido.getClienteId());
        validarRestaurante(pedido.getRestauranteId());

        // List<Long> platoIds = pedido.getDetalles()
        //         .stream()
        //         .map(DetallePedido::getPlatoId)
        //         .toList();

        List<Long> platoIds = obtenerPlatoIds(
                pedido.getDetalles()
        );

        List<Plato> platos =
                platoPersistenciaPuerto
                        .buscarActivosPorIds(platoIds);

        Map<Long, Plato> platosPorId =
                convertirAMapa(platos);

        completarYValidarDetalles(
                pedido,
                platosPorId
        );

        Instant fechaActual = reloj.instant();

        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setEmpleadoAsignadoId(null);
        pedido.setPinHash(null);
        pedido.setPinGeneradoEn(null);
        pedido.setPinExpiraEn(null);
        pedido.setPinIntentosFallidos(0);
        pedido.setFechaCreacion(fechaActual);
        pedido.setFechaActualizacion(fechaActual);
        pedido.setVersion(0L);

        return pedidoPersistenciaPuerto.guardar(pedido);
    }

    private void validarDetalles(
            List<DetallePedido> detalles) {

        if (detalles == null || detalles.isEmpty()) {
            throw crearExcepcion(
                    CodigosError.PEDIDO_VACIO,
                    MensajesError.PEDIDO_VACIO
            );
        }

        if (detalles.size()
                > ConstantesValidacion
                .MAXIMO_LINEAS_PEDIDO) {

            throw crearExcepcion(
                    CodigosError
                            .LIMITE_LINEAS_PEDIDO_EXCEDIDO,
                    MensajesError
                            .LIMITE_LINEAS_PEDIDO_EXCEDIDO
            );
        }

        Set<Long> identificadores = new HashSet<>();

        // for (DetallePedido detalle : detalles) {
        //     validarCantidad(detalle.getCantidad());

        //     if (detalle.getPlatoId() == null
        //             || !identificadores.add(
        //             detalle.getPlatoId()
        //     )) {
        //         throw crearExcepcion(
        //                 CodigosError.PLATO_REPETIDO,
        //                 MensajesError.PLATO_REPETIDO
        //         );
        //     }
        // }

        for (DetallePedido detalle : detalles) {
            if (detalle == null
                    || detalle.getPlatoId() == null) {

                throw crearExcepcion(
                        CodigosError.PLATO_PEDIDO_INVALIDO,
                        MensajesError.PLATO_PEDIDO_INVALIDO
                );
            }

            validarCantidad(detalle.getCantidad());

            if (!identificadores.add(
                    detalle.getPlatoId()
            )) {
                throw crearExcepcion(
                        CodigosError.PLATO_REPETIDO,
                        MensajesError.PLATO_REPETIDO
                );
            }
        }
    }

    private void validarCantidad(Integer cantidad) {
        if (cantidad == null
                || cantidad
                < ConstantesValidacion
                .CANTIDAD_MINIMA_PLATO_PEDIDO
                || cantidad
                > ConstantesValidacion
                .CANTIDAD_MAXIMA_PLATO_PEDIDO) {

            throw crearExcepcion(
                    CodigosError.CANTIDAD_PLATO_INVALIDA,
                    MensajesError.CANTIDAD_PLATO_INVALIDA
            );
        }
    }

    private void validarPedidoActivo(Long clienteId) {
        if (pedidoPersistenciaPuerto
                .existePedidoActivoPorCliente(
                        clienteId
                )) {

            throw new ExcepcionPlazoleta(
                    CodigosError
                            .PEDIDO_ACTIVO_EXISTENTE,
                    MensajesError
                            .PEDIDO_ACTIVO_EXISTENTE,
                    CategoriaError.CONFLICTO
            );
        }
    }

    private void validarRestaurante(
            Long restauranteId) {

        Restaurante restaurante =
                restaurantePersistenciaPuerto
                        .buscarPorId(restauranteId)
                        .orElseThrow(() ->
                                crearExcepcionNoEncontrado(
                                        CodigosError
                                                .RESTAURANTE_NO_ENCONTRADO,
                                        MensajesError
                                                .RESTAURANTE_NO_ENCONTRADO
                                )
                        );

        if (!Boolean.TRUE.equals(
                restaurante.getActivo()
        )) {
            throw crearExcepcion(
                    CodigosError.RESTAURANTE_INACTIVO,
                    MensajesError.RESTAURANTE_INACTIVO
            );
        }
    }

    private Map<Long, Plato> convertirAMapa(
            List<Plato> platos) {

        Map<Long, Plato> resultado = new HashMap<>();

        for (Plato plato : platos) {
            resultado.put(plato.getId(), plato);
        }

        return resultado;
    }

    private void completarYValidarDetalles(
            Pedido pedido,
            Map<Long, Plato> platosPorId) {

        long total = 0L;

        for (DetallePedido detalle :
                pedido.getDetalles()) {

            Plato plato = platosPorId.get(
                    detalle.getPlatoId()
            );

            if (plato == null
                    || !pedido.getRestauranteId()
                    .equals(
                            plato.getRestauranteId()
                    )
                    || !Boolean.TRUE.equals(
                    plato.getActivo()
            )) {
                throw crearExcepcion(
                        CodigosError
                                .PLATO_PEDIDO_INVALIDO,
                        MensajesError
                                .PLATO_PEDIDO_INVALIDO
                );
            }

            detalle.setNombrePlato(
                    plato.getNombre()
            );
            detalle.setPrecioUnitario(
                    plato.getPrecio()
            );

            total += (long) plato.getPrecio()
                    * detalle.getCantidad();
        }

        pedido.setTotal(total);
    }

    private ExcepcionPlazoleta crearExcepcion(
            String codigo,
            String mensaje) {

        return new ExcepcionPlazoleta(
                codigo,
                mensaje,
                CategoriaError.VALIDACION
        );
    }

    private ExcepcionPlazoleta
            crearExcepcionNoEncontrado(
                    String codigo,
                    String mensaje) {

        return new ExcepcionPlazoleta(
                codigo,
                mensaje,
                CategoriaError.NO_ENCONTRADO
        );
    }

    private List<Long> obtenerPlatoIds(
            List<DetallePedido> detalles) {

        List<Long> platoIds = new ArrayList<>();

        for (DetallePedido detalle : detalles) {
            platoIds.add(detalle.getPlatoId());
        }

        return platoIds;
    }
}