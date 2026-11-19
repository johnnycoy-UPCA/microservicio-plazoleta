package co.edu.clase.microservicio_plazoleta.aplicacion.dto.solicitud;

import co.edu.clase.microservicio_plazoleta.util.constantes.ConstantesValidacion;
import co.edu.clase.microservicio_plazoleta.util.constantes.MensajesError;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public class RealizarPedidoSolicitudDto {

    @NotNull(
            message = MensajesError
                    .RESTAURANTE_OBLIGATORIO
    )
    @Positive(
            message = MensajesError
                    .RESTAURANTE_OBLIGATORIO
    )
    private Long restauranteId;

    @NotEmpty(
            message = MensajesError
                    .DETALLES_PEDIDO_OBLIGATORIOS
    )
    @Size(
            max = ConstantesValidacion
                    .MAXIMO_LINEAS_PEDIDO,
            message = MensajesError
                    .LIMITE_LINEAS_PEDIDO_EXCEDIDO
    )
    @Valid
    private List<DetallePedidoSolicitudDto> platos;

    public RealizarPedidoSolicitudDto() {
    }

    public Long getRestauranteId() {
        return restauranteId;
    }

    public void setRestauranteId(Long restauranteId) {
        this.restauranteId = restauranteId;
    }

    public List<DetallePedidoSolicitudDto>
            getPlatos() {

        return platos;
    }

    public void setPlatos(
            List<DetallePedidoSolicitudDto> platos) {

        this.platos = platos;
    }
}