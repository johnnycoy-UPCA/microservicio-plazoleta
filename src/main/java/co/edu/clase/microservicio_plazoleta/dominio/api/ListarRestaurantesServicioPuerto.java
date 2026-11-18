package co.edu.clase.microservicio_plazoleta.dominio.api;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pagina;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Restaurante;

public interface ListarRestaurantesServicioPuerto {
    
    Pagina<Restaurante> listar(
        int pagina,
        int tamano
    );
}
