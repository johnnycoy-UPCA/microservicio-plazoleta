package co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.entidad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "restaurantes")
public class RestauranteEntidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "nombre",
            nullable = false,
            length = 100
    )
    private String nombre;

    @Column(
            name = "nit",
            nullable = false,
            unique = true,
            length = 20
    )
    private String nit;

    @Column(
            name = "direccion",
            nullable = false,
            length = 150
    )
    private String direccion;

    @Column(
            name = "celular",
            nullable = false,
            length = 13
    )
    private String celular;

    @Column(
            name = "url_logo",
            nullable = false,
            length = 500
    )
    private String urlLogo;

    @Column(
            name = "propietario_id",
            nullable = false
    )
    private Long propietarioId;

    @Column(
            name = "activo",
            nullable = false
    )
    private Boolean activo;

    @Column(
            name = "fecha_creacion",
            nullable = false
    )
    private LocalDateTime fechaCreacion;

    @Column(
            name = "fecha_actualizacion",
            nullable = false
    )
    private LocalDateTime fechaActualizacion;

    public RestauranteEntidad() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    public Long getPropietarioId() {
        return propietarioId;
    }

    public void setPropietarioId(Long propietarioId) {
        this.propietarioId = propietarioId;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(
            LocalDateTime fechaCreacion) {

        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(
            LocalDateTime fechaActualizacion) {

        this.fechaActualizacion = fechaActualizacion;
    }
}