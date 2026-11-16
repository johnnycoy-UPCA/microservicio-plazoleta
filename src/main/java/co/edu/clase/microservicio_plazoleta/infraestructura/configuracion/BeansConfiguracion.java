package co.edu.clase.microservicio_plazoleta.infraestructura.configuracion;

import co.edu.clase.microservicio_plazoleta.dominio.api.CrearRestauranteServicioPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.casouso.CrearRestauranteCasoUso;
import co.edu.clase.microservicio_plazoleta.dominio.spi.PropietarioConsultaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.RestaurantePersistenciaPuerto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.time.Clock;

@Configuration
public class BeansConfiguracion {

    @Bean
    public Clock relojAplicacion() {
        return Clock.systemDefaultZone();
    }

    @Bean
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
}