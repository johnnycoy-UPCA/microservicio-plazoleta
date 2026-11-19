package co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta;

public class DetallePedidoRespuestaDto {

    private Long platoId;
    private String nombrePlato;
    private Integer precioUnitario;
    private Integer cantidad;
    private Long subtotal;

    public DetallePedidoRespuestaDto() {
    }

    public DetallePedidoRespuestaDto(
            Long platoId,
            String nombrePlato,
            Integer precioUnitario,
            Integer cantidad,
            Long subtotal) {

        this.platoId = platoId;
        this.nombrePlato = nombrePlato;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public Long getPlatoId() {
        return platoId;
    }

    public void setPlatoId(Long platoId) {
        this.platoId = platoId;
    }

    public String getNombrePlato() {
        return nombrePlato;
    }

    public void setNombrePlato(String nombrePlato) {
        this.nombrePlato = nombrePlato;
    }

    public Integer getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(
            Integer precioUnitario) {

        this.precioUnitario = precioUnitario;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Long getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Long subtotal) {
        this.subtotal = subtotal;
    }
}