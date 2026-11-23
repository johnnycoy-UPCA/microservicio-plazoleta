package co.edu.clase.microservicio_plazoleta.infraestructura.salida.cliente;

import co.edu.clase.microservicio_plazoleta.dominio.excepcion.ExcepcionPlazoleta;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.NotificacionPedido;
import co.edu.clase.microservicio_plazoleta.dominio.spi.NotificacionPedidoPuerto;
import co.edu.clase.microservicio_plazoleta.infraestructura.configuracion.MensajeriaServicioPropiedades;
import co.edu.clase.microservicio_plazoleta.util.constantes.CodigosError;
import co.edu.clase.microservicio_plazoleta.util.constantes.ConstantesApi;
import co.edu.clase.microservicio_plazoleta.util.constantes.ConstantesSeguridad;
import co.edu.clase.microservicio_plazoleta.util.constantes.MensajesError;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.CategoriaError;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class NotificacionPedidoHttpAdaptador
        implements NotificacionPedidoPuerto {

    private final RestClient clienteMensajeria;

    private final MensajeriaServicioPropiedades
            propiedades;

    public NotificacionPedidoHttpAdaptador(
            @Qualifier("clienteMensajeria")
            RestClient clienteMensajeria,
            MensajeriaServicioPropiedades propiedades) {

        this.clienteMensajeria = clienteMensajeria;
        this.propiedades = propiedades;
    }

    @Override
    public void notificar(
            NotificacionPedido notificacion) {

        NotificarPedidoListoSolicitud solicitud =
                new NotificarPedidoListoSolicitud(
                        notificacion.getPedidoId(),
                        notificacion.getCelular(),
                        notificacion.getMensaje()
                );

        try {
            clienteMensajeria.post()
                    .uri(
                            ConstantesApi.NOTIFICAR_PEDIDO_LISTO
                    )
                    .header(
                            ConstantesSeguridad.ENCABEZADO_API_KEY_INTERNA,
                            propiedades.getApiKey()
                    )
                    .body(solicitud)
                    .exchange(
                            (peticion, respuesta) -> {
                                if (respuesta.getStatusCode()
                                        .value() == 202) {

                                    return null;
                                }

                                if (respuesta.getStatusCode()
                                        .is4xxClientError()) {

                                    throw new ExcepcionPlazoleta(
                                            CodigosError.NOTIFICACION_RECHAZADA,
                                            MensajesError.NOTIFICACION_RECHAZADA,
                                            CategoriaError.DEPENDENCIA_EXTERNA
                                    );
                                }

                                throw crearExcepcionDependencia();
                            }
                    );
        } catch (ExcepcionPlazoleta excepcion) {
            throw excepcion;
        } catch (RestClientException excepcion) {
            throw new ExcepcionPlazoleta(
                    CodigosError.SERVICIO_MENSAJERIA_NO_DISPONIBLE,
                    MensajesError.SERVICIO_MENSAJERIA_NO_DISPONIBLE,
                    CategoriaError.DEPENDENCIA_EXTERNA,
                    excepcion
            );
        }
    }

    private ExcepcionPlazoleta
            crearExcepcionDependencia() {

        return new ExcepcionPlazoleta(
                CodigosError.SERVICIO_MENSAJERIA_NO_DISPONIBLE,
                MensajesError.SERVICIO_MENSAJERIA_NO_DISPONIBLE,
                CategoriaError.DEPENDENCIA_EXTERNA
        );
    }
}