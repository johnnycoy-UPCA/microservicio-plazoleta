package co.edu.clase.microservicio_plazoleta.dominio.excepcion;

import co.edu.clase.microservicio_plazoleta.util.enumeraciones.CategoriaError;

import java.util.Collections;
import java.util.Map;

public class ExcepcionPlazoleta extends RuntimeException {

    private final String codigo;
    private final CategoriaError categoria;
    private final Map<String, Object> detalles;

    public ExcepcionPlazoleta(
            String codigo,
            String mensaje,
            CategoriaError categoria) {

        super(mensaje);
        this.codigo = codigo;
        this.categoria = categoria;
        this.detalles = Collections.emptyMap();
    }

    public ExcepcionPlazoleta(
            String codigo,
            String mensaje,
            CategoriaError categoria,
            Map<String, Object> detalles) {

        super(mensaje);
        this.codigo = codigo;
        this.categoria = categoria;
        this.detalles = detalles == null
                ? Collections.emptyMap()
                : Map.copyOf(detalles);
    }

    public ExcepcionPlazoleta(
            String codigo,
            String mensaje,
            CategoriaError categoria,
            Throwable causa) {

        super(mensaje, causa);
        this.codigo = codigo;
        this.categoria = categoria;
        this.detalles = Collections.emptyMap();
    }

    public String getCodigo() {
        return codigo;
    }

    public CategoriaError getCategoria() {
        return categoria;
    }

    public Map<String, Object> getDetalles() {
        return detalles;
    }
}