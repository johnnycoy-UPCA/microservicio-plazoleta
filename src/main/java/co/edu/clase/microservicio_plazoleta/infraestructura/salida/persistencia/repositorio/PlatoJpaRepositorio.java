package co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.entidad.PlatoEntidad;

public interface PlatoJpaRepositorio 
    extends JpaRepository<PlatoEntidad, Long> {
    
}
