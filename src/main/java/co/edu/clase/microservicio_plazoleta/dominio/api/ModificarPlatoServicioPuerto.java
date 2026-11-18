package co.edu.clase.microservicio_plazoleta.dominio.api;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.Plato;

public interface ModificarPlatoServicioPuerto {
    
    Plato modificar(
        Long platoId,
        Integer precio,
        String descripcion,
        Long propietarioAutenticadoId
    );
}
