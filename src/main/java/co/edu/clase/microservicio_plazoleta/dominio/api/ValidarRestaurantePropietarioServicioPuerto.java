package co.edu.clase.microservicio_plazoleta.dominio.api;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.RestauranteValidacion;

public interface ValidarRestaurantePropietarioServicioPuerto {
    
    RestauranteValidacion validar(
        Long restauranteId,
        Long propietarioAutenticadoId
    );
}
