package co.edu.clase.microservicio_plazoleta.infraestructura.salida.cliente;

import co.edu.clase.microservicio_plazoleta.dominio.excepcion.ExcepcionPlazoleta;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Propietario;
import co.edu.clase.microservicio_plazoleta.dominio.spi.PropietarioConsultaPuerto;
import co.edu.clase.microservicio_plazoleta.infraestructura.entrada.rest.seguridad.UsuarioAutenticado;
import co.edu.clase.microservicio_plazoleta.util.constantes.CodigosError;
import co.edu.clase.microservicio_plazoleta.util.constantes.ConstantesApi;
import co.edu.clase.microservicio_plazoleta.util.constantes.ConstantesSeguridad;
import co.edu.clase.microservicio_plazoleta.util.constantes.MensajesError;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.CategoriaError;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

@Component
public class PropietarioConsultaHttpAdaptador
	implements PropietarioConsultaPuerto {

    private final RestClient clienteUsuarios;

    public PropietarioConsultaHttpAdaptador(
		RestClient clienteUsuarios) {

        this.clienteUsuarios = clienteUsuarios;
    }

    @Override
    public Optional<Propietario> buscarPorId(
		Long propietarioId) {

        try {
            // UsuarioConsultaRespuesta respuesta =
			// 	clienteUsuarios.get()
			// 		.uri(
			// 			ConstantesApi.CONSULTA_USUARIO_POR_ID,
			// 			propietarioId
			// 		)
			// 		.retrieve()
			// 		.onStatus(
			// 			estado -> estado.value() == 404,
			// 			(solicitud, respuestaHttp) -> {
			// 			}
			// 		)
			// 		.body(
			// 			UsuarioConsultaRespuesta.class
			// 		);

            // if (respuesta == null) {
            //     return Optional.empty();
            // }

            // return Optional.of(new Propietario(
			// 	respuesta.getId(),
			// 	respuesta.getPerfil(),
			// 	respuesta.getActivo()
            // ));
			return clienteUsuarios.get()
					.uri(
						ConstantesApi.CONSULTA_USUARIO_POR_ID,
						propietarioId
					)
					.header(
						ConstantesSeguridad.ENCABEZADO_AUTORIZACION,
						ConstantesSeguridad.PREFIJO_TOKEN
							+ obtenerTokenActual()
					)
					.exchange((solicitud, respuesta) -> {
						if (respuesta.getStatusCode().value() == 404) {
							return Optional.empty();
						}

						if (respuesta.getStatusCode().isError()) {
							throw new ExcepcionPlazoleta(
								CodigosError.SERVICIO_USUARIOS_NO_DISPONIBLE,
								MensajesError.SERVICIO_USUARIOS_NO_DISPONIBLE,
								CategoriaError.DEPENDENCIA_EXTERNA
							);
						}

						UsuarioConsultaRespuesta usuario =
								respuesta.bodyTo(UsuarioConsultaRespuesta.class);

						if (usuario == null) {
							return Optional.empty();
						}

						return Optional.of(new Propietario(
							usuario.getId(),
							usuario.getPerfil(),
							usuario.getActivo()
						));
					});
        } catch (ExcepcionPlazoleta excepcion) {
			throw excepcion;
		} catch (RestClientException excepcion) {
            throw new ExcepcionPlazoleta(
				CodigosError.SERVICIO_USUARIOS_NO_DISPONIBLE,
				MensajesError.SERVICIO_USUARIOS_NO_DISPONIBLE,
				CategoriaError.DEPENDENCIA_EXTERNA,
				excepcion
            );
        }
    }

	private String obtenerTokenActual() {
		Authentication autenticacion =
				SecurityContextHolder
						.getContext()
						.getAuthentication();

		if (autenticacion == null
				|| !(autenticacion.getPrincipal()
				instanceof UsuarioAutenticado usuario)) {

			throw new ExcepcionPlazoleta(
					CodigosError.AUTENTICACION_REQUERIDA,
					MensajesError.AUTENTICACION_REQUERIDA,
					CategoriaError.NO_AUTORIZADO
			);
		}

		return usuario.getToken();
	}
}