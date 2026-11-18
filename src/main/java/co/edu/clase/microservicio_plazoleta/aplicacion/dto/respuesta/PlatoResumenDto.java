package co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta;

public class PlatoResumenDto {

    private Long id;
    private String nombre;
    private String descripcion;
    private Integer precio;
    private String urlImagen;
    private Long categoriaId;
    private String categoriaNombre;

    public PlatoResumenDto() {
    }

    public PlatoResumenDto(
            Long id,
            String nombre,
            String descripcion,
            Integer precio,
            String urlImagen,
            Long categoriaId,
            String categoriaNombre) {

        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.urlImagen = urlImagen;
        this.categoriaId = categoriaId;
        this.categoriaNombre = categoriaNombre;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Integer getPrecio() {
        return precio;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }
}