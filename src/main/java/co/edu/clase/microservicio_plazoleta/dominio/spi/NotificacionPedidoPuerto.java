package co.edu.clase.microservicio_plazoleta.dominio.spi;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.NotificacionPedido;

public interface NotificacionPedidoPuerto {

    void notificar(NotificacionPedido notificacion);
}