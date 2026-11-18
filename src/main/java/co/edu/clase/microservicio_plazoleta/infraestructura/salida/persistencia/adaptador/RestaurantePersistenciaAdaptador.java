package co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.adaptador;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pagina;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Restaurante;
import co.edu.clase.microservicio_plazoleta.dominio.spi.RestaurantePersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.entidad.RestauranteEntidad;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.mapeador.RestaurantePersistenciaMapeador;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.repositorio.RestauranteJpaRepositorio;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Sort;
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
            RestauranteJpaRepositorio restauranteJpaRepositorio,
            RestaurantePersistenciaMapeador restaurantePersistenciaMapeador) {

        this.restauranteJpaRepositorio = restauranteJpaRepositorio;

        this.restaurantePersistenciaMapeador = restaurantePersistenciaMapeador;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorNit(String nit) {
        return restauranteJpaRepositorio.existsByNit(nit);
    }

	@Override
	@Transactional(readOnly = true)
	public Optional<Restaurante> buscarPorId(
			Long restauranteId) {

		return restauranteJpaRepositorio
				.findById(restauranteId)
				.map(
					restaurantePersistenciaMapeador::aModelo
				);
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

    @Override
    @Transactional(readOnly = true)
    public Pagina<Restaurante> listarActivos(
            int pagina,
            int tamano) {

        PageRequest solicitudPagina =
                PageRequest.of(
                        pagina,
                        tamano
                        // Sort.by(
                        //         Sort.Direction.ASC,
                        //         "nombre"
                        // )
                );

        Page<RestauranteEntidad> paginaEntidades =
                restauranteJpaRepositorio
                        .findByActivoTrueOrderByNombreAsc(solicitudPagina);

        return new Pagina<>(
                paginaEntidades
                        .getContent()
                        .stream()
                        .map(
                                restaurantePersistenciaMapeador
                                        ::aModelo
                        )
                        .toList(),
                paginaEntidades.getNumber(),
                paginaEntidades.getSize(),
                paginaEntidades.getTotalElements(),
                paginaEntidades.getTotalPages(),
                paginaEntidades.isFirst(),
                paginaEntidades.isLast()
        );
    }
}