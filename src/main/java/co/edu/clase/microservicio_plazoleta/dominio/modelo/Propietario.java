package co.edu.clase.microservicio_plazoleta.dominio.modelo;

import co.edu.clase.microservicio_plazoleta.util.enumeraciones.PerfilUsuario;

public class Propietario {

    private Long id;
    private PerfilUsuario perfil;
    private Boolean activo;

    public Propietario() {
    }

    public Propietario(
            Long id,
            PerfilUsuario perfil,
            Boolean activo) {

        this.id = id;
        this.perfil = perfil;
        this.activo = activo;
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