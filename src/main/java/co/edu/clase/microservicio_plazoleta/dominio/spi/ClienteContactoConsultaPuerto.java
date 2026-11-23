package co.edu.clase.microservicio_plazoleta.dominio.spi;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.ContactoCliente;

public interface ClienteContactoConsultaPuerto {

    ContactoCliente consultar(Long clienteId);
}