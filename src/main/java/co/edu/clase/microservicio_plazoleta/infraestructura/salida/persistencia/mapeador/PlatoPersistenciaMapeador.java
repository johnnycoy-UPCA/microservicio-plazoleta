package co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.mapeador;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.Plato;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.entidad.PlatoEntidad;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.entidad.RestauranteEntidad;
import org.springframework.stereotype.Component;

@Component
public class PlatoPersistenciaMapeador {

    private final CategoriaPersistenciaMapeador
            categoriaPersistenciaMapeador;

    public PlatoPersistenciaMapeador(
            CategoriaPersistenciaMapeador
                    categoriaPersistenciaMapeador) {

        this.categoriaPersistenciaMapeador =
                categoriaPersistenciaMapeador;
    }

    public PlatoEntidad aEntidad(Plato modelo) {
        if (modelo == null) {
            return null;
        }

        PlatoEntidad entidad = new PlatoEntidad();

        entidad.setId(modelo.getId());
        entidad.setNombre(modelo.getNombre());
        entidad.setDescripcion(modelo.getDescripcion());
        entidad.setPrecio(modelo.getPrecio());
        entidad.setUrlImagen(modelo.getUrlImagen());
        entidad.setCategoria(
                categoriaPersistenciaMapeador.aEntidad(
                        modelo.getCategoria()
                )
        );

        RestauranteEntidad restaurante =
                new RestauranteEntidad();

        restaurante.setId(modelo.getRestauranteId());

        entidad.setRestaurante(restaurante);
        entidad.setActivo(modelo.getActivo());
        entidad.setFechaCreacion(
                modelo.getFechaCreacion()
        );
        entidad.setFechaActualizacion(
                modelo.getFechaActualizacion()
        );

        return entidad;
    }

    public Plato aModelo(PlatoEntidad entidad) {
        if (entidad == null) {
            return null;
        }

        return new Plato(
                entidad.getId(),
                entidad.getNombre(),
                entidad.getDescripcion(),
                entidad.getPrecio(),
                entidad.getUrlImagen(),
                categoriaPersistenciaMapeador.aModelo(
                        entidad.getCategoria()
                ),
                entidad.getRestaurante().getId(),
                entidad.getActivo(),
                entidad.getFechaCreacion(),
                entidad.getFechaActualizacion()
        );
    }
}