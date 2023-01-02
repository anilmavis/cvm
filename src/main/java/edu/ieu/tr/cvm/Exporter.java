package edu.ieu.tr.cvm;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javafx.stage.FileChooser;

public final class Exporter {
    public static void export(Cv cv) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream stream = new PDPageContentStream(document, page);
        stream.setFont(PDType1Font.TIMES_ROMAN, 12);
        stream.close();
        FileChooser chooser = new FileChooser();
        chooser.setInitialFileName(cv.getFullName() + new SecureRandom().nextInt() + ".pdf");
        chooser.setTitle("export");
        File file = chooser.showSaveDialog(null);
        
        if (file != null) {
            document.save(file);
        }
    }
}
