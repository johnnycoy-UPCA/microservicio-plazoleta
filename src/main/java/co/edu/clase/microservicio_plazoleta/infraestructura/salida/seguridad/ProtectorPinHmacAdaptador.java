package co.edu.clase.microservicio_plazoleta.infraestructura.salida.seguridad;

import co.edu.clase.microservicio_plazoleta.dominio.excepcion.ExcepcionPlazoleta;
import co.edu.clase.microservicio_plazoleta.dominio.spi.ProtectorPinPuerto;
import co.edu.clase.microservicio_plazoleta.infraestructura.configuracion.PinPropiedades;
import co.edu.clase.microservicio_plazoleta.util.constantes.CodigosError;
import co.edu.clase.microservicio_plazoleta.util.constantes.MensajesError;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.CategoriaError;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Component
public class ProtectorPinHmacAdaptador
        implements ProtectorPinPuerto {

    private static final String ALGORITMO =
            "HmacSHA256";

    private final byte[] secreto;

    public ProtectorPinHmacAdaptador(
            PinPropiedades propiedades) {

        try {
            this.secreto = Base64
                    .getDecoder()
                    .decode(
                            propiedades.getSecreto()
                    );
        } catch (IllegalArgumentException excepcion) {
            throw new IllegalStateException(
                    "PIN_SECRETO debe ser Base64 válido",
                    excepcion
            );
        }

        if (secreto.length < 32) {
            throw new IllegalStateException(
                    "PIN_SECRETO debe representar "
                            + "al menos 32 bytes"
            );
        }
    }

    @Override
    public String proteger(
            Long pedidoId,
            String pin) {

        try {
            Mac mac = Mac.getInstance(ALGORITMO);

            mac.init(
                    new SecretKeySpec(
                            secreto,
                            ALGORITMO
                    )
            );

            byte[] resultado = mac.doFinal(
                    construirEntrada(
                            pedidoId,
                            pin
                    )
            );

            return Base64.getEncoder()
                    .encodeToString(resultado);
        } catch (Exception excepcion) {
            throw new ExcepcionPlazoleta(
                    CodigosError.PIN_NO_PROCESABLE,
                    MensajesError.PIN_NO_PROCESABLE,
                    CategoriaError.ERROR_INTERNO,
                    excepcion
            );
        }
    }

    @Override
    public boolean coincide(
            Long pedidoId,
            String pin,
            String valorProtegido) {

        if (valorProtegido == null
                || valorProtegido.isBlank()) {

            return false;
        }

        String calculado = proteger(
                pedidoId,
                pin
        );

        return MessageDigest.isEqual(
                calculado.getBytes(
                        StandardCharsets.UTF_8
                ),
                valorProtegido.getBytes(
                        StandardCharsets.UTF_8
                )
        );
    }

    private byte[] construirEntrada(
            Long pedidoId,
            String pin) {

        String entrada = pedidoId
                + ":"
                + pin;

        return entrada.getBytes(
                StandardCharsets.UTF_8
        );
    }
}