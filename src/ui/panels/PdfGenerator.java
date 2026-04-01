package ui.panels;

import javax.swing.JPanel;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.Paragraph;

public class PdfGenerator  extends JPanel {

    public static void generate(String facture) {

        try {

            String path = System.getProperty("user.home") + "/facture.pdf";

            PdfWriter writer = new PdfWriter(path);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("===== FACTURE =====").setBold());
            document.add(new Paragraph("\n"));
            document.add(new Paragraph(facture));

            document.close();

            System.out.println("PDF généré : " + path);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}