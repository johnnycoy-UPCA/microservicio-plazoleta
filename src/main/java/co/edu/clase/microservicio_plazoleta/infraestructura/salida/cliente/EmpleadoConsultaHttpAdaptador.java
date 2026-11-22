package co.edu.clase.microservicio_plazoleta.infraestructura.salida.cliente;

import co.edu.clase.microservicio_plazoleta.dominio.excepcion.ExcepcionPlazoleta;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.EmpleadoValidacion;
import co.edu.clase.microservicio_plazoleta.dominio.spi.EmpleadoConsultaPuerto;
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
public class EmpleadoConsultaHttpAdaptador
        implements EmpleadoConsultaPuerto {

    private final RestClient clienteUsuarios;

    public EmpleadoConsultaHttpAdaptador(
            @Qualifier("clienteUsuarios")
            RestClient clienteUsuarios) {

        this.clienteUsuarios = clienteUsuarios;
    }

    @Override
    public EmpleadoValidacion validar(
            Long usuarioId,
            String token) {

        try {
            EmpleadoValidacionRespuesta respuesta =
                    clienteUsuarios.get()
                            .uri(
                                    ConstantesApi
                                            .VALIDACION_EMPLEADO_USUARIOS,
                                    usuarioId
                            )
                            .header(
                                    ConstantesSeguridad
                                            .ENCABEZADO_AUTORIZACION,
                                    ConstantesSeguridad
                                            .PREFIJO_TOKEN
                                            + token
                            )
                            .retrieve()
                            .body(
                                    EmpleadoValidacionRespuesta
                                            .class
                            );

            if (respuesta == null
                    || respuesta.getRestauranteId() == null
                    || !Boolean.TRUE.equals(
                    respuesta.getActivo()
            )) {
                throw crearExcepcionEmpleado();
            }

            return new EmpleadoValidacion(
                    respuesta.getUsuarioId(),
                    respuesta.getPerfil(),
                    respuesta.getRestauranteId(),
                    respuesta.getActivo()
            );
        } catch (ExcepcionPlazoleta excepcion) {
            throw excepcion;
        } catch (RestClientException excepcion) {
            throw new ExcepcionPlazoleta(
                    CodigosError
                            .SERVICIO_USUARIOS_NO_DISPONIBLE,
                    MensajesError
                            .SERVICIO_USUARIOS_NO_DISPONIBLE,
                    CategoriaError.DEPENDENCIA_EXTERNA,
                    excepcion
            );
        }
    }

    private ExcepcionPlazoleta
            crearExcepcionEmpleado() {

        return new ExcepcionPlazoleta(
                CodigosError.EMPLEADO_NO_VALIDO,
                MensajesError.EMPLEADO_NO_VALIDO,
                CategoriaError.ACCESO_DENEGADO
        );
    }
}