package co.edu.clase.microservicio_plazoleta.infraestructura.configuracion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@ConfigurationProperties(prefix = "servicios.mensajeria")
public class MensajeriaServicioPropiedades {

    @NotBlank(
            message = "MENSAJERIA_URL_BASE es obligatoria"
    )
    private String urlBase;

    @NotBlank(
            message = "MENSAJERIA_API_KEY es obligatoria"
    )
    @Size(
            min = 24,
            message = "MENSAJERIA_API_KEY debe tener "
                        + "al menos 24 caracteres"
    )
    private String apiKey;

    public String getUrlBase() {
        return urlBase;
    }

    public void setUrlBase(String urlBase) {
        this.urlBase = urlBase;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}