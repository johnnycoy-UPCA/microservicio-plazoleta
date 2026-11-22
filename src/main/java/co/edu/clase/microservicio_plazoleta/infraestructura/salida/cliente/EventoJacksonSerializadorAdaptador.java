package co.edu.clase.microservicio_plazoleta.infraestructura.salida.cliente;

import co.edu.clase.microservicio_plazoleta.dominio.excepcion.ExcepcionPlazoleta;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.EventoTrazabilidad;
import co.edu.clase.microservicio_plazoleta.dominio.spi.EventoSerializadorPuerto;
import co.edu.clase.microservicio_plazoleta.util.constantes.CodigosError;
import co.edu.clase.microservicio_plazoleta.util.constantes.MensajesError;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.CategoriaError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class EventoJacksonSerializadorAdaptador
        implements EventoSerializadorPuerto {

    private final ObjectMapper objectMapper;

    public EventoJacksonSerializadorAdaptador(
            ObjectMapper objectMapper) {

        this.objectMapper = objectMapper;
    }

    @Override
    public String serializar(
            EventoTrazabilidad evento) {

        try {
            return objectMapper
                    .writeValueAsString(evento);
        } catch (JsonProcessingException excepcion) {
            throw new ExcepcionPlazoleta(
                    CodigosError.EVENTO_NO_SERIALIZABLE,
                    MensajesError.EVENTO_NO_SERIALIZABLE,
                    CategoriaError.ERROR_INTERNO,
                    excepcion
            );
        }
    }

    @Override
    public EventoTrazabilidad deserializar(
            String cargaJson) {

        try {
            return objectMapper.readValue(
                    cargaJson,
                    EventoTrazabilidad.class
            );
        } catch (JsonProcessingException excepcion) {
            throw new ExcepcionPlazoleta(
                    CodigosError.EVENTO_NO_SERIALIZABLE,
                    MensajesError.EVENTO_NO_SERIALIZABLE,
                    CategoriaError.ERROR_INTERNO,
                    excepcion
            );
        }
    }
}