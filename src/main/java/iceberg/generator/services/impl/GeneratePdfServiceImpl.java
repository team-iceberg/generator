package iceberg.generator.services.impl;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.TextField;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import iceberg.generator.models.Membership;
import iceberg.generator.services.GeneratePdfService;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Service("generator.generatePdfService")
public class GeneratePdfServiceImpl implements GeneratePdfService {

    private static final Font FONT_TITLE_1 = FontFactory.getFont(FontFactory.HELVETICA, 20, BaseColor.BLACK);

    private static final Font FONT_TITLE_2 = FontFactory.getFont(FontFactory.HELVETICA, 16, BaseColor.BLACK);

    private static final Font FONT = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
    private static final Font FONT_BOLD = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);

    private static final Font FONT_SMALL = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
    private static final Font FONT_SMALL_BOLD = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK);
    private static final Font FONT_SMALL_OBLIQUE = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, BaseColor.BLACK);

    @Override
    public void generatePdf() throws IOException, DocumentException, URISyntaxException {
        Document document = new Document();
        document.setMargins(15, 15, 15, 5);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("iTextHelloWorld.pdf"));

        document.open();

        setHeader(document);
        setMembers(document);
        setRules(document);
        setPayment(document);

        Phrase footer = new Phrase("Association Les Sympathiques - Place Robert Devos, 59940 Neuf-Berquin - associationslessympathiques@gmail.com",
                FONT_SMALL);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, footer, 300, 10, 0);

        document.getJavaScript_onLoad();
        document.close();
    }

    private void setHeader(Document document) throws DocumentException, IOException, URISyntaxException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setTotalWidth(new float[] { 1, 4 });
        table.addCell(getLogo());
        table.addCell(getTitle());
        table.setHorizontalAlignment(0);
        document.add(table);
    }

    private PdfPCell getLogo() throws URISyntaxException, BadElementException, IOException {
        Path path = Paths.get(ClassLoader.getSystemResource("img/logo.png").toURI());
        Image logo = Image.getInstance(path.toAbsolutePath().toString());

        PdfPCell cell = new PdfPCell(logo, true);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private PdfPCell getTitle() {
        Chunk name = new Chunk("Association Les SympathiquesAssociation\n", FONT_TITLE_1);
        Paragraph namePara = new Paragraph();
        namePara.add(name);
        namePara.setAlignment(Element.ALIGN_CENTER);
        namePara.setSpacingAfter(5);

        Chunk docTitle = new Chunk("Fiche de réinscription annèe : ..........-..........", FONT_TITLE_2);
        Paragraph docTitlePara = new Paragraph();
        docTitlePara.add(docTitle);
        docTitlePara.setAlignment(Element.ALIGN_CENTER);

        DottedLineSeparator dottedline = new DottedLineSeparator();
        dottedline.setOffset(-15);
        dottedline.setGap(0f);
        dottedline.setLineWidth(-10);
        dottedline.setPercentage(50);
        docTitlePara.add(dottedline);

        Chunk generalInformation = new Chunk("Famille :\nResponsable légal :\nAdresse :\nVille :\nTél :\nMail :\n", FONT_BOLD);

        Paragraph generalInformationPara = new Paragraph();
        generalInformationPara.add(generalInformation);
        generalInformationPara.setSpacingBefore(20);

        PdfPCell cell = new PdfPCell();
        cell.addElement(namePara);
        cell.addElement(docTitlePara);
        cell.addElement(generalInformationPara);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private void setMembers(Document document) throws DocumentException {
        Chunk chunk = new Chunk("Liste des adhérents :", FONT_BOLD);
        chunk.setUnderline(0.1f, -2f);
        Paragraph para = new Paragraph();
        para.add(chunk);
        para.setAlignment(Element.ALIGN_CENTER);
        para.setSpacingAfter(5);
        document.add(para);

        PdfPTable table = new PdfPTable(4);
        table.setTotalWidth(new float[] { 3, 2, 2, 1 });
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(0);
        addMemberTableHeader(table);
        addRowByMembers(table, Arrays.asList(null,null,null,null));
        document.add(table);

        Chunk chunkSmall = new Chunk("(Pour toute nouvelle inscription voir la liste d'attente lors du dépot de la fiche de réinscription)", FONT_SMALL_OBLIQUE);
        Paragraph paraSmall = new Paragraph();
        paraSmall.add(chunkSmall);
        paraSmall.setSpacingBefore(-5);
        paraSmall.setSpacingAfter(20);
        document.add(paraSmall);
    }

    private void addMemberTableHeader(PdfPTable table) {
        Stream.of("NOM, Prénom", "Date de naissance", "1ere Inscription", "Attestation").forEach(columnTitle -> {
            Chunk chunk = new Chunk(columnTitle, FONT);
            Paragraph para = new Paragraph();
            para.add(chunk);
            para.setAlignment(Element.ALIGN_CENTER);

            PdfPCell header = new PdfPCell();
            header.setBorderWidth(1);
            header.addElement(para);
            table.addCell(header);
        });
    }

    private void addRowByMembers(PdfPTable table, List<Membership> memberships) {
        final int[] count = { 1 };
        memberships.forEach(membership -> {
            table.addCell("row "+ count[0] +", col 1");
            table.addCell("row "+ count[0] +", col 2");
            table.addCell("row "+ count[0] +", col 3");
            table.addCell("row "+ count[0] +", col 4");
            count[0]++;
        });
    }

    private void setRules(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);

        Chunk ruleText = new Chunk("J’ai pris connaissance du règlement de l’association “Les Sympathiques” et je m’engage à le respecter.\n"
                + "Je soussigné(e) : ____________________________________________________________\n"
                + "autorise l’association “Les Sympathiques” à photographier "
                + "ou filmer les membres de cette adhésion lors des cours, stages, représentations et événements et "
                + "à publier ces images sur leurs supports de communication (flyers, film du gala, site internet, page"
                + "Facebook) pour une durée illimitée.\n", FONT_SMALL);

        Chunk signature = new Chunk("Fait à : ______________________________  Le : ______________________________\n"
                + "Signature, précédée de la mention “Lu et approuvé” :\n\n", FONT_SMALL_BOLD);

        Paragraph paragraph = new Paragraph();
        paragraph.add(ruleText);
        paragraph.add(signature);
        paragraph.setMultipliedLeading(1.5f);
        paragraph.setSpacingAfter(5);

        PdfPCell cell = new PdfPCell();
        cell.addElement(paragraph);
        cell.setBorder(Rectangle.BOX);

        table.addCell(cell);
        document.add(table);
    }

    private void setPayment(Document document) throws DocumentException {
        Chunk paymentTitle = new Chunk("Moyen de paiement :", FONT_BOLD);
        paymentTitle.setUnderline(0.1f, -2f);
        Paragraph paymentTitlePara = new Paragraph();
        paymentTitlePara.add(paymentTitle);
        paymentTitlePara.setAlignment(Element.ALIGN_CENTER);
        paymentTitlePara.setSpacingBefore(20);
        document.add(paymentTitlePara);

        Chunk paymentRules = new Chunk("1ère adhèrent = cotisation 65€\n"
                + "2ème adhérent (ou plus dans la même fratrie) = cotisation 50€", FONT_SMALL);
        paymentRules.setLineHeight(-10);
        Paragraph paymentRulesPara = new Paragraph();
        paymentRulesPara.add(paymentRules);
        paymentRulesPara.setMultipliedLeading(1);
        document.add(paymentRulesPara);


        Chunk paymentNumber = new Chunk("_____ x 65€ = _____ €\n_____ x 50€ = _____ €\nTotal à payer = ______ €", FONT);
        Paragraph paymentNumberPara = new Paragraph();
        paymentNumberPara.add(paymentNumber);
        paymentNumberPara.setAlignment(Element.ALIGN_CENTER);
        paymentNumberPara.setSpacingBefore(10);
        document.add(paymentNumberPara);

        Chunk chunkSmall = new Chunk("(Paiement possible en plusieurs fois seulement si fratrie)", FONT_SMALL_OBLIQUE);
        Paragraph paraSmall = new Paragraph();
        paraSmall.add(chunkSmall);
        paraSmall.setAlignment(Element.ALIGN_CENTER);
        paraSmall.setSpacingBefore(-1);
        paraSmall.setSpacingAfter(20);
        document.add(paraSmall);

        setAdminPart(document);
    }

    private void setAdminPart(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);

        Chunk adminTitle = new Chunk("NE PAS REMPLIR - Cadre réservé à l'administration", FONT_TITLE_2);
        Paragraph paymentTitlePara = new Paragraph();
        paymentTitlePara.add(adminTitle);
        paymentTitlePara.setAlignment(Element.ALIGN_CENTER);
        paymentTitlePara.setSpacingAfter(10);

        Chunk cheque = new Chunk("[ ] Chéque(s)", FONT);
        Paragraph chequePara = new Paragraph(cheque);
        chequePara.setSpacingAfter(10);

        Chunk cash = new Chunk("[ ] Espèces : ______€", FONT);
        Paragraph cashPara = new Paragraph(cash);
        cashPara.setSpacingBefore(10);
        cashPara.setSpacingAfter(10);

        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.BOX);
        cell.addElement(paymentTitlePara);
        cell.addElement(chequePara);
        cell.addElement(getChequeTable());
        cell.addElement(cashPara);
        cell.setBackgroundColor(new BaseColor(217, 217, 217));
        cell.setBorderWidth(2);

        table.addCell(cell);
        document.add(table);
    }

    private PdfPTable getChequeTable() {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        Stream.of("Nom", "Banque", "Montant").forEach(columnTitle -> {
            Chunk chunk = new Chunk(columnTitle, FONT_BOLD);
            Paragraph para = new Paragraph();
            para.add(chunk);
            para.setAlignment(Element.ALIGN_CENTER);
            para.setSpacingAfter(5);

            PdfPCell header = new PdfPCell();
            header.setBorderWidth(1);
            header.addElement(para);
            table.addCell(header);
        });

        for (int i = 0; i < 3; i++) {
            table.addCell("\n\n");
            table.addCell("\n\n");
            table.addCell("\n\n");
        }
        return table;
    }
}
