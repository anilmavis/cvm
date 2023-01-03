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
    public static void export(Cv c) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream stream = new PDPageContentStream(document, page);
        int padding = 24;
        int width = (int) page.getMediaBox().getWidth();
        int height = (int) page.getMediaBox().getHeight();
        stream.setFont(PDType1Font.TIMES_ROMAN, padding);
        stream.beginText();
        if (c instanceof AcademicCv cv) {
            stream.newLineAtOffset(width / 4, height - padding);
            stream.showText("Full name: " + cv.getFullName());
            stream.newLine();
            stream.newLineAtOffset(0, -padding);
            stream.showText("Birth year: " + cv.getBirthYear());
            stream.newLine();
            stream.newLineAtOffset(0, -padding);
            stream.showText("GPA: " + cv.getGpa());
            stream.newLine();
            stream.newLineAtOffset(0, -padding);
            stream.showText("Home address: " + cv.getHomeAddress());
            stream.newLine();
            stream.newLineAtOffset(0, -padding);
            stream.showText("Job address: " + cv.getJobAddress());
            stream.newLine();
            stream.newLineAtOffset(0, -padding);
            stream.showText("Phone: " + cv.getPhone());
            stream.newLine();
            stream.newLineAtOffset(0, -padding);
            stream.showText("Website: " + cv.getWebsite());
            cv.getSkills().forEach((k, v) -> {
                    try {
                        stream.newLine();
						stream.newLineAtOffset(0, -padding);
                        stream.showText(k + ", proficiency: " + v + " / 10");
					} catch (IOException e) {
						e.printStackTrace();
					}
                });
            cv.getEducation().forEach((k, v) -> {
                    try {
                        stream.newLine();
						stream.newLineAtOffset(0, -padding);
                        stream.showText(k + ", register year: " + v);
					} catch (IOException e) {
						e.printStackTrace();
					}
                });
        } else {
            Cv cv = c;
            System.out.println(width + " " + height);
            stream.newLineAtOffset(width / 4, height - padding);
            stream.showText("Full name: " + cv.getFullName());
            stream.newLine();
            stream.newLineAtOffset(0, -padding);
            stream.showText("Birth year: " + cv.getBirthYear());
            stream.newLine();
            stream.newLineAtOffset(0, -padding);
            stream.showText("GPA: " + cv.getGpa());
            stream.newLine();
            stream.newLineAtOffset(0, -padding);
            stream.showText("Home address: " + cv.getHomeAddress());
            stream.newLine();
            stream.newLineAtOffset(0, -padding);
            stream.showText("Phone: " + cv.getPhone());
            stream.newLine();
            stream.newLineAtOffset(0, -padding);
            stream.showText("Website: " + cv.getWebsite());
            stream.newLine();
            stream.newLineAtOffset(0, -padding);
            stream.showText("SKILLS:");
            cv.getSkills().forEach((k, v) -> {
                    try {
                        stream.newLine();
						stream.newLineAtOffset(0, -padding);
                        stream.showText(k + ", proficiency: " + v + " / 10");
					} catch (IOException e) {
						e.printStackTrace();
					}
                });
            cv.getEducation().forEach((k, v) -> {
                    try {
                        stream.newLine();
						stream.newLineAtOffset(0, -padding);
                        stream.showText(k + ", register year: " + v);
					} catch (IOException e) {
						e.printStackTrace();
					}
                });
        }
        stream.endText();
        stream.close();
        FileChooser chooser = new FileChooser();
        chooser.setInitialFileName(c.getFullName() + c.getBirthYear() + ".pdf");
        chooser.setTitle("export");
        File file = chooser.showSaveDialog(null);

        if (file != null) {
            document.save(file);
        }
    }
}
