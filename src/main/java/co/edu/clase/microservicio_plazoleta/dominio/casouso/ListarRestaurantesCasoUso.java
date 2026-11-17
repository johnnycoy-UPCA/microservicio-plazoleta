package co.edu.clase.microservicio_plazoleta.dominio.casouso;

import co.edu.clase.microservicio_plazoleta.dominio.api.ListarRestaurantesServicioPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.excepcion.ExcepcionPlazoleta;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pagina;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Restaurante;
import co.edu.clase.microservicio_plazoleta.dominio.spi.RestaurantePersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.util.constantes.CodigosError;
import co.edu.clase.microservicio_plazoleta.util.constantes.ConstantesValidacion;
import co.edu.clase.microservicio_plazoleta.util.constantes.MensajesError;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.CategoriaError;

public class ListarRestaurantesCasoUso
        implements ListarRestaurantesServicioPuerto {

    private final RestaurantePersistenciaPuerto
            restaurantePersistenciaPuerto;

    public ListarRestaurantesCasoUso(
            RestaurantePersistenciaPuerto
                    restaurantePersistenciaPuerto) {

        this.restaurantePersistenciaPuerto =
                restaurantePersistenciaPuerto;
    }

    @Override
    public Pagina<Restaurante> listar(
            int pagina,
            int tamano) {

        validarPagina(pagina);
        validarTamano(tamano);

        return restaurantePersistenciaPuerto
                .listarActivos(pagina, tamano);
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
        boolean tamanoInvalido =
                tamano
                        < ConstantesValidacion
                        .TAMANO_PAGINA_MINIMO
                        || tamano
                        > ConstantesValidacion
                        .TAMANO_PAGINA_MAXIMO;

        if (tamanoInvalido) {
            throw new ExcepcionPlazoleta(
                    CodigosError.TAMANO_PAGINA_INVALIDO,
                    MensajesError.TAMANO_PAGINA_INVALIDO,
                    CategoriaError.VALIDACION
            );
        }
    }
}