package co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.adaptador;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.Restaurante;
import co.edu.clase.microservicio_plazoleta.dominio.spi.RestaurantePersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.entidad.RestauranteEntidad;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.mapeador.RestaurantePersistenciaMapeador;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.repositorio.RestauranteJpaRepositorio;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RestaurantePersistenciaAdaptador
        implements RestaurantePersistenciaPuerto {

    private final RestauranteJpaRepositorio
            restauranteJpaRepositorio;

    private final RestaurantePersistenciaMapeador
            restaurantePersistenciaMapeador;

    public RestaurantePersistenciaAdaptador(
            RestauranteJpaRepositorio
                    restauranteJpaRepositorio,
            RestaurantePersistenciaMapeador
                    restaurantePersistenciaMapeador) {

        this.restauranteJpaRepositorio =
                restauranteJpaRepositorio;

        this.restaurantePersistenciaMapeador =
                restaurantePersistenciaMapeador;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorNit(String nit) {
        return restauranteJpaRepositorio.existsByNit(nit);
    }

    @Override
    @Transactional
    public Restaurante guardar(
            Restaurante restaurante) {

        RestauranteEntidad entidad =
                restaurantePersistenciaMapeador.aEntidad(
                        restaurante
                );

        RestauranteEntidad entidadGuardada =
                restauranteJpaRepositorio.save(entidad);

        return restaurantePersistenciaMapeador.aModelo(
                entidadGuardada
        );
    }
}