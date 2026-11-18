package co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta;

public class RestauranteResumenDto {

    private Long id;
    private String nombre;
    private String urlLogo;

    public RestauranteResumenDto() {
    }

    public RestauranteResumenDto(
            Long id,
            String nombre,
            String urlLogo) {

        this.id = id;
        this.nombre = nombre;
        this.urlLogo = urlLogo;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUrlLogo() {
        return urlLogo;
    }
}