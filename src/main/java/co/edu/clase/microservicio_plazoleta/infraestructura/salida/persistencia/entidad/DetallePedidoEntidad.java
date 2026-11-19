package co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.entidad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pedidos_platos")
public class DetallePedidoEntidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "pedido_id",
            nullable = false
    )
    private PedidoEntidad pedido;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "plato_id",
            nullable = false
    )
    private PlatoEntidad plato;

    @Column(
            name = "nombre_plato",
            nullable = false,
            length = 100
    )
    private String nombrePlato;

    @Column(
            name = "precio_unitario",
            nullable = false
    )
    private Integer precioUnitario;

    @Column(
            name = "cantidad",
            nullable = false
    )
    private Integer cantidad;

    public DetallePedidoEntidad() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PedidoEntidad getPedido() {
        return pedido;
    }

    public void setPedido(PedidoEntidad pedido) {
        this.pedido = pedido;
    }

    public PlatoEntidad getPlato() {
        return plato;
    }

    public void setPlato(PlatoEntidad plato) {
        this.plato = plato;
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