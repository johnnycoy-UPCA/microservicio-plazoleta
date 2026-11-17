package co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.adaptador;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.Categoria;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Plato;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.entidad.CategoriaEntidad;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.entidad.PlatoEntidad;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.entidad.RestauranteEntidad;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.mapeador.CategoriaPersistenciaMapeador;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.mapeador.PlatoPersistenciaMapeador;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.repositorio.PlatoJpaRepositorio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlatoPersistenciaAdaptadorTest {

    @Mock
    private PlatoJpaRepositorio platoJpaRepositorio;

    @Spy
    private PlatoPersistenciaMapeador
            platoPersistenciaMapeador =
            new PlatoPersistenciaMapeador(
                    new CategoriaPersistenciaMapeador()
            );

    @InjectMocks
    private PlatoPersistenciaAdaptador
            platoPersistenciaAdaptador;

    @Test
    void debeGuardarPlato() {
        when(platoJpaRepositorio.save(
                any(PlatoEntidad.class)
        )).thenReturn(crearEntidadGuardada());

        Plato resultado =
                platoPersistenciaAdaptador.guardar(
                        crearPlato()
                );

        assertEquals(1L, resultado.getId());
        assertEquals(
                "Arroz con pollo",
                resultado.getNombre()
        );
        assertEquals(25000, resultado.getPrecio());
        assertEquals(
                "PLATO_FUERTE",
                resultado.getCategoria().getNombre()
        );
        assertEquals(1L, resultado.getRestauranteId());
        assertTrue(resultado.getActivo());
    }

    private Plato crearPlato() {
        return new Plato(
                null,
                "Arroz con pollo",
                "Arroz acompañado de pollo",
                25000,
                "https://ejemplo.com/arroz-pollo.png",
                new Categoria(
                        2L,
                        "PLATO_FUERTE",
                        "Platos principales del menú",
                        Boolean.TRUE
                ),
                1L,
                Boolean.TRUE,
                LocalDateTime.of(
                        2026, 11, 16, 12, 0
                ),
                LocalDateTime.of(
                        2026, 11, 16, 12, 0
                )
        );
    }

    private PlatoEntidad crearEntidadGuardada() {
        CategoriaEntidad categoria =
                new CategoriaEntidad();

        categoria.setId(2L);
        categoria.setNombre("PLATO_FUERTE");
        categoria.setDescripcion(
                "Platos principales del menú"
        );
        categoria.setActivo(Boolean.TRUE);

        RestauranteEntidad restaurante =
                new RestauranteEntidad();

        restaurante.setId(1L);

        PlatoEntidad entidad = new PlatoEntidad();

        entidad.setId(1L);
        entidad.setNombre("Arroz con pollo");
        entidad.setDescripcion(
                "Arroz acompañado de pollo"
        );
        entidad.setPrecio(25000);
        entidad.setUrlImagen(
                "https://ejemplo.com/arroz-pollo.png"
        );
        entidad.setCategoria(categoria);
        entidad.setRestaurante(restaurante);
        entidad.setActivo(Boolean.TRUE);
        entidad.setFechaCreacion(
                LocalDateTime.of(
                        2026, 11, 16, 12, 0
                )
        );
        entidad.setFechaActualizacion(
                LocalDateTime.of(
                        2026, 11, 16, 12, 0
                )
        );

        return entidad;
    }
}