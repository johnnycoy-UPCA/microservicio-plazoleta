package co.edu.clase.microservicio_plazoleta.dominio.api;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.Plato;

public interface CrearPlatoServicioPuerto {
    
    Plato crear(
        Plato plato,
        Long propietarioAutenticadoId
    );
}
