package co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.adaptador;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.Categoria;
import co.edu.clase.microservicio_plazoleta.dominio.spi.CategoriaPersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.mapeador.CategoriaPersistenciaMapeador;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.repositorio.CategoriaJpaRepositorio;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class CategoriaPersistenciaAdaptador
        implements CategoriaPersistenciaPuerto {

    private final CategoriaJpaRepositorio
            categoriaJpaRepositorio;

    private final CategoriaPersistenciaMapeador
            categoriaPersistenciaMapeador;

    public CategoriaPersistenciaAdaptador(
            CategoriaJpaRepositorio
                    categoriaJpaRepositorio,
            CategoriaPersistenciaMapeador
                    categoriaPersistenciaMapeador) {

        this.categoriaJpaRepositorio =
                categoriaJpaRepositorio;

        this.categoriaPersistenciaMapeador =
                categoriaPersistenciaMapeador;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Categoria> buscarActivaPorId(
            Long categoriaId) {

        return categoriaJpaRepositorio
                .findByIdAndActivoTrue(categoriaId)
                .map(
                        categoriaPersistenciaMapeador::aModelo
                );
    }
}