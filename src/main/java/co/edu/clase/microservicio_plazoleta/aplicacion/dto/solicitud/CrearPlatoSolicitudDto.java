package co.edu.clase.microservicio_plazoleta.aplicacion.dto.solicitud;

import co.edu.clase.microservicio_plazoleta.util.constantes.ConstantesValidacion;
import co.edu.clase.microservicio_plazoleta.util.constantes.MensajesError;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public class CrearPlatoSolicitudDto {

    @NotBlank(message = MensajesError.NOMBRE_PLATO_OBLIGATORIO)
    @Size(
            max = ConstantesValidacion.LONGITUD_MAXIMA_NOMBRE_PLATO,
            message = MensajesError.NOMBRE_PLATO_LONGITUD_INVALIDA
    )
    private String nombre;

    @NotBlank(message = MensajesError.DESCRIPCION_PLATO_OBLIGATORIA)
    @Size(
            max = ConstantesValidacion
                    .LONGITUD_MAXIMA_DESCRIPCION_PLATO,
            message = MensajesError
                    .DESCRIPCION_PLATO_LONGITUD_INVALIDA
    )
    private String descripcion;

    @NotNull(message = MensajesError.PRECIO_PLATO_OBLIGATORIO)
    @Positive(message = MensajesError.PRECIO_PLATO_INVALIDO)
    private Integer precio;

    @NotBlank(message = MensajesError.URL_IMAGEN_OBLIGATORIA)
    @Size(
            max = ConstantesValidacion.LONGITUD_MAXIMA_URL_IMAGEN,
            message = MensajesError.URL_IMAGEN_INVALIDA
    )
    @URL(message = MensajesError.URL_IMAGEN_INVALIDA)
    private String urlImagen;

    @NotNull(message = MensajesError.CATEGORIA_OBLIGATORIA)
    @Positive(message = MensajesError.CATEGORIA_OBLIGATORIA)
    private Long categoriaId;

    @NotNull(message = MensajesError.RESTAURANTE_OBLIGATORIO)
    @Positive(message = MensajesError.RESTAURANTE_OBLIGATORIO)
    private Long restauranteId;

    public CrearPlatoSolicitudDto() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public Long getRestauranteId() {
        return restauranteId;
    }

    public void setRestauranteId(Long restauranteId) {
        this.restauranteId = restauranteId;
    }
}