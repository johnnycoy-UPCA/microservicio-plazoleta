package co.edu.clase.microservicio_plazoleta.dominio.spi;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.Plato;

public interface PlatoPersistenciaPuerto {
    
    Plato guardar(Plato plato);
}
