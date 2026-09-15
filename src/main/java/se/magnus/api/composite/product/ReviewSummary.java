package se.magnus.api.composite.product;

/**
 * Resumo de uma avaliação (review) para ser exibido dentro do agregado do produto.
 * Contém o id da avaliação, autor, assunto e conteúdo.
 */
public class ReviewSummary {

    private final int reviewId;
    private final String author;
    private final String subject;
    private final String content;

    // Construtor padrão sem argumentos para serialização/desserialização JSON
    public ReviewSummary() {
        this.reviewId = 0;
        this.author = null;
        this.subject = null;
        this.content = null;
    }

    // Construtor com todos os parâmetros
    public ReviewSummary(int reviewId, String author, String subject, String content) {
        this.reviewId = reviewId;
        this.author = author;
        this.subject = subject;
        this.content = content;
    }

    public int getReviewId() {
        return reviewId;
    }

    public String getAuthor() {
        return author;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }
}
