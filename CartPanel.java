package ui.panels;

import models.PanierItem;
import services.CommandeService;
import services.PanierService;
import services.SessionService;
import services.PaiementService;
import ui.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CartPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JLabel totalLabel;

    public CartPanel(MainFrame frame) {

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Mon Panier", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));

        model = new DefaultTableModel(
                new Object[]{"ID", "Produit", "Prix", "Quantité", "Total"}, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(table);

        totalLabel = new JLabel("Total : 0 DH", JLabel.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        JPanel buttons = new JPanel();

        JButton btnPlus = new JButton("➕");
        JButton btnMoins = new JButton("➖");
        JButton btnDelete = new JButton("Supprimer");
        JButton btnCommander = new JButton("Commander");
        JButton btnBack = new JButton("Retour");

        buttons.add(btnPlus);
        buttons.add(btnMoins);
        buttons.add(btnDelete);
        buttons.add(btnCommander);
        buttons.add(btnBack);

        bottom.add(totalLabel, BorderLayout.NORTH);
        bottom.add(buttons, BorderLayout.SOUTH);

        add(title, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        loadPanier();

        btnBack.addActionListener(e -> frame.showPage("PRODUCTS"));
        btnPlus.addActionListener(e -> modifierQuantite(1));
        btnMoins.addActionListener(e -> modifierQuantite(-1));
        btnDelete.addActionListener(e -> supprimerProduit());
        btnCommander.addActionListener(e -> commander(frame));
    }

    // =========================
    // LOAD PANIER
    // =========================
    private void loadPanier() {

        model.setRowCount(0);
        double total = 0;

        if (!SessionService.isLoggedIn()) {
            totalLabel.setText("Non connecté");
            return;
        }

        try {
            PanierService service = new PanierService();
            List<PanierItem> items = service.getPanier();

            for (PanierItem item : items) {

                double totalLigne = item.getPrix() * item.getQuantite();
                total += totalLigne;

                model.addRow(new Object[]{
                        item.getProduitId(),
                        item.getNom(),
                        item.getPrix(),
                        item.getQuantite(),
                        totalLigne
                });
            }

            totalLabel.setText("Total : " + total + " DH");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement panier !");
        }
    }

    // =========================
    // MODIFIER QUANTITE
    // =========================
    private void modifierQuantite(int delta) {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un produit !");
            return;
        }

        int produitId = (int) model.getValueAt(row, 0);
        int quantite = (int) model.getValueAt(row, 3) + delta;

        if (quantite <= 0) return;

        PanierService service = new PanierService();
        int userId = SessionService.getCurrentUserId();

        if (!service.updateQuantite(userId, produitId, quantite)) {
            JOptionPane.showMessageDialog(this, "Erreur modification !");
            return;
        }

        reload();
    }

    // =========================
    // SUPPRIMER PRODUIT
    // =========================
    private void supprimerProduit() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un produit !");
            return;
        }

        int produitId = (int) model.getValueAt(row, 0);

        PanierService service = new PanierService();
        int userId = SessionService.getCurrentUserId();

        if (!service.supprimerProduit(userId, produitId)) {
            JOptionPane.showMessageDialog(this, "Erreur suppression !");
            return;
        }

        reload();
    }

    // =========================
    // COMMANDE + PAIEMENT
    // =========================
    private void commander(MainFrame frame) {

        if (!SessionService.isLoggedIn()) {
            JOptionPane.showMessageDialog(this, "Veuillez vous connecter !");
            frame.showPage("LOGIN");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Confirmer la commande ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        int userId = SessionService.getCurrentUserId();

        CommandeService commandeService = new CommandeService();
        int commandeId = commandeService.creerCommande(userId);

        if (commandeId == -1) {
            JOptionPane.showMessageDialog(this, "Panier vide !");
            return;
        }

        // =========================
        // CHOIX MODE PAIEMENT
        // =========================
        String[] options = {"Cash", "Carte"};
        int choix = JOptionPane.showOptionDialog(
                this,
                "Choisir méthode de paiement",
                "Paiement",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choix == -1) return;

        String methode = options[choix];

        PaiementService paiementService = new PaiementService();
        boolean ok;

        // =========================
        // PAIEMENT CARTE
        // =========================
        if ("Carte".equalsIgnoreCase(methode)) {

            JTextField num = new JTextField();
            JTextField nom = new JTextField();
            JTextField exp = new JTextField();
            JTextField cvv = new JTextField();

            Object[] fields = {
                    "Numéro carte:", num,
                    "Nom:", nom,
                    "Expiration:", exp,
                    "CVV:", cvv
            };

            int res = JOptionPane.showConfirmDialog(
                    this,
                    fields,
                    "Paiement Carte",
                    JOptionPane.OK_CANCEL_OPTION
            );

            if (res != JOptionPane.OK_OPTION) return;

            if (num.getText().isEmpty() || nom.getText().isEmpty()
                    || exp.getText().isEmpty() || cvv.getText().isEmpty()) {

                JOptionPane.showMessageDialog(this, "Champs invalides !");
                return;
            }

            ok = paiementService.processPayment(
                    userId,
                    commandeId,
                    "Carte",
                    num.getText(),
                    nom.getText(),
                    exp.getText(),
                    cvv.getText()
            );

        } else {

            ok = paiementService.processPayment(userId, commandeId, "Cash");
        }

        // =========================
        // RESULTAT
        // =========================
        if (ok) {
            JOptionPane.showMessageDialog(this, "✅ Paiement réussi !");
            afficherFacture(commandeId);
            reload();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Paiement échoué !");
        }
    }

    // =========================
    // FACTURE
    // =========================
    private void afficherFacture(int commandeId) {

        try {
            CommandeService service = new CommandeService();
            String facture = service.genererFacture(commandeId);

            JTextArea area = new JTextArea(facture);
            area.setEditable(false);
            area.setFont(new Font("Monospaced", Font.PLAIN, 14));

            JOptionPane.showMessageDialog(
                    this,
                    new JScrollPane(area),
                    "Facture",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur facture !");
        }
    }

    public void reload() {
        loadPanier();
    }
}