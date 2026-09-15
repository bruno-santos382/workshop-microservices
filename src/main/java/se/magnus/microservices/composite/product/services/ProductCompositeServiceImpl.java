package se.magnus.microservices.composite.product.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import se.magnus.api.composite.product.ProductAggregate;
import se.magnus.api.composite.product.ProductCompositeService;

/**
 * Controlador REST principal do microsserviço product-composite.
 * Ele implementa a interface ProductCompositeService e atende as requisições HTTP:
 * - POST /product-composite
 * - GET /product-composite/{productId}
 * - DELETE /product-composite/{productId}
 *
 * Ele não sabe se os dados vêm de um mock ou da rede real:
 * ele apenas delega para a interface ProductCompositeIntegration.
 */
@RestController
public class ProductCompositeServiceImpl implements ProductCompositeService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeServiceImpl.class);

    private final ProductCompositeIntegration integration;

    @Autowired
    public ProductCompositeServiceImpl(ProductCompositeIntegration integration) {
        this.integration = integration;
    }

    /**
     * POST /product-composite
     * Recebe um ProductAggregate e solicita a criação.
     * Retorna HTTP 202 (Accepted) conforme anotado na interface.
     */
    @Override
    public void createProduct(ProductAggregate body) {
        LOG.info("createProduct chamado para o productId: {}", body.getProductId());
        integration.createProduct(body);
    }

    /**
     * GET /product-composite/{productId}
     * Busca os dados agregados do produto.
     * Retorna HTTP 200 (OK) com o JSON de ProductAggregate.
     */
    @Override
    public ProductAggregate getProduct(int productId) {
        LOG.info("getProduct chamado para o productId: {}", productId);
        return integration.getProduct(productId);
    }

    /**
     * DELETE /product-composite/{productId}
     * Remove o produto e suas informações filhas pelo ID.
     * Retorna HTTP 202 (Accepted) conforme anotado na interface.
     */
    @Override
    public void deleteProduct(int productId) {
        LOG.info("deleteProduct chamado para o productId: {}", productId);
        integration.deleteProduct(productId);
    }
}
