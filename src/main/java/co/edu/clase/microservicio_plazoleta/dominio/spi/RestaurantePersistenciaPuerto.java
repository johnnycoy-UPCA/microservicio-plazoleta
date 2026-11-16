package co.edu.clase.microservicio_plazoleta.dominio.spi;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.Restaurante;

public interface RestaurantePersistenciaPuerto {

    boolean existePorNit(String nit);

    Restaurante guardar(Restaurante restaurante);
}