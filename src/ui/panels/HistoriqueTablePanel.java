package ui.panels;

import services.CommandeService;
import services.SessionService;
import ui.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;

public class HistoriqueTablePanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    public HistoriqueTablePanel(MainFrame frame) {

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Liste des commandes", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));

        model = new DefaultTableModel(
                new Object[]{"ID", "Total", "Statut", "Date"}, 0);

        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);

        JButton btnDetails = new JButton("Voir détails");
        JButton btnBack = new JButton("Retour");

        JPanel bottom = new JPanel();
        bottom.add(btnDetails);
        bottom.add(btnBack);

        add(title, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        loadData();

        btnBack.addActionListener(e -> frame.showPage("HISTORIQUE"));
        btnDetails.addActionListener(e -> showDetails());
    }

    private void loadData() {

        model.setRowCount(0);

        int userId = SessionService.getCurrentUserId();

        CommandeService service = new CommandeService();

        try {

            java.util.List<models.Commande> list = service.getCommandesByUser(userId);

            for (models.Commande c : list) {

                model.addRow(new Object[]{
                        c.getId(),
                        c.getTotal(),
                        c.getStatut(),
                        c.getDate()
                });
            }

            if (list.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Aucune commande trouvée.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDetails() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une commande !");
            return;
        }

        int id = (int) model.getValueAt(row, 0);

        CommandeService service = new CommandeService();
        String facture = service.genererFacture(id);

        JTextArea area = new JTextArea(facture);
        area.setEditable(false);

        JOptionPane.showMessageDialog(this,
                new JScrollPane(area),
                "Détails",
                JOptionPane.INFORMATION_MESSAGE);
    }
}