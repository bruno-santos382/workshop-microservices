package se.magnus.util.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Utilitário responsável por capturar o endereço e a porta da instância em execução.
 * No Spring Boot 3, o evento WebServerInitializedEvent é disparado quando o servidor web
 * embutido (Tomcat) é inicializado, permitindo obter a porta real mesmo com server.port=0.
 */
@Component
public class ServiceUtil implements ApplicationListener<WebServerInitializedEvent> {

    private Integer serverPort;
    private String serverIp;

    // Injeção opcional da Registration do Eureka (quando o serviço estiver registrado)
    @Autowired(required = false)
    private Registration registration;

    public Integer getServerPort() {
        return serverPort;
    }

    public String getServerIp() {
        return serverIp;
    }

    /**
     * Retorna o endereço da instância no formato "host:porta", usado no campo serviceAddress (cmp).
     * Se houver registro no Eureka, usa o host e porta registrados; caso contrário, usa o IP local e a porta do servidor.
     */
    public String getServiceAddress() {
        if (registration != null) {
            return registration.getHost() + ":" + registration.getPort();
        }
        return (serverIp != null ? serverIp : "localhost") + ":" + (serverPort != null ? serverPort : 7000);
    }

    /**
     * Retorna a URL completa no formato "http://host:porta" para conveniência.
     */
    public String getServerAddress() {
        return "http://" + getServiceAddress();
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        // Captura a porta em que a aplicação subiu
        this.serverPort = event.getWebServer().getPort();
        try {
            // Obtém o IP da máquina local
            this.serverIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            this.serverIp = "unknown";
        }
    }
}
