package co.edu.clase.microservicio_plazoleta.infraestructura.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import co.edu.clase.microservicio_plazoleta.infraestructura.entrada.rest.seguridad.FiltroAutenticacionJwt;
import co.edu.clase.microservicio_plazoleta.infraestructura.entrada.rest.seguridad.ManejadorAccesoDenegado;
import co.edu.clase.microservicio_plazoleta.infraestructura.entrada.rest.seguridad.PuntoEntradaNoAutorizado;
import co.edu.clase.microservicio_plazoleta.util.constantes.ConstantesApi;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.PerfilUsuario;

@Configuration
@Profile("desarrollo")
public class SeguridadDesarrolloConfiguracion {

    @Bean
    public SecurityFilterChain filtroSeguridadDesarrollo(
            HttpSecurity seguridadHttp,
			FiltroAutenticacionJwt filtroAutenticacionJwt,
			PuntoEntradaNoAutorizado puntoEntradaNoAutorizado,
            ManejadorAccesoDenegado manejadorAccesoDenegado
    		) 
			throws Exception {

        seguridadHttp
			.authorizeHttpRequests(autorizacion -> autorizacion
				.requestMatchers(
					"/h2-console/**",
					"/v3/api-docs/**",
					"/swagger-ui/**",
					"/swagger-ui.html"
				)
				.permitAll()
				.requestMatchers(
					HttpMethod.POST,
					ConstantesApi.API_RESTAURANTES
				)
				.hasRole(
					PerfilUsuario.ADMINISTRADOR.name()
				)
				.requestMatchers(
					HttpMethod.GET,
					ConstantesApi.API_RESTAURANTES
				)
				.hasRole(
					PerfilUsuario.CLIENTE.name()
				)
				.requestMatchers(
					ConstantesApi.API_PLATOS,
					ConstantesApi.API_PLATOS + "/**"
				)
				.hasRole(
					PerfilUsuario.PROPIETARIO.name()
				)
				.requestMatchers(
						HttpMethod.GET,
						ConstantesApi.API_RESTAURANTES + "/*/validacion-propietario"
				)
				.hasRole(
						PerfilUsuario.PROPIETARIO.name()
				)
				.requestMatchers(
					HttpMethod.GET,
					ConstantesApi.API_RESTAURANTES + "/**"
				)
				.hasRole(
					PerfilUsuario.CLIENTE.name()
				)
				.anyRequest()
				.authenticated()
			)
			.csrf(csrf -> csrf
				.ignoringRequestMatchers(
					"/h2-console/**",
					ConstantesApi.API_RESTAURANTES,
					ConstantesApi.API_PLATOS,
					ConstantesApi.API_PLATOS + "/**"
				)
			)
			.headers(encabezados -> encabezados
				.frameOptions(frameOptions -> frameOptions
					.sameOrigin()
				)
			)
			.sessionManagement(sesion -> sesion
				.sessionCreationPolicy(
					SessionCreationPolicy.STATELESS
				)
			)
			.exceptionHandling(excepciones -> excepciones
				.authenticationEntryPoint(
					puntoEntradaNoAutorizado
				)
				.accessDeniedHandler(
					manejadorAccesoDenegado
				)
			)
			.addFilterBefore(
				filtroAutenticacionJwt,
				UsernamePasswordAuthenticationFilter.class
			);

        return seguridadHttp.build();
    }
}