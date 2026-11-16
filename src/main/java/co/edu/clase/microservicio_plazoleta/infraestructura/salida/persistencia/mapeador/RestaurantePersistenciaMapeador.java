package co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.mapeador;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.Restaurante;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.entidad.RestauranteEntidad;
import org.springframework.stereotype.Component;

@Component
public class RestaurantePersistenciaMapeador {

    public RestauranteEntidad aEntidad(
            Restaurante modelo) {

        if (modelo == null) {
            return null;
        }

        RestauranteEntidad entidad =
                new RestauranteEntidad();

        entidad.setId(modelo.getId());
        entidad.setNombre(modelo.getNombre());
        entidad.setNit(modelo.getNit());
        entidad.setDireccion(modelo.getDireccion());
        entidad.setCelular(modelo.getCelular());
        entidad.setUrlLogo(modelo.getUrlLogo());
        entidad.setPropietarioId(
                modelo.getPropietarioId()
        );
        entidad.setActivo(modelo.getActivo());
        entidad.setFechaCreacion(
                modelo.getFechaCreacion()
        );
        entidad.setFechaActualizacion(
                modelo.getFechaActualizacion()
        );

        return entidad;
    }

    public Restaurante aModelo(
            RestauranteEntidad entidad) {

        if (entidad == null) {
            return null;
        }

        return new Restaurante(
                entidad.getId(),
                entidad.getNombre(),
                entidad.getNit(),
                entidad.getDireccion(),
                entidad.getCelular(),
                entidad.getUrlLogo(),
                entidad.getPropietarioId(),
                entidad.getActivo(),
                entidad.getFechaCreacion(),
                entidad.getFechaActualizacion()
        );
    }
}