package edu.ieu.tr.cvm;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.stream.Stream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;

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
            stream.newLineAtOffset(width / 8, height - padding);
            stream.setRenderingMode(RenderingMode.FILL_STROKE);
            stream.showText("• Full name: ");
            stream.setRenderingMode(RenderingMode.FILL);
            stream.showText(cv.getFullName());
            stream.newLine();
            stream.newLineAtOffset(0, -padding);
            stream.setRenderingMode(RenderingMode.FILL_STROKE);
            stream.showText("• Birth year: ");
            stream.setRenderingMode(RenderingMode.FILL);
            stream.showText("" + cv.getBirthYear());
            stream.newLine();
            stream.newLineAtOffset(0, -padding);
            stream.setRenderingMode(RenderingMode.FILL_STROKE);
            stream.showText("• GPA: ");
            stream.setRenderingMode(RenderingMode.FILL);
            stream.showText("" + cv.getGpa());
            stream.newLine();
            stream.setRenderingMode(RenderingMode.FILL_STROKE);
            stream.newLineAtOffset(0, -padding);
            stream.showText("• Email: ");
            stream.setRenderingMode(RenderingMode.FILL);
            stream.showText(cv.getEmail());
            stream.newLine();
            stream.setRenderingMode(RenderingMode.FILL_STROKE);
            stream.newLineAtOffset(0, -padding);
            stream.showText("• Description: ");
            stream.setRenderingMode(RenderingMode.FILL);
            stream.showText(cv.getDescription());
            stream.newLine();

            stream.newLineAtOffset(0, -padding);
            stream.setRenderingMode(RenderingMode.FILL_STROKE);
            stream.showText("• Home address: ");
            stream.showText(cv.getHomeAddress());
            stream.newLine();
            stream.newLineAtOffset(0, -padding);
            stream.setRenderingMode(RenderingMode.FILL_STROKE);
            stream.showText("• Job address: ");
            stream.setRenderingMode(RenderingMode.FILL);
            stream.showText(cv.getJobAddress());
            stream.newLine();
            stream.setRenderingMode(RenderingMode.FILL_STROKE);
            stream.newLineAtOffset(0, -padding);
            stream.setRenderingMode(RenderingMode.FILL_STROKE);
            stream.showText("• Phone: ");
            stream.setRenderingMode(RenderingMode.FILL);
            stream.showText(cv.getPhone());
            stream.newLine();
            stream.newLineAtOffset(0, -padding);
            stream.setRenderingMode(RenderingMode.FILL_STROKE);
            stream.showText("• Website: ");
            stream.setRenderingMode(RenderingMode.FILL);
            stream.showText(cv.getWebsite());
            stream.setRenderingMode(RenderingMode.FILL_STROKE);
            stream.showText("• SKILLS:");
            stream.setRenderingMode(RenderingMode.FILL);
            cv.getSkills().forEach((k, v) -> {
                try {
                    stream.newLine();
                    stream.newLineAtOffset(0, -padding);
                    stream.showText(k + ", proficiency: " + v + " / 10");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            stream.showText("• EDUCATION:");
            cv.getEducation().forEach((k, v) -> {
                try {
                    stream.newLine();
                    stream.newLineAtOffset(0, -padding);
                    stream.showText(k + ", register year: " + v);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            stream.setRenderingMode(RenderingMode.FILL_STROKE);
            stream.showText("• PUBLICATIONS:");
            stream.setRenderingMode(RenderingMode.FILL);
            cv.getPublications().forEach((k, v) -> {
                try {
                    stream.newLine();
                    stream.newLineAtOffset(0, -padding);
                    stream.showText(k + ", year: " + v);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            Cv cv = c;
            System.out.println(width + " " + height);
            stream.newLineAtOffset(width / 8, height - padding);
            stream.setRenderingMode(RenderingMode.FILL_STROKE);
            stream.showText(cv.getFullName() + "'s CV");
            stream.newLine();
            stream.newLineAtOffset(0, -padding*2);
            stream.showText("• Full name: ");
            stream.setRenderingMode(RenderingMode.FILL);
            stream.showText(cv.getFullName());
            stream.newLine();
            stream.newLineAtOffset(0, -padding*2);
            stream.setRenderingMode(RenderingMode.FILL_STROKE);
            stream.showText("• Birth year: ");
            stream.setRenderingMode(RenderingMode.FILL);
            stream.showText("" + cv.getBirthYear());
            stream.newLine();
            stream.newLineAtOffset(0, -padding*2);
            stream.setRenderingMode(RenderingMode.FILL_STROKE);
            stream.showText("• GPA: ");
            stream.setRenderingMode(RenderingMode.FILL);
            stream.showText(cv.getGpa() + "");
            stream.newLine();
            stream.newLineAtOffset(0, -padding*2);
            stream.setRenderingMode(RenderingMode.FILL_STROKE);
            stream.showText("• Email: ");
            stream.setRenderingMode(RenderingMode.FILL);
            stream.showText(cv.getHomeAddress());
            stream.newLine();
            stream.newLineAtOffset(0, -padding*2);
            stream.setRenderingMode(RenderingMode.FILL_STROKE);
            stream.showText("• Description: ");
            stream.setRenderingMode(RenderingMode.FILL);
            stream.showText(cv.getHomeAddress());
            stream.newLine();
            stream.newLineAtOffset(0, -padding*2);
            stream.setRenderingMode(RenderingMode.FILL_STROKE);
            stream.showText("• Home address: ");
            stream.setRenderingMode(RenderingMode.FILL);
            stream.showText(cv.getHomeAddress());
            stream.newLine();
            stream.newLineAtOffset(0, -padding*2);
            stream.setRenderingMode(RenderingMode.FILL_STROKE);
            stream.showText("• Phone: ");
            stream.setRenderingMode(RenderingMode.FILL);
            stream.showText(cv.getPhone());
            stream.newLine();
            stream.newLineAtOffset(0, -padding*2);
            stream.setRenderingMode(RenderingMode.FILL_STROKE);
            stream.showText("• Website: ");
            stream.setRenderingMode(RenderingMode.FILL);
            stream.showText(cv.getWebsite());
            stream.newLine();
            stream.newLineAtOffset(0, -padding*2);
            stream.setRenderingMode(RenderingMode.FILL_STROKE);
            stream.showText("• SKILLS:");
            stream.setRenderingMode(RenderingMode.FILL);
            cv.getSkills().forEach((k, v) -> {
                try {
                    stream.newLine();
                    stream.newLineAtOffset(0, -padding*2);
                    stream.showText(k);
                    stream.showText(", proficiency: " + v);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            stream.newLineAtOffset(0, -padding*2);
            stream.newLine();
            stream.setRenderingMode(RenderingMode.FILL_STROKE);
            stream.showText("• EDUCATION:");
            stream.setRenderingMode(RenderingMode.FILL);
            cv.getEducation().forEach((k, v) -> {
                try {
                    stream.newLine();
                    stream.newLineAtOffset(0, -padding*2);
                    stream.showText(k);
                    stream.showText(", register year: " + v);
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
        document.close();
    }
}
