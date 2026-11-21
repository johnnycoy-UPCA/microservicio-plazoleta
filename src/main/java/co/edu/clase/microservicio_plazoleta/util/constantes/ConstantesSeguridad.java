package co.edu.clase.microservicio_plazoleta.util.constantes;

public final class ConstantesSeguridad {

    public static final String ENCABEZADO_AUTORIZACION = "Authorization";
    public static final String PREFIJO_TOKEN = "Bearer ";
    public static final String RECLAMO_USUARIO_ID = "usuarioId";
    public static final String RECLAMO_PERFIL = "perfil";
    public static final String EMISOR_TOKEN = "microservicio-usuarios";
    public static final String PREFIJO_AUTORIDAD = "ROLE_";
    public static final String ENCABEZADO_API_KEY_INTERNA = "X-Internal-Api-Key";

    private ConstantesSeguridad() {
        throw new IllegalStateException(
                "No se permite crear instancias de una clase de constantes"
        );
    }
}