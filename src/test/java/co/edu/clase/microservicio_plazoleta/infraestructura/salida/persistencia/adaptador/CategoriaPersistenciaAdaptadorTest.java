package co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.adaptador;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.Categoria;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.entidad.CategoriaEntidad;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.mapeador.CategoriaPersistenciaMapeador;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.repositorio.CategoriaJpaRepositorio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoriaPersistenciaAdaptadorTest {

    @Mock
    private CategoriaJpaRepositorio
            categoriaJpaRepositorio;

    @Spy
    private CategoriaPersistenciaMapeador
            categoriaPersistenciaMapeador;

    @InjectMocks
    private CategoriaPersistenciaAdaptador
            categoriaPersistenciaAdaptador;

    @Test
    void debeConsultarCategoriaActivaPorId() {
        CategoriaEntidad entidad = new CategoriaEntidad();

        entidad.setId(2L);
        entidad.setNombre("PLATO_FUERTE");
        entidad.setDescripcion(
                "Platos principales del menú"
        );
        entidad.setActivo(Boolean.TRUE);

        when(categoriaJpaRepositorio
                .findByIdAndActivoTrue(2L))
                .thenReturn(Optional.of(entidad));

        Optional<Categoria> resultado =
                categoriaPersistenciaAdaptador
                        .buscarActivaPorId(2L);

        assertTrue(resultado.isPresent());
        assertEquals(
                "PLATO_FUERTE",
                resultado.orElseThrow().getNombre()
        );
    }
}