package co.edu.clase.microservicio_plazoleta.dominio.casouso;

import co.edu.clase.microservicio_plazoleta.dominio.api.ListarPlatosServicioPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.excepcion.ExcepcionPlazoleta;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pagina;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Plato;
import co.edu.clase.microservicio_plazoleta.dominio.spi.PlatoPersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.RestaurantePersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.util.constantes.CodigosError;
import co.edu.clase.microservicio_plazoleta.util.constantes.ConstantesValidacion;
import co.edu.clase.microservicio_plazoleta.util.constantes.MensajesError;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.CategoriaError;

public class ListarPlatosCasoUso
        implements ListarPlatosServicioPuerto {

    private final PlatoPersistenciaPuerto
            platoPersistenciaPuerto;

    private final RestaurantePersistenciaPuerto
            restaurantePersistenciaPuerto;

    public ListarPlatosCasoUso(
            PlatoPersistenciaPuerto platoPersistenciaPuerto,
            RestaurantePersistenciaPuerto
                    restaurantePersistenciaPuerto) {

        this.platoPersistenciaPuerto =
                platoPersistenciaPuerto;

        this.restaurantePersistenciaPuerto =
                restaurantePersistenciaPuerto;
    }

    @Override
    public Pagina<Plato> listar(
            Long restauranteId,
            Long categoriaId,
            int pagina,
            int tamano) {

        validarPagina(pagina);
        validarTamano(tamano);
        validarRestaurante(restauranteId);

        return platoPersistenciaPuerto
                .listarActivosPorRestaurante(
                        restauranteId,
                        categoriaId,
                        pagina,
                        tamano
                );
    }

    private void validarPagina(int pagina) {
        if (pagina
                < ConstantesValidacion.PAGINA_INICIAL) {

            throw new ExcepcionPlazoleta(
                    CodigosError.PAGINA_INVALIDA,
                    MensajesError.PAGINA_INVALIDA,
                    CategoriaError.VALIDACION
            );
        }
    }

    private void validarTamano(int tamano) {
        boolean invalido =
                tamano
                        < ConstantesValidacion
                        .TAMANO_PAGINA_MINIMO
                        || tamano
                        > ConstantesValidacion
                        .TAMANO_PAGINA_MAXIMO;

        if (invalido) {
            throw new ExcepcionPlazoleta(
                    CodigosError.TAMANO_PAGINA_INVALIDO,
                    MensajesError.TAMANO_PAGINA_INVALIDO,
                    CategoriaError.VALIDACION
            );
        }
    }

    private void validarRestaurante(
            Long restauranteId) {

        restaurantePersistenciaPuerto
                .buscarPorId(restauranteId)
                .orElseThrow(() -> new ExcepcionPlazoleta(
                        CodigosError.RESTAURANTE_NO_ENCONTRADO,
                        MensajesError.RESTAURANTE_NO_ENCONTRADO,
                        CategoriaError.NO_ENCONTRADO
                ));
    }
}