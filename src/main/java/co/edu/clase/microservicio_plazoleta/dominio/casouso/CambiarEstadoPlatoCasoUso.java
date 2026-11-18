package co.edu.clase.microservicio_plazoleta.dominio.casouso;

import co.edu.clase.microservicio_plazoleta.dominio.api.CambiarEstadoPlatoServicioPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.excepcion.ExcepcionPlazoleta;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Plato;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Restaurante;
import co.edu.clase.microservicio_plazoleta.dominio.spi.PlatoPersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.RestaurantePersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.util.constantes.CodigosError;
import co.edu.clase.microservicio_plazoleta.util.constantes.MensajesError;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.CategoriaError;

import java.time.Clock;
import java.time.LocalDateTime;

public class CambiarEstadoPlatoCasoUso
        implements CambiarEstadoPlatoServicioPuerto {

    private final PlatoPersistenciaPuerto
            platoPersistenciaPuerto;

    private final RestaurantePersistenciaPuerto
            restaurantePersistenciaPuerto;

    private final Clock reloj;

    public CambiarEstadoPlatoCasoUso(
            PlatoPersistenciaPuerto platoPersistenciaPuerto,
            RestaurantePersistenciaPuerto
                    restaurantePersistenciaPuerto,
            Clock reloj) {

        this.platoPersistenciaPuerto =
                platoPersistenciaPuerto;

        this.restaurantePersistenciaPuerto =
                restaurantePersistenciaPuerto;

        this.reloj = reloj;
    }

    @Override
    public Plato cambiarEstado(
            Long platoId,
            Boolean activo,
            Long propietarioAutenticadoId) {

        validarEstado(activo);

        Plato plato = obtenerPlato(platoId);

        Restaurante restaurante = obtenerRestaurante(
                plato.getRestauranteId()
        );

        validarPropiedadRestaurante(
                restaurante,
                propietarioAutenticadoId
        );

        plato.setActivo(activo);
        plato.setFechaActualizacion(
                LocalDateTime.now(reloj)
        );

        return platoPersistenciaPuerto.guardar(plato);
    }

    private void validarEstado(Boolean activo) {
        if (activo == null) {
            throw new ExcepcionPlazoleta(
                    CodigosError.ESTADO_PLATO_OBLIGATORIO,
                    MensajesError.ESTADO_PLATO_OBLIGATORIO,
                    CategoriaError.VALIDACION
            );
        }
    }

    private Plato obtenerPlato(Long platoId) {
        return platoPersistenciaPuerto
                .buscarPorId(platoId)
                .orElseThrow(() -> new ExcepcionPlazoleta(
                        CodigosError.PLATO_NO_ENCONTRADO,
                        MensajesError.PLATO_NO_ENCONTRADO,
                        CategoriaError.NO_ENCONTRADO
                ));
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
}
