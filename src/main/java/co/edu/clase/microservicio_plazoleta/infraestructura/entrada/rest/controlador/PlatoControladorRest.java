package co.edu.clase.microservicio_plazoleta.infraestructura.entrada.rest.controlador;

import co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta.PlatoRespuestaDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.dto.solicitud.CrearPlatoSolicitudDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.handler.CrearPlatoHandler;
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
        name = "Platos",
        description = "Operaciones para gestionar platos"
)
@RestController
@RequestMapping(ConstantesApi.API_PLATOS)
public class PlatoControladorRest {

    private final CrearPlatoHandler crearPlatoHandler;

    public PlatoControladorRest(
            CrearPlatoHandler crearPlatoHandler) {

        this.crearPlatoHandler = crearPlatoHandler;
    }

    @Operation(
            summary = "Crear plato",
            description =
                    "Crea un plato en un restaurante perteneciente "
                            + "al propietario autenticado"
    )
    @SecurityRequirement(name = "autenticacionBearer")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Plato creado correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos, precio o restaurante inválido"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente, inválido o expirado"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description =
                            "Perfil sin autorización o restaurante ajeno"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description =
                            "Restaurante o categoría no encontrado"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del microservicio"
            )
    })
    @PostMapping
    public ResponseEntity<PlatoRespuestaDto> crearPlato(
            @Valid @RequestBody
            CrearPlatoSolicitudDto solicitud,
            @AuthenticationPrincipal
            UsuarioAutenticado usuarioAutenticado) {

        PlatoRespuestaDto respuesta =
                crearPlatoHandler.ejecutar(
                        solicitud,
                        usuarioAutenticado.getUsuarioId()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(respuesta);
    }
}