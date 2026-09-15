package se.magnus.api.composite.product;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Contrato REST para o serviço ProductCompositeService.
 * Define os endpoints para criar, buscar e deletar a visão agregada de um produto.
 */
public interface ProductCompositeService {

    /**
     * Endpoint para cadastrar um produto agregado.
     * Na aula 1 (mock), apenas recebe a requisição e retorna 202 (Accepted).
     */
    @PostMapping(value = "/product-composite", consumes = "application/json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void createProduct(@RequestBody ProductAggregate body);

    /**
     * Endpoint para buscar os dados agregados de um produto pelo seu ID.
     * Retorna o produto com suas recomendações e avaliações.
     */
    @GetMapping(value = "/product-composite/{productId}", produces = "application/json")
    ProductAggregate getProduct(@PathVariable("productId") int productId);

    /**
     * Endpoint para deletar um produto e suas entidades filhas.
     * Na aula 1 (mock), apenas simula a deleção e responde sem acionar os núcleos.
     */
    @DeleteMapping(value = "/product-composite/{productId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void deleteProduct(@PathVariable("productId") int productId);
}
