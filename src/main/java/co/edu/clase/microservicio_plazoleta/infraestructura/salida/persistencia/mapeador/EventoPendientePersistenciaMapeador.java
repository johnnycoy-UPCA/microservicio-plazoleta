package co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.mapeador;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.EventoPendiente;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.entidad.EventoPendienteEntidad;
import org.springframework.stereotype.Component;

@Component
public class EventoPendientePersistenciaMapeador {

    public EventoPendienteEntidad aEntidad(
            EventoPendiente modelo) {

        if (modelo == null) {
            return null;
        }

        EventoPendienteEntidad entidad =
                new EventoPendienteEntidad();

        entidad.setId(modelo.getId());
        entidad.setTipoEvento(
                modelo.getTipoEvento()
        );
        entidad.setAgregadoId(
                modelo.getAgregadoId()
        );
        entidad.setCargaJson(
                modelo.getCargaJson()
        );
        entidad.setEstadoEnvio(
                modelo.getEstadoEnvio()
        );
        entidad.setIntentos(modelo.getIntentos());
        entidad.setProximoIntento(
                modelo.getProximoIntento()
        );
        entidad.setFechaCreacion(
                modelo.getFechaCreacion()
        );
        entidad.setFechaEnvio(
                modelo.getFechaEnvio()
        );
        entidad.setUltimoError(
                modelo.getUltimoError()
        );
        entidad.setVersion(modelo.getVersion());

        return entidad;
    }

    public EventoPendiente aModelo(
            EventoPendienteEntidad entidad) {

        if (entidad == null) {
            return null;
        }

        EventoPendiente modelo =
                new EventoPendiente();

        modelo.setId(entidad.getId());
        modelo.setTipoEvento(
                entidad.getTipoEvento()
        );
        modelo.setAgregadoId(
                entidad.getAgregadoId()
        );
        modelo.setCargaJson(
                entidad.getCargaJson()
        );
        modelo.setEstadoEnvio(
                entidad.getEstadoEnvio()
        );
        modelo.setIntentos(entidad.getIntentos());
        modelo.setProximoIntento(
                entidad.getProximoIntento()
        );
        modelo.setFechaCreacion(
                entidad.getFechaCreacion()
        );
        modelo.setFechaEnvio(
                entidad.getFechaEnvio()
        );
        modelo.setUltimoError(
                entidad.getUltimoError()
        );
        modelo.setVersion(entidad.getVersion());

        return modelo;
    }
}