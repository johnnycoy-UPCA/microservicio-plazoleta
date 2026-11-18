package co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.adaptador;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pagina;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Plato;
import co.edu.clase.microservicio_plazoleta.dominio.spi.PlatoPersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.entidad.PlatoEntidad;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.mapeador.PlatoPersistenciaMapeador;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.repositorio.PlatoJpaRepositorio;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PlatoPersistenciaAdaptador
        implements PlatoPersistenciaPuerto {

    private final PlatoJpaRepositorio
            platoJpaRepositorio;

    private final PlatoPersistenciaMapeador
            platoPersistenciaMapeador;

    public PlatoPersistenciaAdaptador(
            PlatoJpaRepositorio platoJpaRepositorio,
            PlatoPersistenciaMapeador
                    platoPersistenciaMapeador) {

        this.platoJpaRepositorio =
                platoJpaRepositorio;

        this.platoPersistenciaMapeador =
                platoPersistenciaMapeador;
    }

	@Override
	@Transactional(readOnly = true)
	public Optional<Plato> buscarPorId(Long platoId) {
		return platoJpaRepositorio
				.findById(platoId)
				.map(platoPersistenciaMapeador::aModelo);
	}

    @Override
    @Transactional
    public Plato guardar(Plato plato) {
        PlatoEntidad entidad =
                platoPersistenciaMapeador.aEntidad(plato);

        PlatoEntidad entidadGuardada =
                platoJpaRepositorio.save(entidad);

        return platoPersistenciaMapeador.aModelo(
                entidadGuardada
        );
    }

	@Override
	@Transactional(readOnly = true)
	public Pagina<Plato> listarActivosPorRestaurante(
			Long restauranteId,
			Long categoriaId,
			int pagina,
			int tamano) {

		PageRequest solicitudPagina =
				PageRequest.of(pagina, tamano);

		Page<PlatoEntidad> paginaEntidades =
				categoriaId == null
						? platoJpaRepositorio
						.findByRestauranteIdAndActivoTrueOrderByNombreAsc(
								restauranteId,
								solicitudPagina
						)
						: platoJpaRepositorio
						.findByRestauranteIdAndCategoriaIdAndActivoTrueOrderByNombreAsc(
								restauranteId,
								categoriaId,
								solicitudPagina
						);

		return new Pagina<>(
				paginaEntidades.getContent()
						.stream()
						.map(platoPersistenciaMapeador::aModelo)
						.toList(),
				paginaEntidades.getNumber(),
				paginaEntidades.getSize(),
				paginaEntidades.getTotalElements(),
				paginaEntidades.getTotalPages(),
				paginaEntidades.isFirst(),
				paginaEntidades.isLast()
		);
	}
}