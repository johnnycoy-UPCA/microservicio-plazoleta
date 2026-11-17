package co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.entidad.CategoriaEntidad;

public interface CategoriaJpaRepositorio 
        extends JpaRepository<CategoriaEntidad, Long> {
    
    Optional<CategoriaEntidad> findByIdAndActivoTrue(Long categoriaId);
}
