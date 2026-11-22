package co.edu.clase.microservicio_plazoleta.dominio.modelo;

import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoEnvioEvento;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.TipoEvento;

import java.time.Instant;

public class EventoPendiente {

    private Long id;
    private TipoEvento tipoEvento;
    private Long agregadoId;
    private String cargaJson;
    private EstadoEnvioEvento estadoEnvio;
    private Integer intentos;
    private Instant proximoIntento;
    private Instant fechaCreacion;
    private Instant fechaEnvio;
    private String ultimoError;
    private Long version;

    public EventoPendiente() {
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

    public void setTipoEvento(TipoEvento tipoEvento) {
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