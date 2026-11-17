package co.edu.clase.microservicio_plazoleta.util.constantes;

public final class ConstantesApi {

    public static final String API_RESTAURANTES = "/api/v1/restaurantes";

    public static final String CONSULTA_USUARIO_POR_ID = "/api/v1/usuarios/{usuarioId}/validacion";

	public static final String API_PLATOS = "/api/v1/platos";

    private ConstantesApi() {
        throw new IllegalStateException(
                "No se permite crear instancias de una clase de constantes"
        );
    }
}