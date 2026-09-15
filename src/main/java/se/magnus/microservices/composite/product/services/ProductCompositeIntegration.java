package se.magnus.microservices.composite.product.services;

import se.magnus.api.composite.product.ProductAggregate;

/**
 * Contrato de integração para o serviço composite.
 * Isola a lógica de obtenção dos dados agregados, permitindo alternar
 * facilmente entre dados mockados (nesta aula) e integração real via rede (próxima aula).
 */
public interface ProductCompositeIntegration {

    /**
     * Obtém o agregado completo do produto pelo id.
     */
    ProductAggregate getProduct(int productId);

    /**
     * Cria um novo produto agregado (produto + recomendações + avaliações).
     */
    void createProduct(ProductAggregate body);

    /**
     * Remove o produto e todas as suas informações associadas.
     */
    void deleteProduct(int productId);
}
