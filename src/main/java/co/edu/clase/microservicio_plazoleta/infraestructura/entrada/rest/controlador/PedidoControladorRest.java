package co.edu.clase.microservicio_plazoleta.infraestructura.entrada.rest.controlador;

import co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta.PaginaRespuestaDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta.PedidoRespuestaDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.dto.solicitud.RealizarPedidoSolicitudDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.handler.ListarPedidosHandler;
import co.edu.clase.microservicio_plazoleta.aplicacion.handler.RealizarPedidoHandler;
import co.edu.clase.microservicio_plazoleta.infraestructura.entrada.rest.seguridad.UsuarioAutenticado;
import co.edu.clase.microservicio_plazoleta.util.constantes.ConstantesApi;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoPedido;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Pedidos",
        description = "Operaciones para gestionar pedidos"
)
@RestController
@RequestMapping(ConstantesApi.API_PEDIDOS)
public class PedidoControladorRest {

    private final RealizarPedidoHandler realizarPedidoHandler;
    private final ListarPedidosHandler listarPedidosHandler;

    public PedidoControladorRest(
            RealizarPedidoHandler realizarPedidoHandler,
			ListarPedidosHandler listarPedidosHandler) {

        this.realizarPedidoHandler = realizarPedidoHandler;
		this.listarPedidosHandler = listarPedidosHandler;
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
			description = "Solicitud, plato o cantidad inválida"
		),
		@ApiResponse(
			responseCode = "401",
			description = "Token ausente, inválido o expirado"
		),
		@ApiResponse(
			responseCode = "403",
			description = "El usuario no tiene perfil CLIENTE"
		),
		@ApiResponse(
			responseCode = "404",
			description = "Restaurante no encontrado"
		),
		@ApiResponse(
			responseCode = "409",
			description = "El cliente ya tiene un pedido activo"
		),
		@ApiResponse(
			responseCode = "500",
			description = "Error interno del microservicio"
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

	@Operation(
			summary = "Listar pedidos por estado",
			description =
				"Lista de forma paginada los pedidos "
				+ "del restaurante asociado al empleado "
				+ "autenticado, filtrados por estado"
	)
	@SecurityRequirement(name = "autenticacionBearer")
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "Pedidos consultados correctamente"
		),
		@ApiResponse(
			responseCode = "400",
			description = "Estado o paginación inválidos"
		),
		@ApiResponse(
			responseCode = "401",
			description = "Token ausente, inválido o expirado"
		),
		@ApiResponse(
			responseCode = "403",
			description = "El usuario no tiene perfil EMPLEADO"
		),
		@ApiResponse(
			responseCode = "502",
			description = "Microservicio Usuarios no disponible"
		)
	})
	@GetMapping
	public ResponseEntity<
			PaginaRespuestaDto<PedidoRespuestaDto>>
			listarPedidos(
					@RequestParam
					EstadoPedido estado,
					@RequestParam(
							defaultValue = "0"
					)
					int pagina,
					@RequestParam(
							defaultValue = "10"
					)
					int tamano,
					@AuthenticationPrincipal
					UsuarioAutenticado usuarioAutenticado) {

		return ResponseEntity.ok(
				listarPedidosHandler.ejecutar(
						usuarioAutenticado.getUsuarioId(),
						usuarioAutenticado.getToken(),
						estado,
						pagina,
						tamano
				)
		);
	}
}