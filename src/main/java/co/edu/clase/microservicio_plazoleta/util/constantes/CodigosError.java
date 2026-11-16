package co.edu.clase.microservicio_plazoleta.util.constantes;

public final class CodigosError {

    public static final String NIT_YA_REGISTRADO = "RST-001";
    public static final String NOMBRE_SOLO_NUMEROS = "RST-002";
    public static final String PROPIETARIO_NO_ENCONTRADO = "RST-003";
    public static final String USUARIO_NO_ES_PROPIETARIO = "RST-004";
    public static final String SERVICIO_USUARIOS_NO_DISPONIBLE = "RST-005";

    public static final String SOLICITUD_INVALIDA = "VAL-001";
    public static final String CUERPO_SOLICITUD_INVALIDO = "VAL-002";

    public static final String ERROR_INTERNO = "GEN-001";

    public static final String ACCESO_DENEGADO = "SEG-001";
    public static final String AUTENTICACION_REQUERIDA = "SEG-002";
    public static final String TOKEN_INVALIDO = "SEG-003";
    public static final String TOKEN_EXPIRADO = "SEG-004";

    private CodigosError() {
        throw new IllegalStateException(
                "No se permite crear instancias de una clase de constantes"
        );
    }
}