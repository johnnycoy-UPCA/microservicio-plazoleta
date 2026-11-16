package co.edu.clase.microservicio_plazoleta.infraestructura.configuracion;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.edu.clase.microservicio_plazoleta.util.constantes.ConstantesSeguridad;

@Configuration
public class OpenApiConfiguracion {

	private static final String ESQUEMA_SEGURIDAD = "autenticacionBearer";

    @Bean
    public OpenAPI documentacionApi() {
        return new OpenAPI()
			.info(new Info()
				.title("Microservicio Plazoleta")
				.description(
					"API para gestionar restaurantes, "
					+ "platos y pedidos"
				)
				.version("1.0.0")
				.contact(new Contact()
					.name("Johnny Coy Herrera")
				)
			)
			.components(new Components()
				.addSecuritySchemes(
					ESQUEMA_SEGURIDAD,
					new SecurityScheme()
						.name(ConstantesSeguridad.ENCABEZADO_AUTORIZACION)
						.type(SecurityScheme.Type.HTTP)
						.scheme("bearer")
						.bearerFormat("JWT")
				)
			);
    }
}