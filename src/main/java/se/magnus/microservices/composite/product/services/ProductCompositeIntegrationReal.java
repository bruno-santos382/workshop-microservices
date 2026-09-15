package se.magnus.microservices.composite.product.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import se.magnus.api.composite.product.ProductAggregate;
import se.magnus.api.composite.product.RecommendationSummary;
import se.magnus.api.composite.product.ReviewSummary;
import se.magnus.api.composite.product.ServiceAddresses;
import se.magnus.api.core.product.Product;
import se.magnus.api.core.recommendation.Recommendation;
import se.magnus.api.core.review.Review;
import se.magnus.util.exceptions.InvalidInputException;
import se.magnus.util.exceptions.NotFoundException;
import se.magnus.util.http.ServiceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementação REAL de integração com os microsserviços de núcleo.
 * Esta classe utiliza o RestTemplate anotado com @LoadBalanced para resolver
 * os nomes dos serviços registrados no Eureka (http://product, http://recommendation, http://review).
 *
 * NOTA IMPORTANTE (STEP 1):
 * Esta classe está anotada com @Profile("real"). Como o perfil padrão da aplicação
 * não é "real", ela permanece DESATIVADA nesta aula e só será ativada na próxima aula.
 */
@Component
@Profile("real")
public class ProductCompositeIntegrationReal implements ProductCompositeIntegration {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeIntegrationReal.class);

    private final RestTemplate restTemplate;
    private final ServiceUtil serviceUtil;

    // URLs base usando os nomes dos serviços registrados no Eureka
    private static final String PRODUCT_SERVICE_URL = "http://product/product";
    private static final String RECOMMENDATION_SERVICE_URL = "http://recommendation/recommendation";
    private static final String REVIEW_SERVICE_URL = "http://review/review";

    @Autowired
    public ProductCompositeIntegrationReal(RestTemplate restTemplate, ServiceUtil serviceUtil) {
        this.restTemplate = restTemplate;
        this.serviceUtil = serviceUtil;
    }

    /**
     * Busca os dados reais fazendo chamadas HTTP para os 3 microsserviços de núcleo
     * e monta o objeto agregado ProductAggregate.
     */
    @Override
    public ProductAggregate getProduct(int productId) {
        try {
            LOG.debug("Chamando serviço real de produto para o ID: {}", productId);
            Product product = restTemplate.getForObject(PRODUCT_SERVICE_URL + "/" + productId, Product.class);

            LOG.debug("Chamando serviço real de recomendações para o productId: {}", productId);
            List<Recommendation> recommendations = restTemplate.exchange(
                    RECOMMENDATION_SERVICE_URL + "?productId=" + productId,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Recommendation>>() {}
            ).getBody();

            LOG.debug("Chamando serviço real de avaliações para o productId: {}", productId);
            List<Review> reviews = restTemplate.exchange(
                    REVIEW_SERVICE_URL + "?productId=" + productId,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Review>>() {}
            ).getBody();

            return createProductAggregate(product, recommendations, reviews);

        } catch (HttpClientErrorException ex) {
            // Repassa os status HTTP recebidos dos serviços núcleo
            handleHttpClientException(ex);
            return null; // Linha inalcançável pois handleHttpClientException sempre lança exceção
        }
    }

    /**
     * Envia os dados para os microsserviços criarem suas respectivas entidades.
     */
    @Override
    public void createProduct(ProductAggregate body) {
        try {
            LOG.debug("Criando produto no serviço núcleo: {}", body.getProductId());
            Product product = new Product(body.getProductId(), body.getName(), body.getWeight(), null);
            restTemplate.postForObject(PRODUCT_SERVICE_URL, product, Product.class);

            if (body.getRecommendations() != null) {
                for (RecommendationSummary rec : body.getRecommendations()) {
                    Recommendation recommendation = new Recommendation(
                            body.getProductId(),
                            rec.getRecommendationId(),
                            rec.getAuthor(),
                            rec.getRate(),
                            rec.getContent(),
                            null
                    );
                    restTemplate.postForObject(RECOMMENDATION_SERVICE_URL, recommendation, Recommendation.class);
                }
            }

            if (body.getReviews() != null) {
                for (ReviewSummary rev : body.getReviews()) {
                    Review review = new Review(
                            body.getProductId(),
                            rev.getReviewId(),
                            rev.getAuthor(),
                            rev.getSubject(),
                            rev.getContent(),
                            null
                    );
                    restTemplate.postForObject(REVIEW_SERVICE_URL, review, Review.class);
                }
            }
        } catch (HttpClientErrorException ex) {
            handleHttpClientException(ex);
        }
    }

    /**
     * Remove o produto e suas recomendações/avaliações em cascata.
     */
    @Override
    public void deleteProduct(int productId) {
        try {
            LOG.debug("Deletando produto e dados associados para o ID: {}", productId);
            restTemplate.delete(PRODUCT_SERVICE_URL + "/" + productId);
            restTemplate.delete(RECOMMENDATION_SERVICE_URL + "?productId=" + productId);
            restTemplate.delete(REVIEW_SERVICE_URL + "?productId=" + productId);
        } catch (HttpClientErrorException ex) {
            handleHttpClientException(ex);
        }
    }

    /**
     * Converte as entidades dos serviços núcleo para o formato de visão agregada.
     */
    private ProductAggregate createProductAggregate(Product product, List<Recommendation> recommendations, List<Review> reviews) {
        int productId = product != null ? product.getProductId() : 0;
        String name = product != null ? product.getName() : null;
        int weight = product != null ? product.getWeight() : 0;

        // Converte recomendações
        List<RecommendationSummary> recommendationSummaries = new ArrayList<>();
        if (recommendations != null) {
            for (Recommendation r : recommendations) {
                recommendationSummaries.add(new RecommendationSummary(r.getRecommendationId(), r.getAuthor(), r.getRate(), r.getContent()));
            }
        }

        // Converte avaliações (reviews)
        List<ReviewSummary> reviewSummaries = new ArrayList<>();
        if (reviews != null) {
            for (Review r : reviews) {
                reviewSummaries.add(new ReviewSummary(r.getReviewId(), r.getAuthor(), r.getSubject(), r.getContent()));
            }
        }

        // Obtém o endereço deste composite e dos núcleos que responderam
        String cmpAddress = serviceUtil.getServiceAddress();
        String proAddress = product != null ? product.getServiceAddress() : null;
        String revAddress = (reviews != null && !reviews.isEmpty()) ? reviews.get(0).getServiceAddress() : null;
        String recAddress = (recommendations != null && !recommendations.isEmpty()) ? recommendations.get(0).getServiceAddress() : null;

        ServiceAddresses serviceAddresses = new ServiceAddresses(cmpAddress, proAddress, revAddress, recAddress);

        return new ProductAggregate(productId, name, weight, recommendationSummaries, reviewSummaries, serviceAddresses);
    }

    private void handleHttpClientException(HttpClientErrorException ex) {
        int statusCode = ex.getStatusCode().value();
        if (statusCode == 404) {
            throw new NotFoundException(ex.getResponseBodyAsString());
        } else if (statusCode == 422) {
            throw new InvalidInputException(ex.getResponseBodyAsString());
        } else {
            LOG.warn("Erro HTTP inesperado: {}", ex.getStatusCode());
            throw ex;
        }
    }
}
