package co.edu.clase.microservicio_plazoleta.infraestructura.configuracion;

import co.edu.clase.microservicio_plazoleta.dominio.api.CambiarEstadoPlatoServicioPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.api.CrearPlatoServicioPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.api.CrearRestauranteServicioPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.api.ListarPedidosServicioPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.api.ListarPlatosServicioPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.api.ListarRestaurantesServicioPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.casouso.CrearPlatoCasoUso;
import co.edu.clase.microservicio_plazoleta.dominio.casouso.CrearRestauranteCasoUso;
import co.edu.clase.microservicio_plazoleta.dominio.casouso.ListarPedidosCasoUso;
import co.edu.clase.microservicio_plazoleta.dominio.casouso.ListarPlatosCasoUso;
import co.edu.clase.microservicio_plazoleta.dominio.casouso.ListarRestaurantesCasoUso;
import co.edu.clase.microservicio_plazoleta.dominio.api.ModificarPlatoServicioPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.api.ProcesarEventoPendienteServicioPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.api.RealizarPedidoServicioPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.api.ValidarRestaurantePropietarioServicioPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.casouso.CambiarEstadoPlatoCasoUso;
// import co.edu.clase.microservicio_plazoleta.dominio.casouso.CrearPlatoCasoUso;
// import co.edu.clase.microservicio_plazoleta.dominio.casouso.CrearRestauranteCasoUso;
import co.edu.clase.microservicio_plazoleta.dominio.casouso.ModificarPlatoCasoUso;
import co.edu.clase.microservicio_plazoleta.dominio.casouso.ProcesarEventoPendienteCasoUso;
import co.edu.clase.microservicio_plazoleta.dominio.casouso.RealizarPedidoCasoUso;
import co.edu.clase.microservicio_plazoleta.dominio.casouso.ValidarRestaurantePropietarioCasoUso;
import co.edu.clase.microservicio_plazoleta.dominio.spi.CategoriaPersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.EmpleadoConsultaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.EventoPendientePersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.EventoSerializadorPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.PedidoPersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.PlatoPersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.PropietarioConsultaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.RestaurantePersistenciaPuerto;
// import co.edu.clase.microservicio_plazoleta.dominio.spi.TrazabilidadRegistroPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.TrazabilidadRegistroPuerto;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Clock;

@Configuration
public class BeansConfiguracion {

    @Bean
    public Clock relojAplicacion() {
        return Clock.systemDefaultZone();
    }

	@Bean
	public ObjectMapper objectMapperAplicacion() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();
		return objectMapper;
	}

    @Bean("clienteUsuarios")
    public RestClient clienteUsuarios(
        //     RestClient.Builder constructor,
		UsuariosServicioPropiedades propiedades) {

        // return constructor
        return RestClient.builder()
			.baseUrl(propiedades.getUrlBase())
			.build();
    }

    @Bean
    public CrearRestauranteServicioPuerto
            crearRestauranteServicioPuerto(
				RestaurantePersistenciaPuerto
					restaurantePersistenciaPuerto,
				PropietarioConsultaPuerto
					propietarioConsultaPuerto,
				Clock relojAplicacion) {

        return new CrearRestauranteCasoUso(
			restaurantePersistenciaPuerto,
			propietarioConsultaPuerto,
			relojAplicacion
        );
    }

	@Bean
	public CrearPlatoServicioPuerto
			crearPlatoServicioPuerto(
				PlatoPersistenciaPuerto
					platoPersistenciaPuerto,
				RestaurantePersistenciaPuerto
					restaurantePersistenciaPuerto,
				CategoriaPersistenciaPuerto
					categoriaPersistenciaPuerto,
				Clock relojAplicacion) {

		return new CrearPlatoCasoUso(
			platoPersistenciaPuerto,
			restaurantePersistenciaPuerto,
			categoriaPersistenciaPuerto,
			relojAplicacion
		);
	}

	@Bean
	public ListarRestaurantesServicioPuerto
			listarRestaurantesServicioPuerto(
					RestaurantePersistenciaPuerto
						restaurantePersistenciaPuerto) {

		return new ListarRestaurantesCasoUso(
			restaurantePersistenciaPuerto
		);
	}

	@Bean
	public ListarPlatosServicioPuerto
			listarPlatosServicioPuerto(
					PlatoPersistenciaPuerto
						platoPersistenciaPuerto,
					RestaurantePersistenciaPuerto
						restaurantePersistenciaPuerto) {

		return new ListarPlatosCasoUso(
			platoPersistenciaPuerto,
			restaurantePersistenciaPuerto
		);
	}

	@Bean
	public ModificarPlatoServicioPuerto
			modificarPlatoServicioPuerto(
					PlatoPersistenciaPuerto
						platoPersistenciaPuerto,
					RestaurantePersistenciaPuerto
						restaurantePersistenciaPuerto,
					Clock relojAplicacion) {

		return new ModificarPlatoCasoUso(
			platoPersistenciaPuerto,
			restaurantePersistenciaPuerto,
			relojAplicacion
		);
	}

	@Bean
	public CambiarEstadoPlatoServicioPuerto
			cambiarEstadoPlatoServicioPuerto(
					PlatoPersistenciaPuerto
						platoPersistenciaPuerto,
					RestaurantePersistenciaPuerto
						restaurantePersistenciaPuerto,
					Clock relojAplicacion) {

		return new CambiarEstadoPlatoCasoUso(
			platoPersistenciaPuerto,
			restaurantePersistenciaPuerto,
			relojAplicacion
		);
	}

	@Bean
	public ValidarRestaurantePropietarioServicioPuerto
			validarRestaurantePropietarioServicioPuerto(
					RestaurantePersistenciaPuerto
							restaurantePersistenciaPuerto) {

		return new ValidarRestaurantePropietarioCasoUso(
			restaurantePersistenciaPuerto
		);
	}

	@Bean
	public RealizarPedidoServicioPuerto
			realizarPedidoServicioPuerto(
					PedidoPersistenciaPuerto pedidoPersistenciaPuerto,
					PlatoPersistenciaPuerto platoPersistenciaPuerto,
					RestaurantePersistenciaPuerto restaurantePersistenciaPuerto,
					// TrazabilidadRegistroPuerto trazabilidadRegistroPuerto,
					EventoPendientePersistenciaPuerto eventoPendientePersistenciaPuerto,
					EventoSerializadorPuerto eventoSerializadorPuerto,
					Clock relojAplicacion) {

		return new RealizarPedidoCasoUso(
			pedidoPersistenciaPuerto,
			platoPersistenciaPuerto,
			restaurantePersistenciaPuerto,
			// trazabilidadRegistroPuerto,
			eventoPendientePersistenciaPuerto,
			eventoSerializadorPuerto,
			relojAplicacion
		);
	}

	@Bean("clienteTrazabilidad")
	public RestClient clienteTrazabilidad(
			TrazabilidadServicioPropiedades propiedades) {

		return RestClient.builder()
				.baseUrl(propiedades.getUrlBase())
				.build();
	}

	@Bean
	public ProcesarEventoPendienteServicioPuerto
			procesarEventoPendienteServicioPuerto(
					EventoPendientePersistenciaPuerto
							eventoPendientePersistenciaPuerto,
					EventoSerializadorPuerto
							eventoSerializadorPuerto,
					TrazabilidadRegistroPuerto
							trazabilidadRegistroPuerto,
					Clock relojAplicacion) {

		return new ProcesarEventoPendienteCasoUso(
				eventoPendientePersistenciaPuerto,
				eventoSerializadorPuerto,
				trazabilidadRegistroPuerto,
				relojAplicacion
		);
	}

	@Bean
	public ListarPedidosServicioPuerto
			listarPedidosServicioPuerto(
					PedidoPersistenciaPuerto
							pedidoPersistenciaPuerto,
					EmpleadoConsultaPuerto
							empleadoConsultaPuerto) {

		return new ListarPedidosCasoUso(
				pedidoPersistenciaPuerto,
				empleadoConsultaPuerto
		);
	}
}