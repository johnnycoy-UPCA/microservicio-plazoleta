package co.edu.clase.microservicio_plazoleta.infraestructura.salida.cliente;

public class NotificarPedidoListoSolicitud {

    private Long pedidoId;
    private String celular;
    private String mensaje;

    public NotificarPedidoListoSolicitud() {
    }

    public NotificarPedidoListoSolicitud(
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