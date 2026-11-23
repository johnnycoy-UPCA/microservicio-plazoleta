package co.edu.clase.microservicio_plazoleta.infraestructura.configuracion;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@ConfigurationProperties(prefix = "seguridad.pin")
public class PinPropiedades {

    @NotBlank(
            message = "PIN_SECRETO es obligatorio"
    )
    private String secreto;

    @Min(
            value = 1,
            message =
                    "La duración del PIN debe ser positiva"
    )
    @Max(
            value = 120,
            message =
                    "La duración del PIN no puede "
                            + "superar 120 minutos"
    )
    private int duracionMinutos = 30;

    @Min(
            value = 1,
            message =
                    "El máximo de intentos debe ser positivo"
    )
    @Max(
            value = 10,
            message =
                    "El máximo de intentos no puede "
                            + "superar 10"
    )
    private int maximoIntentos = 5;

    public String getSecreto() {
        return secreto;
    }

    public void setSecreto(String secreto) {
        this.secreto = secreto;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(
            int duracionMinutos) {

        this.duracionMinutos = duracionMinutos;
    }

    public int getMaximoIntentos() {
        return maximoIntentos;
    }

    public void setMaximoIntentos(
            int maximoIntentos) {

        this.maximoIntentos = maximoIntentos;
    }
}