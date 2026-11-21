package co.edu.clase.microservicio_plazoleta.dominio.modelo;

import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoPedido;

public class EventoTrazabilidad {

    private Long pedidoId;
    private Long clienteId;
    private Long restauranteId;
    private Long empleadoId;
    private EstadoPedido estadoAnterior;
    private EstadoPedido estadoNuevo;

    public EventoTrazabilidad() {
    }

    public EventoTrazabilidad(
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

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getRestauranteId() {
        return restauranteId;
    }

    public void setRestauranteId(Long restauranteId) {
        this.restauranteId = restauranteId;
    }

    public Long getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(Long empleadoId) {
        this.empleadoId = empleadoId;
    }

    public EstadoPedido getEstadoAnterior() {
        return estadoAnterior;
    }

    public void setEstadoAnterior(
            EstadoPedido estadoAnterior) {

        this.estadoAnterior = estadoAnterior;
    }

    public EstadoPedido getEstadoNuevo() {
        return estadoNuevo;
    }

    public void setEstadoNuevo(
            EstadoPedido estadoNuevo) {

        this.estadoNuevo = estadoNuevo;
    }
}