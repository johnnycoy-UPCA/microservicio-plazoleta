package co.edu.clase.microservicio_plazoleta.dominio.spi;

public interface ProtectorPinPuerto {

    String proteger(
            Long pedidoId,
            String pin
    );

    boolean coincide(
            Long pedidoId,
            String pin,
            String valorProtegido
    );
}