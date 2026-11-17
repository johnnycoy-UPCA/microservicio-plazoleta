package co.edu.clase.microservicio_plazoleta.infraestructura.entrada.rest.controlador;

import co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta.PaginaRespuestaDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta.RestauranteRespuestaDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta.RestauranteResumenDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.dto.solicitud.CrearRestauranteSolicitudDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.handler.CrearRestauranteHandler;
import co.edu.clase.microservicio_plazoleta.aplicacion.handler.ListarRestaurantesHandler;
import co.edu.clase.microservicio_plazoleta.util.constantes.ConstantesApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Restaurantes",
        description = "Operaciones para gestionar restaurantes"
)
@RestController
@RequestMapping(ConstantesApi.API_RESTAURANTES)
public class RestauranteControladorRest {

    private final CrearRestauranteHandler crearRestauranteHandler;
    private final ListarRestaurantesHandler listarRestaurantesHandler;

    public RestauranteControladorRest(
            CrearRestauranteHandler crearRestauranteHandler,
            ListarRestaurantesHandler listarRestaurantesHandler) {

        this.crearRestauranteHandler = crearRestauranteHandler;
        this.listarRestaurantesHandler = listarRestaurantesHandler;
    }

    @Operation(
            summary = "Crear restaurante",
            description =
                "Crea un restaurante asociado a un usuario "
                + "activo con perfil PROPIETARIO"
    )

    @SecurityRequirement (name = "autenticacionBearer")
    @ApiResponses({
            @ApiResponse(
                responseCode = "201",
                description = "Restaurante creado correctamente"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Solicitud o datos inválidos"
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Token ausente, inválido o expirado"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "El usuario autenticado no tiene perfil ADMINISTRADOR"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Propietario no encontrado"
            ),
            @ApiResponse(
                responseCode = "409",
                description = "NIT ya registrado"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Error interno del microservicio"
            ),
            @ApiResponse(
                responseCode = "502",
                description = "Microservicio Usuarios no disponible"
            )
    })
    @PostMapping
    public ResponseEntity<RestauranteRespuestaDto>
            crearRestaurante(
                @Valid @RequestBody
                CrearRestauranteSolicitudDto solicitud) {

        RestauranteRespuestaDto respuesta =
            crearRestauranteHandler.ejecutar(solicitud);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(respuesta);
    }

    @Operation(
            summary = "Listar restaurantes",
            description =
                "Lista los restaurantes activos, ordenados "
                + "alfabéticamente y de forma paginada"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Restaurantes consultados correctamente"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Parámetros de paginación inválidos"
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
            responseCode = "500",
            description = "Error interno del microservicio"
        )
    })
    @SecurityRequirement(name = "autenticacionBearer")
    @GetMapping
    public ResponseEntity<
            PaginaRespuestaDto<RestauranteResumenDto>>
            listarRestaurantes(
                    @RequestParam(
                            defaultValue = "0"
                    )
                    int pagina,
                    @RequestParam(
                            defaultValue = "10"
                    )
                    int tamano) {

        return ResponseEntity.ok(
                listarRestaurantesHandler.ejecutar(
                        pagina,
                        tamano
                )
        );
    }
}