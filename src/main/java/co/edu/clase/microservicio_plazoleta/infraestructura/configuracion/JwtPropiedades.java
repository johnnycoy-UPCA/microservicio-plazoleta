package co.edu.clase.microservicio_plazoleta.infraestructura.configuracion;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component 
@ConfigurationProperties (prefix = "seguridad.jwt")
public class JwtPropiedades {
    private String secreto;

    public String getSecreto() {
        return secreto;
    }

    public void setSecreto(String secreto) {
        this.secreto = secreto;
    }
}
