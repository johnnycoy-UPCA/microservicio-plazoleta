package co.edu.clase.microservicio_plazoleta.util.constantes;

public final class CodigosError {

    public static final String NIT_YA_REGISTRADO = "RST-001";
    public static final String NOMBRE_SOLO_NUMEROS = "RST-002";
    public static final String PROPIETARIO_NO_ENCONTRADO = "RST-003";
    public static final String USUARIO_NO_ES_PROPIETARIO = "RST-004";
    public static final String SERVICIO_USUARIOS_NO_DISPONIBLE = "RST-005";
    public static final String RESTAURANTE_NO_ENCONTRADO = "RST-006";
    public static final String RESTAURANTE_INACTIVO = "RST-007";
    public static final String RESTAURANTE_NO_PERTENECE_PROPIETARIO = "RST-008";

    public static final String CATEGORIA_NO_ENCONTRADA = "CAT-001";
    public static final String CATEGORIA_INACTIVA = "CAT-002";

    public static final String PRECIO_PLATO_INVALIDO = "PLT-001";
    public static final String PLATO_NO_ENCONTRADO = "PLT-002";
    public static final String ESTADO_PLATO_OBLIGATORIO = "PLT-003";

    public static final String SOLICITUD_INVALIDA = "VAL-001";
    public static final String CUERPO_SOLICITUD_INVALIDO = "VAL-002";

    public static final String ERROR_INTERNO = "GEN-001";

    public static final String ACCESO_DENEGADO = "SEG-001";
    public static final String AUTENTICACION_REQUERIDA = "SEG-002";
    public static final String TOKEN_INVALIDO = "SEG-003";
    public static final String TOKEN_EXPIRADO = "SEG-004";

    public static final String PAGINA_INVALIDA = "PAG-001";
    public static final String TAMANO_PAGINA_INVALIDO = "PAG-002";

    public static final String PEDIDO_VACIO = "PED-001";
    public static final String PEDIDO_ACTIVO_EXISTENTE = "PED-002";
    public static final String PLATO_PEDIDO_INVALIDO = "PED-003";
    public static final String PLATO_REPETIDO = "PED-004";
    public static final String CANTIDAD_PLATO_INVALIDA = "PED-005";
    public static final String LIMITE_LINEAS_PEDIDO_EXCEDIDO = "PED-006";
    public static final String ESTADO_PEDIDO_OBLIGATORIO = "PED-007";

    public static final String TRAZABILIDAD_NO_DISPONIBLE = "TRZ-001";
    public static final String EVENTO_TRAZABILIDAD_RECHAZADO = "TRZ-002";

    public static final String EVENTO_NO_SERIALIZABLE = "OUT-001";
    public static final String EVENTO_PENDIENTE_NO_ENCONTRADO = "OUT-002";

    public static final String EMPLEADO_NO_VALIDO = "EMP-001";
    // public static final String SERVICIO_USUARIOS_NO_DISPONIBLE = "EMP-002";

    private CodigosError() {
        throw new IllegalStateException(
                "No se permite crear instancias de una clase de constantes"
        );
    }
}