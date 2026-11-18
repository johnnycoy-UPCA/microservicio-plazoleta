package co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta;

public class RestauranteValidacionRespuestaDto {

    private Long id;
    private Long propietarioId;
    private Boolean activo;

    public RestauranteValidacionRespuestaDto() {
    }

    public RestauranteValidacionRespuestaDto(
            Long id,
            Long propietarioId,
            Boolean activo) {

        this.id = id;
        this.propietarioId = propietarioId;
        this.activo = activo;
    }

    public Long getId() {
        return id;
    }

    public Long getPropietarioId() {
        return propietarioId;
    }

    public Boolean getActivo() {
        return activo;
    }
}