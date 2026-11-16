package co.edu.clase.microservicio_plazoleta.dominio.casouso;

import co.edu.clase.microservicio_plazoleta.dominio.api.CrearPlatoServicioPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.excepcion.ExcepcionPlazoleta;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Categoria;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Plato;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Restaurante;
import co.edu.clase.microservicio_plazoleta.dominio.spi.CategoriaPersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.PlatoPersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.RestaurantePersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.util.constantes.CodigosError;
import co.edu.clase.microservicio_plazoleta.util.constantes.MensajesError;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.CategoriaError;

import java.time.Clock;
import java.time.LocalDateTime;

public class CrearPlatoCasoUso
        implements CrearPlatoServicioPuerto {

    private final PlatoPersistenciaPuerto
            platoPersistenciaPuerto;

    private final RestaurantePersistenciaPuerto
            restaurantePersistenciaPuerto;

    private final CategoriaPersistenciaPuerto
            categoriaPersistenciaPuerto;

    private final Clock reloj;

    public CrearPlatoCasoUso(
            PlatoPersistenciaPuerto platoPersistenciaPuerto,
            RestaurantePersistenciaPuerto
                    restaurantePersistenciaPuerto,
            CategoriaPersistenciaPuerto
                    categoriaPersistenciaPuerto,
            Clock reloj) {

        this.platoPersistenciaPuerto =
                platoPersistenciaPuerto;

        this.restaurantePersistenciaPuerto =
                restaurantePersistenciaPuerto;

        this.categoriaPersistenciaPuerto =
                categoriaPersistenciaPuerto;

        this.reloj = reloj;
    }

    @Override
    public Plato crear(
            Plato plato,
            Long propietarioAutenticadoId) {

        normalizarDatos(plato);
        validarPrecio(plato.getPrecio());

        Restaurante restaurante = obtenerRestaurante(
                plato.getRestauranteId()
        );

        validarRestauranteActivo(restaurante);

        validarPropiedadRestaurante(
                restaurante,
                propietarioAutenticadoId
        );

        Categoria categoria = obtenerCategoria(
                plato.getCategoria().getId()
        );

        LocalDateTime fechaActual =
                LocalDateTime.now(reloj);

        plato.setCategoria(categoria);
        plato.setActivo(Boolean.TRUE);
        plato.setFechaCreacion(fechaActual);
        plato.setFechaActualizacion(fechaActual);

        return platoPersistenciaPuerto.guardar(plato);
    }

    private void normalizarDatos(Plato plato) {
        if (plato.getNombre() != null) {
            plato.setNombre(plato.getNombre().trim());
        }

        if (plato.getDescripcion() != null) {
            plato.setDescripcion(
                    plato.getDescripcion().trim()
            );
        }

        if (plato.getUrlImagen() != null) {
            plato.setUrlImagen(
                    plato.getUrlImagen().trim()
            );
        }
    }

    private void validarPrecio(Integer precio) {
        if (precio == null || precio <= 0) {
            throw new ExcepcionPlazoleta(
                    CodigosError.PRECIO_PLATO_INVALIDO,
                    MensajesError.PRECIO_PLATO_INVALIDO,
                    CategoriaError.VALIDACION
            );
        }
    }

    private Restaurante obtenerRestaurante(
            Long restauranteId) {

        return restaurantePersistenciaPuerto
                .buscarPorId(restauranteId)
                .orElseThrow(() -> new ExcepcionPlazoleta(
                        CodigosError.RESTAURANTE_NO_ENCONTRADO,
                        MensajesError.RESTAURANTE_NO_ENCONTRADO,
                        CategoriaError.NO_ENCONTRADO
                ));
    }

    private void validarRestauranteActivo(
            Restaurante restaurante) {

        if (!Boolean.TRUE.equals(restaurante.getActivo())) {
            throw new ExcepcionPlazoleta(
                    CodigosError.RESTAURANTE_INACTIVO,
                    MensajesError.RESTAURANTE_INACTIVO,
                    CategoriaError.VALIDACION
            );
        }
    }

    private void validarPropiedadRestaurante(
            Restaurante restaurante,
            Long propietarioAutenticadoId) {

        if (!restaurante.getPropietarioId()
                .equals(propietarioAutenticadoId)) {

            throw new ExcepcionPlazoleta(
                    CodigosError
                            .RESTAURANTE_NO_PERTENECE_PROPIETARIO,
                    MensajesError
                            .RESTAURANTE_NO_PERTENECE_PROPIETARIO,
                    CategoriaError.ACCESO_DENEGADO
            );
        }
    }

    private Categoria obtenerCategoria(
            Long categoriaId) {

        return categoriaPersistenciaPuerto
                .buscarActivaPorId(categoriaId)
                .orElseThrow(() -> new ExcepcionPlazoleta(
                        CodigosError.CATEGORIA_NO_ENCONTRADA,
                        MensajesError.CATEGORIA_NO_ENCONTRADA,
                        CategoriaError.NO_ENCONTRADO
                ));
    }
}