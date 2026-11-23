package co.edu.clase.microservicio_plazoleta.infraestructura.salida.cliente;

public class ContactoClienteRespuesta {

    private Long usuarioId;
    private String celular;
    private Boolean activo;

    public ContactoClienteRespuesta() {
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}