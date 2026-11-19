package co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta;

import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoPedido;

import java.time.Instant;
import java.util.List;

public class PedidoRespuestaDto {

    private Long id;
    private Long clienteId;
    private Long restauranteId;
    private EstadoPedido estado;
    private Long total;
    private List<DetallePedidoRespuestaDto> platos;
    private Instant fechaCreacion;

    public PedidoRespuestaDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<DetallePedidoRespuestaDto>
            getPlatos() {

        return platos;
    }

    public void setPlatos(
            List<DetallePedidoRespuestaDto> platos) {

        this.platos = platos;
    }

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(
            Instant fechaCreacion) {

        this.fechaCreacion = fechaCreacion;
    }
}