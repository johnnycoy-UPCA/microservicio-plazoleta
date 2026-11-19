package co.edu.clase.microservicio_plazoleta.infraestructura.entrada.rest.controlador;

import co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta.PedidoRespuestaDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.dto.solicitud.RealizarPedidoSolicitudDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.handler.RealizarPedidoHandler;
import co.edu.clase.microservicio_plazoleta.infraestructura.entrada.rest.seguridad.UsuarioAutenticado;
import co.edu.clase.microservicio_plazoleta.util.constantes.ConstantesApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Pedidos",
        description = "Operaciones para gestionar pedidos"
)
@RestController
@RequestMapping(ConstantesApi.API_PEDIDOS)
public class PedidoControladorRest {

    private final RealizarPedidoHandler
            realizarPedidoHandler;

    public PedidoControladorRest(
            RealizarPedidoHandler
                    realizarPedidoHandler) {

        this.realizarPedidoHandler =
                realizarPedidoHandler;
    }

    @Operation(
            summary = "Realizar pedido",
            description =
                    "Crea un pedido para el cliente autenticado "
                            + "con platos activos de un único restaurante"
    )
    @SecurityRequirement(name = "autenticacionBearer")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Pedido creado correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description =
                            "Solicitud, plato o cantidad inválida"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description =
                            "Token ausente, inválido o expirado"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description =
                            "El usuario no tiene perfil CLIENTE"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Restaurante no encontrado"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description =
                            "El cliente ya tiene un pedido activo"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description =
                            "Error interno del microservicio"
            )
    })
    @PostMapping
    public ResponseEntity<PedidoRespuestaDto>
            realizarPedido(
                    @Valid @RequestBody
                    RealizarPedidoSolicitudDto solicitud,
                    @AuthenticationPrincipal
                    UsuarioAutenticado usuarioAutenticado) {

        PedidoRespuestaDto respuesta =
                realizarPedidoHandler.ejecutar(
                        solicitud,
                        usuarioAutenticado.getUsuarioId()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(respuesta);
    }
}