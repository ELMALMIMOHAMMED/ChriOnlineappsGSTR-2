package models;

public class CarteBancaire {

    private String numero;
    private String nom;
    private String expiration;
    private String cvv;

    public CarteBancaire(String numero, String nom, String expiration, String cvv) {
        this.numero = numero;
        this.nom = nom;
        this.expiration = expiration;
        this.cvv = cvv;
    }

    public String getNumero() { return numero; }
    public String getNom() { return nom; }
    public String getExpiration() { return expiration; }
    public String getCvv() { return cvv; }
}