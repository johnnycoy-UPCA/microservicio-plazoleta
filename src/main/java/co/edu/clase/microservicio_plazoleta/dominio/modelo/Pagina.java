package co.edu.clase.microservicio_plazoleta.dominio.modelo;

import java.util.List;

public class Pagina<T> {

    private List<T> contenido;
    private int paginaActual;
    private int tamanoPagina;
    private long totalElementos;
    private int totalPaginas;
    private boolean primera;
    private boolean ultima;

    public Pagina() {
    }

    public Pagina(
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

    public void setContenido(List<T> contenido) {
        this.contenido = contenido;
    }

    public int getPaginaActual() {
        return paginaActual;
    }

    public void setPaginaActual(int paginaActual) {
        this.paginaActual = paginaActual;
    }

    public int getTamanoPagina() {
        return tamanoPagina;
    }

    public void setTamanoPagina(int tamanoPagina) {
        this.tamanoPagina = tamanoPagina;
    }

    public long getTotalElementos() {
        return totalElementos;
    }

    public void setTotalElementos(long totalElementos) {
        this.totalElementos = totalElementos;
    }

    public int getTotalPaginas() {
        return totalPaginas;
    }

    public void setTotalPaginas(int totalPaginas) {
        this.totalPaginas = totalPaginas;
    }

    public boolean isPrimera() {
        return primera;
    }

    public void setPrimera(boolean primera) {
        this.primera = primera;
    }

    public boolean isUltima() {
        return ultima;
    }

    public void setUltima(boolean ultima) {
        this.ultima = ultima;
    }
}