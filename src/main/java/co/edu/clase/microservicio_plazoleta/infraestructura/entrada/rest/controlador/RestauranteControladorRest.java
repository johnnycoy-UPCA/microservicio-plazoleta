package co.edu.clase.microservicio_plazoleta.infraestructura.entrada.rest.controlador;

import co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta.RestauranteRespuestaDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.dto.solicitud.CrearRestauranteSolicitudDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.handler.CrearRestauranteHandler;
import co.edu.clase.microservicio_plazoleta.util.constantes.ConstantesApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Restaurantes",
        description = "Operaciones para gestionar restaurantes"
)
@RestController
@RequestMapping(ConstantesApi.API_RESTAURANTES)
public class RestauranteControladorRest {

    private final CrearRestauranteHandler crearRestauranteHandler;

    public RestauranteControladorRest(
            CrearRestauranteHandler crearRestauranteHandler) {

        this.crearRestauranteHandler = crearRestauranteHandler;
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
}