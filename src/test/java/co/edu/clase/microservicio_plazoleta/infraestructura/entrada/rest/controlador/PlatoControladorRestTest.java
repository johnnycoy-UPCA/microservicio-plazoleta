package co.edu.clase.microservicio_plazoleta.infraestructura.entrada.rest.controlador;

import co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta.PlatoRespuestaDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.dto.solicitud.CrearPlatoSolicitudDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.handler.CrearPlatoHandler;
import co.edu.clase.microservicio_plazoleta.infraestructura.entrada.rest.manejador.ManejadorGlobalExcepciones;
import co.edu.clase.microservicio_plazoleta.infraestructura.entrada.rest.seguridad.UsuarioAutenticado;
import co.edu.clase.microservicio_plazoleta.util.constantes.ConstantesApi;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.PerfilUsuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
// import org.springframework.security.web.bind.support.AuthenticationPrincipalArgumentResolver;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import org.junit.jupiter.api.AfterEach;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
// import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PlatoControladorRestTest {

    @Mock
    private CrearPlatoHandler crearPlatoHandler;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void configurar() {
        PlatoControladorRest controlador =
                new PlatoControladorRest(
					crearPlatoHandler
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
				.setCustomArgumentResolvers(
					new AuthenticationPrincipalArgumentResolver()
				)
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

	@AfterEach
	void limpiarContextoSeguridad() {
		SecurityContextHolder.clearContext();
	}

    @Test
    void debeCrearPlatoYResponderCreado()
            throws Exception {

        CrearPlatoSolicitudDto solicitud =
                crearSolicitudValida();

        when(crearPlatoHandler.ejecutar(
			any(CrearPlatoSolicitudDto.class),
			eq(10L)
        )).thenReturn(crearRespuesta());

        mockMvc.perform(post(ConstantesApi.API_PLATOS)
					.with(autenticacionPropietario())
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(
						solicitud
					)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre")
					.value("Arroz con pollo"))
                .andExpect(jsonPath("$.precio")
					.value(25000))
                .andExpect(jsonPath("$.categoriaId")
					.value(2))
                .andExpect(jsonPath("$.restauranteId")
					.value(1))
                .andExpect(jsonPath("$.activo")
					.value(true));

        verify(crearPlatoHandler).ejecutar(
			any(CrearPlatoSolicitudDto.class),
			eq(10L)
        );
    }

    @Test
    void debeRechazarPrecioNoPositivo()
            throws Exception {

        CrearPlatoSolicitudDto solicitud =
                crearSolicitudValida();

        solicitud.setPrecio(0);

        mockMvc.perform(post(ConstantesApi.API_PLATOS)
					.with(autenticacionPropietario())
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(
							solicitud
					)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo")
					.value("VAL-001"))
                .andExpect(jsonPath("$.detalles.precio")
					.exists());
    }

    private RequestPostProcessor
            autenticacionPropietario() {
		return solicitud -> {
			UsuarioAutenticado principal =
					new UsuarioAutenticado(
							10L,
							"propietario@correo.com",
							PerfilUsuario.PROPIETARIO,
							"token-prueba"
					);

        // return authentication(
        //         new org.springframework.security
        //                 .authentication
        //                 .UsernamePasswordAuthenticationToken(
		// 					principal,
		// 					null,
		// 					java.util.List.of(
		// 						new org.springframework
		// 							.security
		// 							.core
		// 							.authority
		// 							.SimpleGrantedAuthority(
		// 								"ROLE_PROPIETARIO"
		// 							)
		// 					)
        //                 )
        // );

			UsernamePasswordAuthenticationToken authenticacion =
					new UsernamePasswordAuthenticationToken(
							principal,
							null,
							List.of(
								new SimpleGrantedAuthority(
									"ROLE_PROPIETARIO"
								)
							)
					);

			SecurityContextHolder
					.getContext()
					.setAuthentication(authenticacion);
		
			return solicitud;
		};
    }

    private CrearPlatoSolicitudDto
            crearSolicitudValida() {

        CrearPlatoSolicitudDto solicitud =
                new CrearPlatoSolicitudDto();

        solicitud.setNombre("Arroz con pollo");
        solicitud.setDescripcion("Arroz acompañado de pollo");
        solicitud.setPrecio(25000);
        solicitud.setUrlImagen("https://ejemplo.com/arroz-pollo.png");
        solicitud.setCategoriaId(2L);
        solicitud.setRestauranteId(1L);

        return solicitud;
    }

    private PlatoRespuestaDto crearRespuesta() {
        PlatoRespuestaDto respuesta =
                new PlatoRespuestaDto();

        respuesta.setId(1L);
        respuesta.setNombre("Arroz con pollo");
        respuesta.setDescripcion("Arroz acompañado de pollo");
        respuesta.setPrecio(25000);
        respuesta.setUrlImagen("https://ejemplo.com/arroz-pollo.png");
        respuesta.setCategoriaId(2L);
        respuesta.setCategoriaNombre("PLATO_FUERTE");
        respuesta.setRestauranteId(1L);
        respuesta.setActivo(Boolean.TRUE);
        respuesta.setFechaCreacion(
                LocalDateTime.of(
                        2026, 11, 16, 14, 0
                )
        );

        return respuesta;
    }
}