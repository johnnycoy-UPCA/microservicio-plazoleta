package co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.adaptador;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.Plato;
import co.edu.clase.microservicio_plazoleta.dominio.spi.PlatoPersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.entidad.PlatoEntidad;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.mapeador.PlatoPersistenciaMapeador;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.repositorio.PlatoJpaRepositorio;

import java.util.Optional;

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
	public Optional<Plato> buscarPorId(Long platoId) {
		return platoJpaRepositorio
				.findById(platoId)
				.map(platoPersistenciaMapeador::aModelo);
	}
}