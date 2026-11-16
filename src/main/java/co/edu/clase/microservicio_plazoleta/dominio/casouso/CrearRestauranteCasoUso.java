package co.edu.clase.microservicio_plazoleta.dominio.casouso;

import co.edu.clase.microservicio_plazoleta.dominio.api.CrearRestauranteServicioPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.excepcion.ExcepcionPlazoleta;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Propietario;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Restaurante;
import co.edu.clase.microservicio_plazoleta.dominio.spi.PropietarioConsultaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.RestaurantePersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.util.constantes.CodigosError;
import co.edu.clase.microservicio_plazoleta.util.constantes.ConstantesValidacion;
import co.edu.clase.microservicio_plazoleta.util.constantes.MensajesError;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.CategoriaError;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.PerfilUsuario;

import java.time.Clock;
import java.time.LocalDateTime;

public class CrearRestauranteCasoUso
        implements CrearRestauranteServicioPuerto {

    private final RestaurantePersistenciaPuerto
            restaurantePersistenciaPuerto;

    private final PropietarioConsultaPuerto
            propietarioConsultaPuerto;

    private final Clock reloj;

    public CrearRestauranteCasoUso(
            RestaurantePersistenciaPuerto
                    restaurantePersistenciaPuerto,
            PropietarioConsultaPuerto propietarioConsultaPuerto,
            Clock reloj) {

        this.restaurantePersistenciaPuerto =
                restaurantePersistenciaPuerto;
        this.propietarioConsultaPuerto =
                propietarioConsultaPuerto;
        this.reloj = reloj;
    }

    @Override
    public Restaurante crear(Restaurante restaurante) {
        normalizarDatos(restaurante);
        validarNombre(restaurante.getNombre());
        validarNitDuplicado(restaurante.getNit());

        Propietario propietario = obtenerPropietario(
                restaurante.getPropietarioId()
        );

        validarPerfilPropietario(propietario);

        LocalDateTime fechaActual =
                LocalDateTime.now(reloj);

        restaurante.setActivo(Boolean.TRUE);
        restaurante.setFechaCreacion(fechaActual);
        restaurante.setFechaActualizacion(fechaActual);

        return restaurantePersistenciaPuerto.guardar(
                restaurante
        );
    }

    private void normalizarDatos(Restaurante restaurante) {
        if (restaurante.getNombre() != null) {
            restaurante.setNombre(
                    restaurante.getNombre().trim()
            );
        }

        if (restaurante.getNit() != null) {
            restaurante.setNit(
                    restaurante.getNit().trim()
            );
        }

        if (restaurante.getDireccion() != null) {
            restaurante.setDireccion(
                    restaurante.getDireccion().trim()
            );
        }

        if (restaurante.getCelular() != null) {
            restaurante.setCelular(
                    restaurante.getCelular().trim()
            );
        }

        if (restaurante.getUrlLogo() != null) {
            restaurante.setUrlLogo(
                    restaurante.getUrlLogo().trim()
            );
        }
    }

    private void validarNombre(String nombre) {
        if (nombre.matches(
                ConstantesValidacion
                        .EXPRESION_NOMBRE_SOLO_NUMEROS
        )) {
            throw new ExcepcionPlazoleta(
                    CodigosError.NOMBRE_SOLO_NUMEROS,
                    MensajesError.NOMBRE_SOLO_NUMEROS,
                    CategoriaError.VALIDACION
            );
        }
    }

    private void validarNitDuplicado(String nit) {
        if (restaurantePersistenciaPuerto.existePorNit(nit)) {
            throw new ExcepcionPlazoleta(
                    CodigosError.NIT_YA_REGISTRADO,
                    MensajesError.NIT_YA_REGISTRADO,
                    CategoriaError.CONFLICTO
            );
        }
    }

    private Propietario obtenerPropietario(
            Long propietarioId) {

        return propietarioConsultaPuerto
                .buscarPorId(propietarioId)
                .orElseThrow(() -> new ExcepcionPlazoleta(
                        CodigosError.PROPIETARIO_NO_ENCONTRADO,
                        MensajesError.PROPIETARIO_NO_ENCONTRADO,
                        CategoriaError.NO_ENCONTRADO
                ));
    }

    private void validarPerfilPropietario(
            Propietario propietario) {

        boolean perfilValido =
                PerfilUsuario.PROPIETARIO.equals(
                        propietario.getPerfil()
                );

        if (!perfilValido
                || !Boolean.TRUE.equals(propietario.getActivo())) {

            throw new ExcepcionPlazoleta(
                    CodigosError.USUARIO_NO_ES_PROPIETARIO,
                    MensajesError.USUARIO_NO_ES_PROPIETARIO,
                    CategoriaError.VALIDACION
            );
        }
    }
}