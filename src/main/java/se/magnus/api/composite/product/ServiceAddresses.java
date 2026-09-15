package se.magnus.api.composite.product;

/**
 * Armazena os endereços de rede (host:porta) dos microsserviços que responderam à requisição.
 * - cmp: composite service
 * - pro: product service
 * - rev: review service
 * - rec: recommendation service
 */
public class ServiceAddresses {

    private final String cmp;
    private final String pro;
    private final String rev;
    private final String rec;

    // Construtor vazio para desserialização
    public ServiceAddresses() {
        this.cmp = null;
        this.pro = null;
        this.rev = null;
        this.rec = null;
    }

    // Construtor completo
    public ServiceAddresses(String cmp, String pro, String rev, String rec) {
        this.cmp = cmp;
        this.pro = pro;
        this.rev = rev;
        this.rec = rec;
    }

    public String getCmp() {
        return cmp;
    }

    public String getPro() {
        return pro;
    }

    public String getRev() {
        return rev;
    }

    public String getRec() {
        return rec;
    }
}
