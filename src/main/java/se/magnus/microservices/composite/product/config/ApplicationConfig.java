package se.magnus.microservices.composite.product.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configurações gerais da aplicação Spring Boot.
 * Define os Beans compartilhados que podem ser injetados nas classes de serviço.
 */
@Configuration
public class ApplicationConfig {

    /**
     * Define o Bean do RestTemplate com a anotação @LoadBalanced.
     * O @LoadBalanced instrui o RestTemplate a utilizar o Eureka (Service Discovery)
     * para resolver nomes de serviços virtuais (como http://product/) em IPs e portas reais.
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
