package co.edu.clase.microservicio_plazoleta.dominio.modelo;

public class NotificacionPedido {

    private Long pedidoId;
    private String celular;
    private String mensaje;

    public NotificacionPedido() {
    }

    public NotificacionPedido(
            Long pedidoId,
            String celular,
            String mensaje) {

        this.pedidoId = pedidoId;
        this.celular = celular;
        this.mensaje = mensaje;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public String getCelular() {
        return celular;
    }

    public String getMensaje() {
        return mensaje;
    }
}