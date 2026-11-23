package co.edu.clase.microservicio_plazoleta.infraestructura.salida.cliente;

import co.edu.clase.microservicio_plazoleta.dominio.excepcion.ExcepcionPlazoleta;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.ContactoCliente;
import co.edu.clase.microservicio_plazoleta.dominio.spi.ClienteContactoConsultaPuerto;
import co.edu.clase.microservicio_plazoleta.infraestructura.configuracion.UsuariosServicioPropiedades;
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
public class ClienteContactoConsultaHttpAdaptador
        implements ClienteContactoConsultaPuerto {

    private final RestClient clienteUsuarios;

    private final UsuariosServicioPropiedades
            propiedades;

    public ClienteContactoConsultaHttpAdaptador(
            @Qualifier("clienteUsuarios")
            RestClient clienteUsuarios,
            UsuariosServicioPropiedades propiedades) {

        this.clienteUsuarios = clienteUsuarios;
        this.propiedades = propiedades;
    }

    @Override
    public ContactoCliente consultar(
            Long clienteId) {

        try {
            ContactoClienteRespuesta respuesta =
                    clienteUsuarios.get()
                            .uri(
                                    ConstantesApi.CONTACTO_CLIENTE_USUARIOS,
                                    clienteId
                            )
                            .header(
                                    ConstantesSeguridad.ENCABEZADO_API_KEY_INTERNA,
                                    propiedades.getApiKey()
                            )
                            .retrieve()
                            .body(
                                    ContactoClienteRespuesta.class
                            );

            if (respuesta == null
                    || respuesta.getCelular() == null
                    || respuesta.getCelular().isBlank()
                    || !Boolean.TRUE.equals(
                    respuesta.getActivo()
            )) {
                throw crearExcepcionContacto();
            }

            return new ContactoCliente(
                    respuesta.getUsuarioId(),
                    respuesta.getCelular(),
                    respuesta.getActivo()
            );
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

    private ExcepcionPlazoleta
            crearExcepcionContacto() {

        return new ExcepcionPlazoleta(
                CodigosError.CONTACTO_CLIENTE_NO_DISPONIBLE,
                MensajesError.CONTACTO_CLIENTE_NO_DISPONIBLE,
                CategoriaError.VALIDACION
        );
    }
}