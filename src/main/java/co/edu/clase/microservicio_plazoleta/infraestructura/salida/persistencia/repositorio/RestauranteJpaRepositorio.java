package co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.repositorio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.entidad.RestauranteEntidad;

public interface RestauranteJpaRepositorio
    extends JpaRepository<RestauranteEntidad, Long> {
        boolean existsByNit(String nit);

        Page<RestauranteEntidad> 
            findByActivoTrueOrderByNombreAsc(
                Pageable pageable
        );
    }
