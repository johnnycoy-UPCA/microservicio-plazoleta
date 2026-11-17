package co.edu.clase.microservicio_plazoleta.aplicacion.dto.solicitud;

import co.edu.clase.microservicio_plazoleta.util.constantes.MensajesError;
import jakarta.validation.constraints.NotNull;

public class CambiarEstadoPlatoSolicitudDto {

    @NotNull(message = MensajesError.ESTADO_PLATO_OBLIGATORIO)
    private Boolean activo;

    public CambiarEstadoPlatoSolicitudDto() {
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}