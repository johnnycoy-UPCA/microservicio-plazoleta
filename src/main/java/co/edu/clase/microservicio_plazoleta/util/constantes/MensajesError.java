package co.edu.clase.microservicio_plazoleta.util.constantes;

public final class MensajesError {

    public static final String NIT_YA_REGISTRADO =
            "El NIT ingresado ya se encuentra registrado";

    public static final String NOMBRE_SOLO_NUMEROS =
            "El nombre del restaurante no puede contener únicamente números";

    public static final String PROPIETARIO_NO_ENCONTRADO =
            "El propietario solicitado no fue encontrado";

    public static final String USUARIO_NO_ES_PROPIETARIO =
            "El usuario indicado no tiene perfil PROPIETARIO";

    public static final String SERVICIO_USUARIOS_NO_DISPONIBLE =
            "No fue posible validar el propietario en el Microservicio Usuarios";

    public static final String SOLICITUD_INVALIDA =
            "La solicitud contiene campos inválidos";

    public static final String CUERPO_SOLICITUD_INVALIDO =
            "El cuerpo de la solicitud no tiene un formato válido";

    public static final String ERROR_INTERNO =
            "Ocurrió un error interno al procesar la solicitud";

	public static final String NOMBRE_OBLIGATORIO =
			"El nombre del restaurante es obligatorio";

	public static final String NOMBRE_LONGITUD_INVALIDA =
			"El nombre del restaurante no puede superar los 100 caracteres";

	public static final String NIT_OBLIGATORIO =
			"El NIT es obligatorio";

	public static final String NIT_FORMATO_INVALIDO =
			"El NIT debe contener únicamente números";

	public static final String NIT_LONGITUD_INVALIDA =
			"El NIT no puede superar los 20 caracteres";

	public static final String DIRECCION_OBLIGATORIA =
			"La dirección es obligatoria";

	public static final String DIRECCION_LONGITUD_INVALIDA =
			"La dirección no puede superar los 150 caracteres";

	public static final String CELULAR_OBLIGATORIO =
			"El celular es obligatorio";

	public static final String CELULAR_FORMATO_INVALIDO =
			"El celular debe contener números y puede iniciar con el signo más";

	public static final String URL_LOGO_OBLIGATORIA =
			"La URL del logotipo es obligatoria";

	public static final String URL_LOGO_INVALIDA =
			"La URL del logotipo no tiene un formato válido";

	public static final String PROPIETARIO_OBLIGATORIO =
			"El identificador del propietario es obligatorio";

	public static final String ACCESO_DENEGADO =
			"El usuario autenticado no tiene permisos para realizar esta operación";

	public static final String AUTENTICACION_REQUERIDA =
			"La solicitud requiere autenticación";

	public static final String TOKEN_INVALIDO =
			"El token de autenticación no es válido";

	public static final String TOKEN_EXPIRADO =
			"El token de autenticación ha expirado";

	public static final String RESTAURANTE_NO_ENCONTRADO =
			"El restaurante solicitado no fue encontrado";

	public static final String RESTAURANTE_INACTIVO =
			"El restaurante se encuentra inactivo";

	public static final String RESTAURANTE_NO_PERTENECE_PROPIETARIO =
			"El restaurante no pertenece al propietario autenticado";

	public static final String CATEGORIA_NO_ENCONTRADA =
			"La categoría solicitada no fue encontrada";

	public static final String CATEGORIA_INACTIVA =
			"La categoría seleccionada se encuentra inactiva";

	public static final String PRECIO_PLATO_INVALIDO =
			"El precio del plato debe ser un entero mayor que cero";

	public static final String NOMBRE_PLATO_OBLIGATORIO =
			"El nombre del plato es obligatorio";

	public static final String NOMBRE_PLATO_LONGITUD_INVALIDA =
			"El nombre del plato no puede superar los 100 caracteres";

	public static final String DESCRIPCION_PLATO_OBLIGATORIA =
			"La descripción del plato es obligatoria";

	public static final String DESCRIPCION_PLATO_LONGITUD_INVALIDA =
			"La descripción del plato no puede superar los 300 caracteres";

	public static final String PRECIO_PLATO_OBLIGATORIO =
			"El precio del plato es obligatorio";

	public static final String URL_IMAGEN_OBLIGATORIA =
			"La URL de la imagen es obligatoria";

	public static final String URL_IMAGEN_INVALIDA =
			"La URL de la imagen no tiene un formato válido";

	public static final String CATEGORIA_OBLIGATORIA =
			"El identificador de la categoría es obligatorio";

	public static final String RESTAURANTE_OBLIGATORIO =
			"El identificador del restaurante es obligatorio";
			
    private MensajesError() {
        throw new IllegalStateException(
			"No se permite crear instancias de una clase de constantes"
        );
    }
}