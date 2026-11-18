package co.edu.clase.microservicio_plazoleta.infraestructura.entrada.rest.controlador;

import co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta.RestauranteRespuestaDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.dto.solicitud.CrearRestauranteSolicitudDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.handler.CrearRestauranteHandler;
import co.edu.clase.microservicio_plazoleta.aplicacion.handler.ListarPlatosHandler;
import co.edu.clase.microservicio_plazoleta.aplicacion.handler.ListarRestaurantesHandler;
import co.edu.clase.microservicio_plazoleta.infraestructura.entrada.rest.manejador.ManejadorGlobalExcepciones;
import co.edu.clase.microservicio_plazoleta.util.constantes.ConstantesApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RestauranteControladorRestTest {

    @Mock
    private CrearRestauranteHandler crearRestauranteHandler;
    @Mock
	private ListarRestaurantesHandler listarRestaurantesHandler;
	@Mock
	private ListarPlatosHandler listarPlatosHandler;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void configurar() {
        RestauranteControladorRest controlador =
                new RestauranteControladorRest(
                        crearRestauranteHandler,
						listarRestaurantesHandler,
						listarPlatosHandler
                );

        LocalValidatorFactoryBean validador =
                new LocalValidatorFactoryBean();

        validador.afterPropertiesSet();

        mockMvc = MockMvcBuilders
                .standaloneSetup(controlador)
                .setControllerAdvice(
                        new ManejadorGlobalExcepciones()
                )
                .setValidator(validador)
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    void debeCrearRestauranteYResponderCreado()
            throws Exception {

        CrearRestauranteSolicitudDto solicitud =
                crearSolicitudValida();

        when(crearRestauranteHandler.ejecutar(
                any(CrearRestauranteSolicitudDto.class)
        )).thenReturn(crearRespuesta());

        mockMvc.perform(post(
                        ConstantesApi.API_RESTAURANTES
                )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                solicitud
                        )))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre")
                        .value("Restaurante Caribe"))
                .andExpect(jsonPath("$.nit")
                        .value("900123456"))
                .andExpect(jsonPath("$.activo")
                        .value(true));

        verify(crearRestauranteHandler)
                .ejecutar(any(
                        CrearRestauranteSolicitudDto.class
                ));
    }

    @Test
    void debeRechazarNitConFormatoInvalido()
            throws Exception {

        CrearRestauranteSolicitudDto solicitud =
                crearSolicitudValida();

        solicitud.setNit("900ABC");

        mockMvc.perform(post(
                        ConstantesApi.API_RESTAURANTES
                )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                solicitud
                        )))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo")
                        .value("VAL-001"))
                .andExpect(jsonPath("$.detalles.nit")
                        .exists());
    }

    private CrearRestauranteSolicitudDto
            crearSolicitudValida() {

        CrearRestauranteSolicitudDto solicitud =
                new CrearRestauranteSolicitudDto();

        solicitud.setNombre("Restaurante Caribe");
        solicitud.setNit("900123456");
        solicitud.setDireccion("Calle 72 # 45-10");
        solicitud.setCelular("+573001234567");
        solicitud.setUrlLogo(
                "https://ejemplo.com/logo.png"
        );
        solicitud.setPropietarioId(2L);

        return solicitud;
    }

    private RestauranteRespuestaDto crearRespuesta() {
        RestauranteRespuestaDto respuesta =
                new RestauranteRespuestaDto();

        respuesta.setId(1L);
        respuesta.setNombre("Restaurante Caribe");
        respuesta.setNit("900123456");
        respuesta.setDireccion("Calle 72 # 45-10");
        respuesta.setCelular("+573001234567");
        respuesta.setUrlLogo(
                "https://ejemplo.com/logo.png"
        );
        respuesta.setPropietarioId(2L);
        respuesta.setActivo(Boolean.TRUE);
        respuesta.setFechaCreacion(
                LocalDateTime.of(
                        2026, 11, 15, 10, 0
                )
        );

        return respuesta;
    }
}