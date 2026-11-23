package co.edu.clase.microservicio_plazoleta.infraestructura.entrada.rest.controlador;

import co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta.DetallePedidoRespuestaDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta.PaginaRespuestaDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta.PedidoRespuestaDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.dto.solicitud.DetallePedidoSolicitudDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.dto.solicitud.RealizarPedidoSolicitudDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.handler.AsignarPedidoHandler;
import co.edu.clase.microservicio_plazoleta.aplicacion.handler.ListarPedidosHandler;
import co.edu.clase.microservicio_plazoleta.aplicacion.handler.MarcarPedidoListoHandler;
import co.edu.clase.microservicio_plazoleta.aplicacion.handler.RealizarPedidoHandler;
import co.edu.clase.microservicio_plazoleta.infraestructura.entrada.rest.manejador.ManejadorGlobalExcepciones;
import co.edu.clase.microservicio_plazoleta.infraestructura.entrada.rest.seguridad.UsuarioAutenticado;
import co.edu.clase.microservicio_plazoleta.util.constantes.ConstantesApi;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoPedido;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.PerfilUsuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

@ExtendWith(MockitoExtension.class)
class PedidoControladorRestTest {

    @Mock
    private RealizarPedidoHandler realizarPedidoHandler;
	@Mock
	private ListarPedidosHandler listarPedidosHandler;
	@Mock
	private AsignarPedidoHandler asignarPedidoHandler;
	@Mock
	private MarcarPedidoListoHandler marcarPedidoListoHandler;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void configurar() {
        PedidoControladorRest controlador =
                new PedidoControladorRest(
                        realizarPedidoHandler,
						listarPedidosHandler,
						asignarPedidoHandler,
						marcarPedidoListoHandler
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
    void limpiarContexto() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void debeRealizarPedidoYResponderCreado()
            throws Exception {

        RealizarPedidoSolicitudDto solicitud =
                crearSolicitud();

        when(realizarPedidoHandler.ejecutar(
                any(RealizarPedidoSolicitudDto.class),
                eq(20L)
        )).thenReturn(crearRespuesta());

        mockMvc.perform(post(ConstantesApi.API_PEDIDOS)
                        .with(autenticacionCliente())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                solicitud
                        )))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.clienteId")
                        .value(20))
                .andExpect(jsonPath("$.estado")
                        .value("PENDIENTE"))
                .andExpect(jsonPath("$.total")
                        .value(48000))
                .andExpect(jsonPath("$.platos.length()")
                        .value(2));

        verify(realizarPedidoHandler).ejecutar(
                any(RealizarPedidoSolicitudDto.class),
                eq(20L)
        );
    }

    @Test
    void debeRechazarPedidoSinPlatos()
            throws Exception {

        RealizarPedidoSolicitudDto solicitud =
                crearSolicitud();

        solicitud.setPlatos(List.of());

        mockMvc.perform(post(ConstantesApi.API_PEDIDOS)
                        .with(autenticacionCliente())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                solicitud
                        )))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo")
                        .value("VAL-001"))
                .andExpect(jsonPath("$.detalles.platos")
                        .exists());
    }

	@Test
	void debeListarPedidosPorEstado()
			throws Exception {

		PaginaRespuestaDto<PedidoRespuestaDto> pagina =
				new PaginaRespuestaDto<>(
						List.of(crearRespuesta()),
						0,
						10,
						1L,
						1,
						true,
						true
				);

		when(listarPedidosHandler.ejecutar(
				eq(30L),
				eq("token-empleado"),
				eq(EstadoPedido.PENDIENTE),
				eq(0),
				eq(10)
		)).thenReturn(pagina);

		mockMvc.perform(get(ConstantesApi.API_PEDIDOS)
						.param(
								"estado",
								"PENDIENTE"
						)
						.param("pagina", "0")
						.param("tamano", "10")
						.with(
								autenticacionEmpleado()
						))
				.andExpect(status().isOk())
				.andExpect(jsonPath(
						"$.contenido.length()"
				).value(1))
				.andExpect(jsonPath(
						"$.contenido[0].id"
				).value(100))
				.andExpect(jsonPath(
						"$.contenido[0].estado"
				).value("PENDIENTE"))
				.andExpect(jsonPath(
						"$.paginaActual"
				).value(0))
				.andExpect(jsonPath(
						"$.totalElementos"
				).value(1));

		verify(listarPedidosHandler).ejecutar(
				eq(30L),
				eq("token-empleado"),
				eq(EstadoPedido.PENDIENTE),
				eq(0),
				eq(10)
		);
	}

	@Test
	void debeAsignarPedidoYResponderCorrectamente()
			throws Exception {

		PedidoRespuestaDto respuesta =
				crearRespuesta();

		respuesta.setEmpleadoAsignadoId(30L);
		respuesta.setEstado(
				EstadoPedido.EN_PREPARACION
		);

		when(asignarPedidoHandler.ejecutar(
				eq(100L),
				eq(30L),
				eq("token-empleado")
		)).thenReturn(respuesta);

		mockMvc.perform(
						patch(
								ConstantesApi.API_PEDIDOS
										+ "/100/asignacion"
						)
								.with(
										autenticacionEmpleado()
								)
				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id")
						.value(100))
				.andExpect(jsonPath(
						"$.empleadoAsignadoId"
				).value(30))
				.andExpect(jsonPath("$.estado")
						.value("EN_PREPARACION"));

		verify(asignarPedidoHandler).ejecutar(
				eq(100L),
				eq(30L),
				eq("token-empleado")
		);
	}

	@Test
	void debeMarcarPedidoListo()
			throws Exception {

		PedidoRespuestaDto respuesta =
				crearRespuesta();

		respuesta.setEmpleadoAsignadoId(30L);
		respuesta.setEstado(EstadoPedido.LISTO);

		when(marcarPedidoListoHandler.ejecutar(
				eq(100L),
				eq(30L),
				eq("token-empleado")
		)).thenReturn(respuesta);

		mockMvc.perform(
						patch(
								ConstantesApi.API_PEDIDOS
										+ "/100/listo"
						)
								.with(
										autenticacionEmpleado()
								)
				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id")
						.value(100))
				.andExpect(jsonPath("$.estado")
						.value("LISTO"))
				.andExpect(jsonPath(
						"$.empleadoAsignadoId"
				).value(30))
				.andExpect(jsonPath("$.pinHash")
						.doesNotExist())
				.andExpect(jsonPath("$.pinGeneradoEn")
						.doesNotExist())
				.andExpect(jsonPath("$.pinExpiraEn")
						.doesNotExist());

		verify(marcarPedidoListoHandler).ejecutar(
				eq(100L),
				eq(30L),
				eq("token-empleado")
		);
	}

    private RequestPostProcessor autenticacionCliente() {
        return solicitud -> {
            UsuarioAutenticado principal =
                    new UsuarioAutenticado(
                            20L,
                            "cliente@correo.com",
                            PerfilUsuario.CLIENTE,
                            "token-cliente"
                    );

            UsernamePasswordAuthenticationToken autenticacion =
                    new UsernamePasswordAuthenticationToken(
                            principal,
                            null,
                            List.of(
                                    new SimpleGrantedAuthority(
                                            "ROLE_CLIENTE"
                                    )
                            )
                    );

            SecurityContextHolder.getContext()
                    .setAuthentication(autenticacion);

            return solicitud;
        };
    }

    private RealizarPedidoSolicitudDto
            crearSolicitud() {

        DetallePedidoSolicitudDto arroz =
                new DetallePedidoSolicitudDto();

        arroz.setPlatoId(1L);
        arroz.setCantidad(2);

        DetallePedidoSolicitudDto limonada =
                new DetallePedidoSolicitudDto();

        limonada.setPlatoId(2L);
        limonada.setCantidad(1);

        RealizarPedidoSolicitudDto solicitud =
                new RealizarPedidoSolicitudDto();

        solicitud.setRestauranteId(1L);
        solicitud.setPlatos(
                List.of(arroz, limonada)
        );

        return solicitud;
    }

    private PedidoRespuestaDto crearRespuesta() {
        PedidoRespuestaDto respuesta =
                new PedidoRespuestaDto();

        respuesta.setId(100L);
        respuesta.setClienteId(20L);
        respuesta.setRestauranteId(1L);
        respuesta.setEstado(EstadoPedido.PENDIENTE);
        respuesta.setTotal(48000L);
        respuesta.setFechaCreacion(
                Instant.parse(
                        "2026-11-19T18:00:00Z"
                )
        );
        respuesta.setPlatos(List.of(
                new DetallePedidoRespuestaDto(
                        1L,
                        "Arroz",
                        20000,
                        2,
                        40000L
                ),
                new DetallePedidoRespuestaDto(
                        2L,
                        "Limonada",
                        8000,
                        1,
                        8000L
                )
        ));

        return respuesta;
    }

	private RequestPostProcessor
			autenticacionEmpleado() {

		return solicitud -> {
			UsuarioAutenticado principal =
					new UsuarioAutenticado(
							30L,
							"empleado@correo.com",
							PerfilUsuario.EMPLEADO,
							"token-empleado"
					);

			UsernamePasswordAuthenticationToken autenticacion =
					new UsernamePasswordAuthenticationToken(
							principal,
							null,
							List.of(
									new SimpleGrantedAuthority(
											"ROLE_EMPLEADO"
									)
							)
					);

			SecurityContextHolder
					.getContext()
					.setAuthentication(autenticacion);

			return solicitud;
		};
	}
}