package co.edu.clase.microservicio_plazoleta.aplicacion.dto.solicitud;

import co.edu.clase.microservicio_plazoleta.util.constantes.ConstantesValidacion;
import co.edu.clase.microservicio_plazoleta.util.constantes.MensajesError;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ModificarPlatoSolicitudDto {

    @NotNull(message = MensajesError.PRECIO_PLATO_OBLIGATORIO)
    @Positive(message = MensajesError.PRECIO_PLATO_INVALIDO)
    private Integer precio;

    @NotBlank(
            message = MensajesError
                    .DESCRIPCION_PLATO_OBLIGATORIA
    )
    @Size(
            max = ConstantesValidacion
                    .LONGITUD_MAXIMA_DESCRIPCION_PLATO,
            message = MensajesError
                    .DESCRIPCION_PLATO_LONGITUD_INVALIDA
    )
    private String descripcion;

    public ModificarPlatoSolicitudDto() {
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}