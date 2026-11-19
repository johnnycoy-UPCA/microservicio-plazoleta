package co.edu.clase.microservicio_plazoleta.aplicacion.dto.solicitud;

import co.edu.clase.microservicio_plazoleta.util.constantes.ConstantesValidacion;
import co.edu.clase.microservicio_plazoleta.util.constantes.MensajesError;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class DetallePedidoSolicitudDto {

    @NotNull(
            message = MensajesError
                    .PLATO_PEDIDO_OBLIGATORIO
    )
    @Positive(
            message = MensajesError
                    .PLATO_PEDIDO_OBLIGATORIO
    )
    private Long platoId;

    @NotNull(
            message = MensajesError
                    .CANTIDAD_PEDIDO_OBLIGATORIA
    )
    @Min(
            value = ConstantesValidacion
                    .CANTIDAD_MINIMA_PLATO_PEDIDO,
            message = MensajesError
                    .CANTIDAD_PLATO_INVALIDA
    )
    @Max(
            value = ConstantesValidacion
                    .CANTIDAD_MAXIMA_PLATO_PEDIDO,
            message = MensajesError
                    .CANTIDAD_PLATO_INVALIDA
    )
    private Integer cantidad;

    public DetallePedidoSolicitudDto() {
    }

    public Long getPlatoId() {
        return platoId;
    }

    public void setPlatoId(Long platoId) {
        this.platoId = platoId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}