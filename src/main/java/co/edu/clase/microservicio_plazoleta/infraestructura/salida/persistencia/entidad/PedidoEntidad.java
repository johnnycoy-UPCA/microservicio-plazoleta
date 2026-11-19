package co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.entidad;

import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoPedido;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class PedidoEntidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "cliente_id",
            nullable = false
    )
    private Long clienteId;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "restaurante_id",
            nullable = false
    )
    private RestauranteEntidad restaurante;

    @Column(name = "empleado_asignado_id")
    private Long empleadoAsignadoId;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "estado",
            nullable = false,
            length = 30
    )
    private EstadoPedido estado;

    @Column(
            name = "total",
            nullable = false
    )
    private Long total;

    @Column(name = "pin_hash", length = 255)
    private String pinHash;

    @Column(name = "pin_generado_en")
    private Instant pinGeneradoEn;

    @Column(name = "pin_expira_en")
    private Instant pinExpiraEn;

    @Column(
            name = "pin_intentos_fallidos",
            nullable = false
    )
    private Integer pinIntentosFallidos;

    @Column(
            name = "fecha_creacion",
            nullable = false
    )
    private Instant fechaCreacion;

    @Column(
            name = "fecha_actualizacion",
            nullable = false
    )
    private Instant fechaActualizacion;

    @Version
    @Column(
            name = "version",
            nullable = false
    )
    private Long version;

    @OneToMany(
            mappedBy = "pedido",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DetallePedidoEntidad> detalles =
            new ArrayList<>();

    public PedidoEntidad() {
    }

    public void agregarDetalle(
            DetallePedidoEntidad detalle) {

        detalles.add(detalle);
        detalle.setPedido(this);
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

    public RestauranteEntidad getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(
            RestauranteEntidad restaurante) {

        this.restaurante = restaurante;
    }

    public Long getEmpleadoAsignadoId() {
        return empleadoAsignadoId;
    }

    public void setEmpleadoAsignadoId(
            Long empleadoAsignadoId) {

        this.empleadoAsignadoId =
                empleadoAsignadoId;
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

    public void setPinExpiraEn(
            Instant pinExpiraEn) {

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

    public List<DetallePedidoEntidad> getDetalles() {
        return detalles;
    }

    public void setDetalles(
            List<DetallePedidoEntidad> detalles) {

        this.detalles = detalles;
    }
}