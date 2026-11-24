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

	public static final String PAGINA_INVALIDA =
			"El número de página no puede ser negativo";

	public static final String TAMANO_PAGINA_INVALIDO =
			"El tamaño de página debe estar entre 1 y 100";

	public static final String PLATO_NO_ENCONTRADO =
			"El plato solicitado no fue encontrado";

	public static final String ESTADO_PLATO_OBLIGATORIO =
			"El estado del plato es obligatorio";

	public static final String PEDIDO_VACIO =
			"El pedido debe contener al menos un plato";

	public static final String PEDIDO_ACTIVO_EXISTENTE =
			"El cliente ya tiene un pedido en proceso";

	public static final String PLATO_PEDIDO_INVALIDO =
			"Uno de los platos no existe, está inactivo "
			+ "o no pertenece al restaurante seleccionado";

	public static final String PLATO_REPETIDO =
			"Un plato no puede aparecer más de una vez "
			+ "en el mismo pedido";

	public static final String CANTIDAD_PLATO_INVALIDA =
			"La cantidad de cada plato debe estar entre 1 y 100";

	public static final String LIMITE_LINEAS_PEDIDO_EXCEDIDO =
			"El pedido no puede contener más de 50 platos diferentes";

	public static final String DETALLES_PEDIDO_OBLIGATORIOS =
			"El pedido debe contener al menos un plato";

	public static final String PLATO_PEDIDO_OBLIGATORIO =
			"El identificador del plato es obligatorio";

	public static final String CANTIDAD_PEDIDO_OBLIGATORIA =
			"La cantidad del plato es obligatoria";

	public static final String TRAZABILIDAD_NO_DISPONIBLE =
			"No fue posible registrar el evento "
			+ "en el Microservicio Trazabilidad";

	public static final String EVENTO_TRAZABILIDAD_RECHAZADO =
			"El Microservicio Trazabilidad rechazó el evento";

	public static final String EVENTO_NO_SERIALIZABLE =
			"No fue posible preparar el evento "
			+ "de trazabilidad";

	public static final String EVENTO_PENDIENTE_NO_ENCONTRADO =
			"El evento pendiente no fue encontrado";

	public static final String EMPLEADO_NO_VALIDO =
			"El empleado no existe, está inactivo "
			+ "o no tiene restaurante asociado";

	// public static final String SERVICIO_USUARIOS_NO_DISPONIBLE =
	// 		"No fue posible validar el empleado "
	// 				+ "en el Microservicio Usuarios";

	public static final String ESTADO_PEDIDO_OBLIGATORIO =
			"El estado del pedido es obligatorio";

	public static final String PEDIDO_NO_ENCONTRADO =
			"El pedido solicitado no fue encontrado";

	public static final String PEDIDO_NO_PERTENECE_RESTAURANTE =
			"El pedido no pertenece al restaurante "
			+ "del empleado autenticado";

	public static final String PEDIDO_ESTADO_NO_ASIGNABLE =
			"Solo se pueden asignar pedidos "
			+ "en estado PENDIENTE";

	public static final String PEDIDO_YA_ASIGNADO =
			"El pedido ya fue asignado a un empleado";

	public static final String
			PEDIDO_MODIFICADO_CONCURRENTEMENTE =
			"El pedido fue modificado por otro empleado";

	public static final String CONTACTO_CLIENTE_NO_DISPONIBLE =
			"No fue posible obtener el contacto del cliente";

	public static final String SERVICIO_MENSAJERIA_NO_DISPONIBLE =
			"El Microservicio Mensajería "
			+ "no se encuentra disponible";

	public static final String NOTIFICACION_RECHAZADA =
			"El Microservicio Mensajería "
			+ "rechazó la notificación";

	public static final String PIN_NO_PROCESABLE =
			"No fue posible proteger el PIN de entrega";

	public static final String PEDIDO_ESTADO_NO_PREPARABLE =
			"Solo se pueden marcar como LISTO los pedidos "
			+ "en estado EN_PREPARACION";

	public static final String EMPLEADO_NO_ASIGNADO_AL_PEDIDO =
			"El pedido no está asignado al empleado autenticado";

	public static final String PEDIDO_NO_LISTO =
			"Solo se pueden entregar pedidos "
			+ "en estado LISTO";

	public static final String PIN_OBLIGATORIO =
			"El PIN de entrega es obligatorio";

	public static final String PIN_FORMATO_INVALIDO =
			"El PIN debe contener exactamente seis dígitos";

	public static final String PIN_NO_GENERADO =
			"El pedido no tiene un PIN de entrega válido";

	public static final String PIN_EXPIRADO =
			"El PIN de entrega ha expirado";

	public static final String PIN_INVALIDO =
			"El PIN de entrega no es válido";

	public static final String PIN_INTENTOS_AGOTADOS =
			"Se agotaron los intentos permitidos "
			+ "para validar el PIN";

    public static final String PEDIDO_NO_CANCELABLE =
            "Lo sentimos, tu pedido se encuentra en estado %s y no puede cancelarse";

    public static final String PEDIDO_NO_PERTENECE_CLIENTE =
            "El pedido no pertenece al cliente autenticado";

			
    private MensajesError() {
        throw new IllegalStateException(
			"No se permite crear instancias de una clase de constantes"
        );
    }
}