package co.edu.clase.microservicio_plazoleta.dominio.spi;

import java.util.Optional;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pagina;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Restaurante;

public interface RestaurantePersistenciaPuerto {

    boolean existePorNit(String nit);

    Optional<Restaurante> buscarPorId(Long restauranteId);

    Pagina<Restaurante> listarActivos(
            int pagina,
            int tamano
    );

    Restaurante guardar(Restaurante restaurante);

}