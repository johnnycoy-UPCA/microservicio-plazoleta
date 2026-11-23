package co.edu.clase.microservicio_plazoleta.util.constantes;

public final class ConstantesApi {

    public static final String API_RESTAURANTES = "/api/v1/restaurantes";
    public static final String CONSULTA_USUARIO_POR_ID = "/api/v1/usuarios/{usuarioId}/validacion";
	public static final String API_PLATOS = "/api/v1/platos";
    public static final String API_PEDIDOS = "/api/v1/pedidos";
    public static final String REGISTRO_EVENTOS_TRAZABILIDAD = "/api/v1/trazabilidad/eventos";
    public static final String VALIDACION_EMPLEADO_USUARIOS = "/api/v1/usuarios/{usuarioId}/validacion-empleado";
    public static final String CONTACTO_CLIENTE_USUARIOS = "/api/v1/usuarios/{usuarioId}/contacto";
    public static final String NOTIFICAR_PEDIDO_LISTO = "/api/v1/notificaciones/pedido-listo";

    private ConstantesApi() {
        throw new IllegalStateException(
                "No se permite crear instancias de una clase de constantes"
        );
    }
}