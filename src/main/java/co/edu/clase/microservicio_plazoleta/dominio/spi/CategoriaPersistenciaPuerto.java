package co.edu.clase.microservicio_plazoleta.dominio.spi;

import java.util.Optional;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.Categoria;

public interface CategoriaPersistenciaPuerto {
    
    Optional<Categoria> buscarActivaPorId(Long categoriaId);
}
