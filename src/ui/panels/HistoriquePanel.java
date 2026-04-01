package ui.panels;

import com.google.gson.*;
import common.Message;
import services.SessionService;
import ui.MainFrame;

import javax.swing.*;
import java.awt.*;

public class HistoriquePanel extends JPanel {

    private JTextArea area;
    private MainFrame frame;

    public HistoriquePanel(MainFrame frame) {

        this.frame = frame;
        setLayout(new BorderLayout());

        // =========================
        // TITLE
        // =========================
        JLabel title = new JLabel("Historique des commandes", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // =========================
        // AREA
        // =========================
        area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));

        add(new JScrollPane(area), BorderLayout.CENTER);

        // =========================
        // BUTTONS
        // =========================
        JPanel bottom = new JPanel();

        JButton btnLoad = new JButton("Voir Historique");
        JButton btnBack = new JButton("Retour");

        bottom.add(btnLoad);
        bottom.add(btnBack);

        add(bottom, BorderLayout.SOUTH);

        btnLoad.addActionListener(e -> loadHistorique());
        btnBack.addActionListener(e -> frame.showPage("HOME"));
    }

    private void loadHistorique() {

        try {

            // =========================
            // REQUEST
            // =========================
            JsonObject data = new JsonObject();
            data.addProperty("userId", SessionService.getCurrentUserId());

            Message req = new Message(
                    "GET_HISTORIQUE",
                    "REQ_HIST",
                    "",
                    data.toString(),
                    ""
            );

            Message res = frame.getClient().sendRequest(req);

            if (res == null) {
                area.setText("❌ Pas de réponse serveur");
                return;
            }

            if (!"SUCCESS".equalsIgnoreCase(res.getStatus())) {
            	area.setText("Erreur : " + res.getData());
                return;
            }

            // =========================
            // PARSE DATA
            // =========================
            JsonObject dataObj = JsonParser
                    .parseString(res.getData())
                    .getAsJsonObject();

            JsonArray array = dataObj.getAsJsonArray("commandes");

            area.setText("");

            if (array.size() == 0) {
                area.setText("Aucune commande trouvée.");
                return;
            }

            // =========================
            // DISPLAY
            // =========================
            for (JsonElement el : array) {

                JsonObject c = el.getAsJsonObject();

                area.append("Commande #" + c.get("id").getAsInt() + "\n");
                area.append("Total : " + c.get("total").getAsDouble() + " DH\n");

                if (c.has("statut")) {
                    area.append("Statut : " + c.get("statut").getAsString() + "\n");
                }

                area.append("Date : " + c.get("date").getAsString() + "\n");
                area.append("-----------------------------\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
            area.setText("❌ Erreur serveur");
        }
    }
}