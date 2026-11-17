package co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.mapeador;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.Categoria;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.entidad.CategoriaEntidad;
import org.springframework.stereotype.Component;

@Component
public class CategoriaPersistenciaMapeador {

    public Categoria aModelo(
            CategoriaEntidad entidad) {

        if (entidad == null) {
            return null;
        }

        return new Categoria(
                entidad.getId(),
                entidad.getNombre(),
                entidad.getDescripcion(),
                entidad.getActivo()
        );
    }

    public CategoriaEntidad aEntidad(
            Categoria modelo) {

        if (modelo == null) {
            return null;
        }

        CategoriaEntidad entidad =
                new CategoriaEntidad();

        entidad.setId(modelo.getId());
        entidad.setNombre(modelo.getNombre());
        entidad.setDescripcion(modelo.getDescripcion());
        entidad.setActivo(modelo.getActivo());

        return entidad;
    }
}