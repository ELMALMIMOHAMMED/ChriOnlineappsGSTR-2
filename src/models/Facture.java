package models;

import java.time.LocalDateTime;

public class Facture {

    private int id;
    private int commandeId;
    private double montant;
    private String details;
    private LocalDateTime date;

    public Facture(int commandeId, double montant, String details) {
        this.commandeId = commandeId;
        this.montant = montant;
        this.details = details;
        this.date = LocalDateTime.now();
    }

    public int getCommandeId() {
        return commandeId;
    }

    public double getMontant() {
        return montant;
    }

    public String getDetails() {
        return details;
    }

    public LocalDateTime getDate() {
        return date;
    }
} 