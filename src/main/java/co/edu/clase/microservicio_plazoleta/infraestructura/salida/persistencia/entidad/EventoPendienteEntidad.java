package co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.entidad;

import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoEnvioEvento;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.TipoEvento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.time.Instant;

@Entity
@Table(name = "eventos_pendientes")
public class EventoPendienteEntidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "tipo_evento",
            nullable = false,
            length = 80
    )
    private TipoEvento tipoEvento;

    @Column(
            name = "agregado_id",
            nullable = false
    )
    private Long agregadoId;

    @Lob
    @Column(
            name = "carga_json",
            nullable = false
    )
    private String cargaJson;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "estado_envio",
            nullable = false,
            length = 20
    )
    private EstadoEnvioEvento estadoEnvio;

    @Column(
            name = "intentos",
            nullable = false
    )
    private Integer intentos;

    @Column(
            name = "proximo_intento",
            nullable = false
    )
    private Instant proximoIntento;

    @Column(
            name = "fecha_creacion",
            nullable = false
    )
    private Instant fechaCreacion;

    @Column(name = "fecha_envio")
    private Instant fechaEnvio;

    @Column(
            name = "ultimo_error",
            length = 500
    )
    private String ultimoError;

    @Version
    @Column(
            name = "version",
            nullable = false
    )
    private Long version;

    public EventoPendienteEntidad() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(
            TipoEvento tipoEvento) {

        this.tipoEvento = tipoEvento;
    }

    public Long getAgregadoId() {
        return agregadoId;
    }

    public void setAgregadoId(Long agregadoId) {
        this.agregadoId = agregadoId;
    }

    public String getCargaJson() {
        return cargaJson;
    }

    public void setCargaJson(String cargaJson) {
        this.cargaJson = cargaJson;
    }

    public EstadoEnvioEvento getEstadoEnvio() {
        return estadoEnvio;
    }

    public void setEstadoEnvio(
            EstadoEnvioEvento estadoEnvio) {

        this.estadoEnvio = estadoEnvio;
    }

    public Integer getIntentos() {
        return intentos;
    }

    public void setIntentos(Integer intentos) {
        this.intentos = intentos;
    }

    public Instant getProximoIntento() {
        return proximoIntento;
    }

    public void setProximoIntento(
            Instant proximoIntento) {

        this.proximoIntento = proximoIntento;
    }

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(
            Instant fechaCreacion) {

        this.fechaCreacion = fechaCreacion;
    }

    public Instant getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Instant fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getUltimoError() {
        return ultimoError;
    }

    public void setUltimoError(String ultimoError) {
        this.ultimoError = ultimoError;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}