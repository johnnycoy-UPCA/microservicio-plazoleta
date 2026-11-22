package co.edu.clase.microservicio_plazoleta.infraestructura.salida.cliente;

import co.edu.clase.microservicio_plazoleta.dominio.excepcion.ExcepcionPlazoleta;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.EventoTrazabilidad;
import co.edu.clase.microservicio_plazoleta.dominio.spi.TrazabilidadRegistroPuerto;
import co.edu.clase.microservicio_plazoleta.infraestructura.configuracion.TrazabilidadServicioPropiedades;
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
public class TrazabilidadRegistroHttpAdaptador
        implements TrazabilidadRegistroPuerto {

    private final RestClient clienteTrazabilidad;
    private final TrazabilidadServicioPropiedades propiedades;

    public TrazabilidadRegistroHttpAdaptador(
			@Qualifier("clienteTrazabilidad")
            RestClient clienteTrazabilidad,
            TrazabilidadServicioPropiedades
                    propiedades) {

        this.clienteTrazabilidad =
                clienteTrazabilidad;

        this.propiedades = propiedades;
    }

    @Override
    public void registrar(EventoTrazabilidad evento) {
        validarConfiguracion();

        RegistrarEventoTrazabilidadSolicitud solicitud =
                new RegistrarEventoTrazabilidadSolicitud(
                        evento.getPedidoId(),
                        evento.getClienteId(),
                        evento.getRestauranteId(),
                        evento.getEmpleadoId(),
                        evento.getEstadoAnterior(),
                        evento.getEstadoNuevo()
                );

        try {
            clienteTrazabilidad.post()
				.uri(
					ConstantesApi.REGISTRO_EVENTOS_TRAZABILIDAD
				)
				.header(
					ConstantesSeguridad.ENCABEZADO_API_KEY_INTERNA,
					propiedades.getApiKey()
				)
				.body(solicitud)
				.exchange(
					(peticion, respuesta) -> {
						if (respuesta.getStatusCode()
								.is2xxSuccessful()
								|| respuesta.getStatusCode()
								.value() == 409
							) {

							return null;
						}

						if (respuesta.getStatusCode()
								.value() == 400
								// || respuesta.getStatusCode()
								// .value() == 409
							) {

							throw new ExcepcionPlazoleta(
									CodigosError.EVENTO_TRAZABILIDAD_RECHAZADO,
									MensajesError.EVENTO_TRAZABILIDAD_RECHAZADO,
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
                    CodigosError.TRAZABILIDAD_NO_DISPONIBLE,
                    MensajesError.TRAZABILIDAD_NO_DISPONIBLE,
                    CategoriaError.DEPENDENCIA_EXTERNA,
                    excepcion
            );
        }
    }

    private void validarConfiguracion() {
        if (propiedades.getApiKey() == null
                || propiedades.getApiKey().isBlank()) {

            throw crearExcepcionDependencia();
        }
    }

    private ExcepcionPlazoleta
            crearExcepcionDependencia() {

        return new ExcepcionPlazoleta(
                CodigosError.TRAZABILIDAD_NO_DISPONIBLE,
                MensajesError.TRAZABILIDAD_NO_DISPONIBLE,
                CategoriaError.DEPENDENCIA_EXTERNA
        );
    }
}