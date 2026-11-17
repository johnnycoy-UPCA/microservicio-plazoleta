package co.edu.clase.microservicio_plazoleta.aplicacion.mapeador;

import co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta.PlatoRespuestaDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.dto.solicitud.CrearPlatoSolicitudDto;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Categoria;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Plato;
import org.springframework.stereotype.Component;

@Component
public class PlatoDtoMapeador {

    public Plato aModelo(
            CrearPlatoSolicitudDto solicitud) {

        Plato plato = new Plato();

        plato.setNombre(solicitud.getNombre());
        plato.setDescripcion(solicitud.getDescripcion());
        plato.setPrecio(solicitud.getPrecio());
        plato.setUrlImagen(solicitud.getUrlImagen());
        plato.setRestauranteId(
                solicitud.getRestauranteId()
        );

        Categoria categoria = new Categoria();
        categoria.setId(solicitud.getCategoriaId());

        plato.setCategoria(categoria);

        return plato;
    }

    public PlatoRespuestaDto aRespuesta(Plato plato) {
        PlatoRespuestaDto respuesta =
                new PlatoRespuestaDto();

        respuesta.setId(plato.getId());
        respuesta.setNombre(plato.getNombre());
        respuesta.setDescripcion(plato.getDescripcion());
        respuesta.setPrecio(plato.getPrecio());
        respuesta.setUrlImagen(plato.getUrlImagen());
        respuesta.setCategoriaId(
                plato.getCategoria().getId()
        );
        respuesta.setCategoriaNombre(
                plato.getCategoria().getNombre()
        );
        respuesta.setRestauranteId(
                plato.getRestauranteId()
        );
        respuesta.setActivo(plato.getActivo());
        respuesta.setFechaCreacion(
                plato.getFechaCreacion()
        );

        return respuesta;
    }
}