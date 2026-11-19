package co.edu.clase.microservicio_plazoleta.dominio.spi;

import java.util.List;
import java.util.Optional;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pagina;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Plato;

public interface PlatoPersistenciaPuerto {

    Optional<Plato> buscarPorId(Long platoId);
    
    Pagina<Plato> listarActivosPorRestaurante(
            Long restauranteId,
            Long categoriaId,
            int pagina,
            int tamano
    );

    List<Plato> buscarActivosPorIds(List<Long> platoIds);
    
    Plato guardar(Plato plato);

}
