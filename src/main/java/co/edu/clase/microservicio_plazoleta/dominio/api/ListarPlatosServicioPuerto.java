package co.edu.clase.microservicio_plazoleta.dominio.api;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pagina;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Plato;

public interface ListarPlatosServicioPuerto {
    
    Pagina<Plato> listar(
        Long restauranteId,
        Long categoriaId,
        int pagina,
        int tamano
    );
}
