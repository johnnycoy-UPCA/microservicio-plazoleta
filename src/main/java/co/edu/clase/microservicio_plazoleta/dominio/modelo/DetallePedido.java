package co.edu.clase.microservicio_plazoleta.dominio.modelo;

public class DetallePedido {

    private Long id;
    private Long platoId;
    private String nombrePlato;
    private Integer precioUnitario;
    private Integer cantidad;

    public DetallePedido() {
    }

    public DetallePedido(
            Long id,
            Long platoId,
            String nombrePlato,
            Integer precioUnitario,
            Integer cantidad) {

        this.id = id;
        this.platoId = platoId;
        this.nombrePlato = nombrePlato;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}