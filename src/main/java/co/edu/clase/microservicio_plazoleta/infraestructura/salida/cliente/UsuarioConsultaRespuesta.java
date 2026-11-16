package co.edu.clase.microservicio_plazoleta.infraestructura.salida.cliente;

import co.edu.clase.microservicio_plazoleta.util.enumeraciones.PerfilUsuario;

public class UsuarioConsultaRespuesta {

    private Long id;
    private PerfilUsuario perfil;
    private Boolean activo;

    public UsuarioConsultaRespuesta() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PerfilUsuario getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilUsuario perfil) {
        this.perfil = perfil;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}