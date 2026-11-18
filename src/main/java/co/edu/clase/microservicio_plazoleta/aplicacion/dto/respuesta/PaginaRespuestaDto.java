package co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta;

import java.util.List;

public class PaginaRespuestaDto<T> {

    private List<T> contenido;
    private int paginaActual;
    private int tamanoPagina;
    private long totalElementos;
    private int totalPaginas;
    private boolean primera;
    private boolean ultima;

    public PaginaRespuestaDto() {
    }

    public PaginaRespuestaDto(
            List<T> contenido,
            int paginaActual,
            int tamanoPagina,
            long totalElementos,
            int totalPaginas,
            boolean primera,
            boolean ultima) {

        this.contenido = contenido;
        this.paginaActual = paginaActual;
        this.tamanoPagina = tamanoPagina;
        this.totalElementos = totalElementos;
        this.totalPaginas = totalPaginas;
        this.primera = primera;
        this.ultima = ultima;
    }

    public List<T> getContenido() {
        return contenido;
    }

    public int getPaginaActual() {
        return paginaActual;
    }

    public int getTamanoPagina() {
        return tamanoPagina;
    }

    public long getTotalElementos() {
        return totalElementos;
    }

    public int getTotalPaginas() {
        return totalPaginas;
    }

    public boolean isPrimera() {
        return primera;
    }

    public boolean isUltima() {
        return ultima;
    }
}