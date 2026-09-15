package se.magnus.api.composite.product;

/**
 * Resumo de uma recomendação para ser exibido dentro do agregado do produto.
 * Contém apenas as informações essenciais para quem consulta o composite.
 */
public class RecommendationSummary {

    private final int recommendationId;
    private final String author;
    private final int rate;
    private final String content;

    // Construtor padrão sem argumentos (necessário para serialização/desserialização JSON)
    public RecommendationSummary() {
        this.recommendationId = 0;
        this.author = null;
        this.rate = 0;
        this.content = null;
    }

    // Construtor completo com todos os campos
    public RecommendationSummary(int recommendationId, String author, int rate, String content) {
        this.recommendationId = recommendationId;
        this.author = author;
        this.rate = rate;
        this.content = content;
    }

    public int getRecommendationId() {
        return recommendationId;
    }

    public String getAuthor() {
        return author;
    }

    public int getRate() {
        return rate;
    }

    public String getContent() {
        return content;
    }
}
