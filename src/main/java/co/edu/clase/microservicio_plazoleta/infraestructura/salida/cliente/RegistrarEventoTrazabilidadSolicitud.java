package co.edu.clase.microservicio_plazoleta.infraestructura.salida.cliente;

import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoPedido;

public class RegistrarEventoTrazabilidadSolicitud {

    private Long pedidoId;
    private Long clienteId;
    private Long restauranteId;
    private Long empleadoId;
    private EstadoPedido estadoAnterior;
    private EstadoPedido estadoNuevo;

    public RegistrarEventoTrazabilidadSolicitud() {
    }

    public RegistrarEventoTrazabilidadSolicitud(
            Long pedidoId,
            Long clienteId,
            Long restauranteId,
            Long empleadoId,
            EstadoPedido estadoAnterior,
            EstadoPedido estadoNuevo) {

        this.pedidoId = pedidoId;
        this.clienteId = clienteId;
        this.restauranteId = restauranteId;
        this.empleadoId = empleadoId;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public Long getRestauranteId() {
        return restauranteId;
    }

    public Long getEmpleadoId() {
        return empleadoId;
    }

    public EstadoPedido getEstadoAnterior() {
        return estadoAnterior;
    }

    public EstadoPedido getEstadoNuevo() {
        return estadoNuevo;
    }
}