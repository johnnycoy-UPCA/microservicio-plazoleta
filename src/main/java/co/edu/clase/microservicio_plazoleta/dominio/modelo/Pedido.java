package co.edu.clase.microservicio_plazoleta.dominio.modelo;

import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoPedido;

import java.time.Instant;
import java.util.List;

public class Pedido {

    private Long id;
    private Long clienteId;
    private Long restauranteId;
    private Long empleadoAsignadoId;
    private EstadoPedido estado;
    private Long total;
    private List<DetallePedido> detalles;
    private String pinHash;
    private Instant pinGeneradoEn;
    private Instant pinExpiraEn;
    private Integer pinIntentosFallidos;
    private Instant fechaCreacion;
    private Instant fechaActualizacion;
    private Long version;

    public Pedido() {
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

    public Long getEmpleadoAsignadoId() {
        return empleadoAsignadoId;
    }

    public void setEmpleadoAsignadoId(
            Long empleadoAsignadoId) {

        this.empleadoAsignadoId = empleadoAsignadoId;
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

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(
            List<DetallePedido> detalles) {

        this.detalles = detalles;
    }

    public String getPinHash() {
        return pinHash;
    }

    public void setPinHash(String pinHash) {
        this.pinHash = pinHash;
    }

    public Instant getPinGeneradoEn() {
        return pinGeneradoEn;
    }

    public void setPinGeneradoEn(
            Instant pinGeneradoEn) {

        this.pinGeneradoEn = pinGeneradoEn;
    }

    public Instant getPinExpiraEn() {
        return pinExpiraEn;
    }

    public void setPinExpiraEn(Instant pinExpiraEn) {
        this.pinExpiraEn = pinExpiraEn;
    }

    public Integer getPinIntentosFallidos() {
        return pinIntentosFallidos;
    }

    public void setPinIntentosFallidos(
            Integer pinIntentosFallidos) {

        this.pinIntentosFallidos =
                pinIntentosFallidos;
    }

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(
            Instant fechaCreacion) {

        this.fechaCreacion = fechaCreacion;
    }

    public Instant getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(
            Instant fechaActualizacion) {

        this.fechaActualizacion =
                fechaActualizacion;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}