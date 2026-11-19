package co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.repositorio;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.entidad.PlatoEntidad;

public interface PlatoJpaRepositorio 
    extends JpaRepository<PlatoEntidad, Long> {
    
        Page<PlatoEntidad>
			findByRestauranteIdAndActivoTrueOrderByNombreAsc(
				Long restauranteId,
				Pageable pageable
			);

        Page<PlatoEntidad>
			findByRestauranteIdAndCategoriaIdAndActivoTrueOrderByNombreAsc(
				Long restauranteId,
				Long categoriaId,
				Pageable pageable
			);

		List<PlatoEntidad> 
			findByIdInAndActivoTrue(
				List<Long> platoIds
		);
}
