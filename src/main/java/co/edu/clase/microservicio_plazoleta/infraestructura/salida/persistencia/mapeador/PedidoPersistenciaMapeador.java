package co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.mapeador;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.DetallePedido;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Pedido;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.entidad.DetallePedidoEntidad;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.entidad.PedidoEntidad;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.entidad.PlatoEntidad;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.entidad.RestauranteEntidad;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PedidoPersistenciaMapeador {

    public PedidoEntidad aEntidad(Pedido modelo) {
        if (modelo == null) {
            return null;
        }

        PedidoEntidad entidad = new PedidoEntidad();

        entidad.setId(modelo.getId());
        entidad.setClienteId(modelo.getClienteId());

        RestauranteEntidad restaurante =
                new RestauranteEntidad();

        restaurante.setId(modelo.getRestauranteId());

        entidad.setRestaurante(restaurante);
        entidad.setEmpleadoAsignadoId(
                modelo.getEmpleadoAsignadoId()
        );
        entidad.setEstado(modelo.getEstado());
        entidad.setTotal(modelo.getTotal());
        entidad.setPinHash(modelo.getPinHash());
        entidad.setPinGeneradoEn(
                modelo.getPinGeneradoEn()
        );
        entidad.setPinExpiraEn(
                modelo.getPinExpiraEn()
        );
        entidad.setPinIntentosFallidos(
                modelo.getPinIntentosFallidos()
        );
        entidad.setFechaCreacion(
                modelo.getFechaCreacion()
        );
        entidad.setFechaActualizacion(
                modelo.getFechaActualizacion()
        );
        entidad.setVersion(modelo.getVersion());

        if (modelo.getDetalles() != null) {
            for (DetallePedido detalle :
                    modelo.getDetalles()) {

                entidad.agregarDetalle(
                        detalleAEntidad(detalle)
                );
            }
        }

        return entidad;
    }

    public Pedido aModelo(PedidoEntidad entidad) {
        if (entidad == null) {
            return null;
        }

        Pedido modelo = new Pedido();

        modelo.setId(entidad.getId());
        modelo.setClienteId(entidad.getClienteId());
        modelo.setRestauranteId(
                entidad.getRestaurante().getId()
        );
        modelo.setEmpleadoAsignadoId(
                entidad.getEmpleadoAsignadoId()
        );
        modelo.setEstado(entidad.getEstado());
        modelo.setTotal(entidad.getTotal());
        modelo.setPinHash(entidad.getPinHash());
        modelo.setPinGeneradoEn(
                entidad.getPinGeneradoEn()
        );
        modelo.setPinExpiraEn(
                entidad.getPinExpiraEn()
        );
        modelo.setPinIntentosFallidos(
                entidad.getPinIntentosFallidos()
        );
        modelo.setFechaCreacion(
                entidad.getFechaCreacion()
        );
        modelo.setFechaActualizacion(
                entidad.getFechaActualizacion()
        );
        modelo.setVersion(entidad.getVersion());
        modelo.setDetalles(
                detallesAModelo(entidad.getDetalles())
        );

        return modelo;
    }

    private DetallePedidoEntidad detalleAEntidad(
            DetallePedido modelo) {

        DetallePedidoEntidad entidad =
                new DetallePedidoEntidad();

        entidad.setId(modelo.getId());

        PlatoEntidad plato = new PlatoEntidad();
        plato.setId(modelo.getPlatoId());

        entidad.setPlato(plato);
        entidad.setNombrePlato(
                modelo.getNombrePlato()
        );
        entidad.setPrecioUnitario(
                modelo.getPrecioUnitario()
        );
        entidad.setCantidad(modelo.getCantidad());

        return entidad;
    }

    private List<DetallePedido> detallesAModelo(
            List<DetallePedidoEntidad> entidades) {

        List<DetallePedido> resultado =
                new ArrayList<>();

        if (entidades == null) {
            return resultado;
        }

        for (DetallePedidoEntidad entidad :
                entidades) {

            resultado.add(new DetallePedido(
                    entidad.getId(),
                    entidad.getPlato().getId(),
                    entidad.getNombrePlato(),
                    entidad.getPrecioUnitario(),
                    entidad.getCantidad()
            ));
        }

        return resultado;
    }
}