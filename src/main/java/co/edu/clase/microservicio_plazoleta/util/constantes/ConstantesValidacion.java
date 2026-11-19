package co.edu.clase.microservicio_plazoleta.util.constantes;

public final class ConstantesValidacion {

    public static final int LONGITUD_MAXIMA_NOMBRE_RESTAURANTE = 100;
    public static final int LONGITUD_MAXIMA_NIT = 20;
    public static final int LONGITUD_MAXIMA_DIRECCION = 150;
    public static final int LONGITUD_MAXIMA_CELULAR = 13;
    public static final int LONGITUD_MAXIMA_URL_LOGO = 500;
	public static final int LONGITUD_MAXIMA_NOMBRE_PLATO = 100;
	public static final int LONGITUD_MAXIMA_DESCRIPCION_PLATO = 300;
	public static final int LONGITUD_MAXIMA_URL_IMAGEN = 500;
    public static final int PAGINA_INICIAL = 0;
    public static final int TAMANO_PAGINA_PREDETERMINADO = 10;
    public static final int TAMANO_PAGINA_MINIMO = 1;
    public static final int TAMANO_PAGINA_MAXIMO = 100;
    public static final int CANTIDAD_MINIMA_PLATO_PEDIDO = 1;
    public static final int CANTIDAD_MAXIMA_PLATO_PEDIDO = 100;
    public static final int MAXIMO_LINEAS_PEDIDO = 50;
    
    public static final String EXPRESION_NIT = "^[0-9]+$";
    public static final String EXPRESION_CELULAR = "^\\+?[0-9]{1,12}$";
    public static final String EXPRESION_NOMBRE_SOLO_NUMEROS = "^[0-9]+$";

    private ConstantesValidacion() {
        throw new IllegalStateException(
                "No se permite crear instancias de una clase de constantes"
        );
    }
}