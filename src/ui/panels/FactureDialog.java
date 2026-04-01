package ui.panels;

import javax.swing.*;
import java.awt.*;


public class FactureDialog extends JDialog {

    public FactureDialog(JFrame parent, String facture) {

        super(parent, "Facture", true);

        setSize(500, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JTextArea area = new JTextArea(facture);
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane(area);

        JButton btnPDF = new JButton("📄 Exporter PDF");
        JButton btnClose = new JButton("Fermer");

        JPanel panel = new JPanel();
        panel.add(btnPDF);
        panel.add(btnClose);

        add(new JLabel(" Facture générée", JLabel.CENTER), BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        // 🔥 export PDF
        btnPDF.addActionListener(e -> {
            PdfGenerator.generate(facture);
            JOptionPane.showMessageDialog(this, "PDF généré !");
        });

        btnClose.addActionListener(e -> dispose());
    }
}