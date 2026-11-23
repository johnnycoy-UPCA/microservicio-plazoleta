package co.edu.clase.microservicio_plazoleta.aplicacion.dto.solicitud;

import co.edu.clase.microservicio_plazoleta.util.constantes.MensajesError;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class EntregarPedidoSolicitudDto {

    @NotBlank(
            message = MensajesError.PIN_OBLIGATORIO
    )
    @Pattern(
            regexp = "\\d{6}",
            message = MensajesError.PIN_FORMATO_INVALIDO
    )
    private String pin;

    public EntregarPedidoSolicitudDto() {
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}