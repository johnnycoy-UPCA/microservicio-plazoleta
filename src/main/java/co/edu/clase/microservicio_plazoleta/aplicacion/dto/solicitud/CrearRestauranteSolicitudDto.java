package co.edu.clase.microservicio_plazoleta.aplicacion.dto.solicitud;

import co.edu.clase.microservicio_plazoleta.util.constantes.ConstantesValidacion;
import co.edu.clase.microservicio_plazoleta.util.constantes.MensajesError;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public class CrearRestauranteSolicitudDto {

    @NotBlank(message = MensajesError.NOMBRE_OBLIGATORIO)
    @Size(
            max = ConstantesValidacion.LONGITUD_MAXIMA_NOMBRE_RESTAURANTE,
            message = MensajesError.NOMBRE_LONGITUD_INVALIDA
    )
    private String nombre;

    @NotBlank(message = MensajesError.NIT_OBLIGATORIO)
    @Size(
            max = ConstantesValidacion.LONGITUD_MAXIMA_NIT,
            message = MensajesError.NIT_LONGITUD_INVALIDA
    )
    @Pattern(
            regexp = ConstantesValidacion.EXPRESION_NIT,
            message = MensajesError.NIT_FORMATO_INVALIDO
    )
    private String nit;

    @NotBlank(message = MensajesError.DIRECCION_OBLIGATORIA)
    @Size(
            max = ConstantesValidacion.LONGITUD_MAXIMA_DIRECCION,
            message = MensajesError.DIRECCION_LONGITUD_INVALIDA
    )
    private String direccion;

    @NotBlank(message = MensajesError.CELULAR_OBLIGATORIO)
    @Pattern(
            regexp = ConstantesValidacion.EXPRESION_CELULAR,
            message = MensajesError.CELULAR_FORMATO_INVALIDO
    )
    private String celular;

    @NotBlank(message = MensajesError.URL_LOGO_OBLIGATORIA)
    @Size(
            max = ConstantesValidacion.LONGITUD_MAXIMA_URL_LOGO,
            message = MensajesError.URL_LOGO_INVALIDA
    )
    @URL(message = MensajesError.URL_LOGO_INVALIDA)
    private String urlLogo;

    @NotNull(message = MensajesError.PROPIETARIO_OBLIGATORIO)
    @Positive(message = MensajesError.PROPIETARIO_OBLIGATORIO)
    private Long propietarioId;

    public CrearRestauranteSolicitudDto() {
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
}