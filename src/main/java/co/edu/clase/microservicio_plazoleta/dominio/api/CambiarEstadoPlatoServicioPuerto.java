package co.edu.clase.microservicio_plazoleta.dominio.api;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.Plato;

public interface CambiarEstadoPlatoServicioPuerto {
    
    Plato cambiarEstado(
        Long platoId,
        Boolean activo,
        Long propietarioAutenticadoId
    );
}
