package se.magnus.product_composite_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Ponto de entrada (Main) do microsserviço product-composite-service.
 * A anotação @ComponentScan("se.magnus") instrui o Spring a escanear todos os pacotes
 * sob 'se.magnus' para encontrar Controllers, Services, Handlers de erro e Utilitários.
 */
@SpringBootApplication
@ComponentScan("se.magnus")
public class ProductCompositeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductCompositeServiceApplication.class, args);
    }
}
