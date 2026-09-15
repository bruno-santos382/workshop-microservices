package se.magnus.microservices.composite.product.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import se.magnus.api.composite.product.ProductAggregate;
import se.magnus.api.composite.product.RecommendationSummary;
import se.magnus.api.composite.product.ReviewSummary;
import se.magnus.api.composite.product.ServiceAddresses;
import se.magnus.util.exceptions.InvalidInputException;
import se.magnus.util.exceptions.NotFoundException;
import se.magnus.util.http.ServiceUtil;

import java.util.List;

/**
 * Implementação Mockada de integração para o STEP 1.
 * Nesta aula, o serviço composite responde com dados fixos em memória,
 * sem depender que os serviços de núcleo (product, recommendation, review) estejam no ar.
 *
 * A anotação @Profile("!real") garante que este componente seja carregado por padrão,
 * a menos que o perfil "real" seja explicitamente ativado.
 */
@Component
@Profile("!real")
public class ProductCompositeIntegrationMock implements ProductCompositeIntegration {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeIntegrationMock.class);

    private final ServiceUtil serviceUtil;

    public ProductCompositeIntegrationMock(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    /**
     * Retorna dados agregados mockados baseados no productId fornecido.
     * Regras definidas para o STEP 1:
     * - productId < 1: lança InvalidInputException (HTTP 422)
     * - productId == 13: lança NotFoundException (HTTP 404)
     * - productId == 1 (ou demais válidos): retorna agregado completo com 1 recomendação e 1 avaliação.
     */
    @Override
    public ProductAggregate getProduct(int productId) {
        LOG.debug("Chamando getProduct mockado para productId: {}", productId);

        // Validação: IDs menores que 1 são considerados entradas inválidas (HTTP 422)
        if (productId < 1) {
            throw new InvalidInputException("ID do produto inválido: " + productId);
        }

        // Simulação da convenção de testes: ID 13 simula produto não existente no banco (HTTP 404)
        if (productId == 13) {
            throw new NotFoundException("Nenhum produto encontrado para o productId: " + productId);
        }

        // Criamos 1 recomendação mockada respeitando o contrato
        List<RecommendationSummary> recommendations = List.of(
                new RecommendationSummary(1, "Autor Mock 1", 4, "Ótimo produto, recomendo!")
        );

        // Criamos 1 avaliação (review) mockada respeitando o contrato
        List<ReviewSummary> reviews = List.of(
                new ReviewSummary(1, "Autor Mock 1", "Excelente", "Produto superou minhas expectativas.")
        );

        // Captura o endereço real deste composite (cmp) e define valores fictícios para os núcleos
        String cmpAddress = serviceUtil.getServiceAddress();
        ServiceAddresses serviceAddresses = new ServiceAddresses(
                cmpAddress,
                "product-mock:7001",
                "review-mock:7003",
                "recommendation-mock:7002"
        );

        // Monta e retorna o agregado conforme o contrato 1.1
        return new ProductAggregate(
                productId,
                "Produto Mock " + productId,
                100,
                recommendations,
                reviews,
                serviceAddresses
        );
    }

    /**
     * Simula a criação de um produto agregado.
     * No STEP 1 apenas registramos o log, sem acionar os serviços de núcleo.
     */
    @Override
    public void createProduct(ProductAggregate body) {
        if (body.getProductId() < 1) {
            throw new InvalidInputException("ID do produto inválido: " + body.getProductId());
        }
        LOG.info("[MOCK] createProduct simulado com sucesso para o produto: {} (ID: {})",
                body.getName(), body.getProductId());
    }

    /**
     * Simula a deleção de um produto pelo ID.
     * No STEP 1 apenas registramos o log, sem acionar os serviços de núcleo.
     */
    @Override
    public void deleteProduct(int productId) {
        if (productId < 1) {
            throw new InvalidInputException("ID do produto inválido: " + productId);
        }
        LOG.info("[MOCK] deleteProduct simulado com sucesso para o productId: {}", productId);
    }
}
