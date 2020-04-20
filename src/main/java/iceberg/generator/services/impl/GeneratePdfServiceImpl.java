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
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import iceberg.generator.exceptions.ServiceException;
import iceberg.generator.models.Membership;
import iceberg.generator.services.GeneratePdfService;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Stream;

@Service
public class GeneratePdfServiceImpl implements GeneratePdfService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneratePdfServiceImpl.class);

    private static final Font FONT_TITLE_1 = FontFactory.getFont(FontFactory.HELVETICA, 20, BaseColor.BLACK);

    private static final Font FONT_TITLE_2 = FontFactory.getFont(FontFactory.HELVETICA, 16, BaseColor.BLACK);

    private static final Font FONT = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

    private static final Font FONT_BOLD = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);

    private static final Font FONT_SMALL = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

    private static final Font FONT_SMALL_BOLD = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK);

    private static final Font FONT_SMALL_OBLIQUE = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, BaseColor.BLACK);

    @Override
    public File createFile(Membership membership) throws DocumentException, URISyntaxException, ServiceException, FileNotFoundException {
        File file = null;
        try {
            file = File.createTempFile(Long.toString(System.currentTimeMillis()), Strings.EMPTY);
        } catch (IOException e) {
            String message = "Echec de la création du document";
            LOGGER.error(message);
            throw new ServiceException(message);
        }

        FileOutputStream fileOut = new FileOutputStream(file);
        try {
            generatePdf(fileOut, membership);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    private Document generatePdf(FileOutputStream fos, Membership membership) throws IOException, DocumentException, URISyntaxException {
        Document document = new Document();
        document.setMargins(15, 15, 15, 5);
        PdfWriter writer = PdfWriter.getInstance(document, fos);

        document.open();

        setHeader(document, membership);
        setMembers(document, membership);
        setRules(document);
        setPayment(document);

        Phrase footer = new Phrase("Association Les Sympathiques - Place Robert Devos, 59940 Neuf-Berquin - associationslessympathiques@gmail.com",
                FONT_SMALL);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, footer, 300, 10, 0);

        document.getJavaScript_onLoad();
        document.close();
        return document;
    }

    private void setHeader(Document document, Membership membership) throws DocumentException, IOException, URISyntaxException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setTotalWidth(new float[] { 1, 4 });
        table.addCell(getLogo());
        table.addCell(getTitle(membership));
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

    private PdfPCell getTitle(Membership membership) {
        Chunk title = new Chunk("Association Les SympathiquesAssociation\n", FONT_TITLE_1);
        Paragraph titleParagraph = new Paragraph();
        titleParagraph.add(title);
        titleParagraph.setAlignment(Element.ALIGN_CENTER);
        titleParagraph.setSpacingAfter(5);

        Chunk subTitle = new Chunk("Fiche de réinscription annèe : " + getYears(), FONT_TITLE_2);
        Paragraph subTitleParagraph = new Paragraph();
        subTitleParagraph.add(subTitle);
        subTitleParagraph.setAlignment(Element.ALIGN_CENTER);

        DottedLineSeparator dottedline = new DottedLineSeparator();
        dottedline.setOffset(-15);
        dottedline.setGap(0f);
        dottedline.setLineWidth(-10);
        dottedline.setPercentage(50);
        subTitleParagraph.add(dottedline);

        String family = "Famille : " + membership.getName();
        String guardian = "\nResponsable légal :";
        String address = "\nAdresse : " + membership.getAddress();
        String city = "\nVille : " + membership.getCity();
        String phone = "\nTél : " + membership.getPhoneNumber();
        String mail = "\nMail : " + membership.getMail();

        Chunk generalInformation = new Chunk(family + guardian + address + city + phone + mail, FONT_BOLD);

        Paragraph generalInformationParagraph = new Paragraph();
        generalInformationParagraph.add(generalInformation);
        generalInformationParagraph.setSpacingBefore(20);

        PdfPCell cell = new PdfPCell();
        cell.addElement(titleParagraph);
        cell.addElement(subTitleParagraph);
        cell.addElement(generalInformationParagraph);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private String getYears() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        String currentYear = String.valueOf(year);
        String nextYear = String.valueOf(year + 1);
        return currentYear + "/" + nextYear;
    }

    private void setMembers(Document document, Membership membership) throws DocumentException {
        Chunk chunk = new Chunk("Liste des adhérents :", FONT_BOLD);
        chunk.setUnderline(0.1f, -2f);
        Paragraph paragraph = new Paragraph();
        paragraph.add(chunk);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.setSpacingAfter(5);
        document.add(paragraph);

        PdfPTable table = new PdfPTable(4);
        table.setTotalWidth(new float[] { 3, 2, 2, 1 });
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(0);
        addMemberTableHeader(table);
        addRowMembers(table, Arrays.asList(membership));
        document.add(table);

        Chunk chunkSmall = new Chunk("(Pour toute nouvelle inscription voir la liste d'attente lors du dépot de la fiche de réinscription)",
                FONT_SMALL_OBLIQUE);
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

    private void addRowMembers(PdfPTable table, List<Membership> memberships) {
        final int[] count = { 1 };
        memberships.forEach(membership -> {
            table.addCell(membership.getName());
            table.addCell(membership.getBirthdate());
            table.addCell(membership.getEntryNumber());
            table.addCell("   ");
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
        Paragraph paymentTitleParagraph = new Paragraph();
        paymentTitleParagraph.add(paymentTitle);
        paymentTitleParagraph.setAlignment(Element.ALIGN_CENTER);
        paymentTitleParagraph.setSpacingBefore(20);
        document.add(paymentTitleParagraph);

        Chunk paymentRules = new Chunk("1ère adhèrent = cotisation 65€\n" + "2ème adhérent (ou plus dans la même fratrie) = cotisation 50€",
                FONT_SMALL);
        paymentRules.setLineHeight(-10);
        Paragraph paymentRulesParagraph = new Paragraph();
        paymentRulesParagraph.add(paymentRules);
        paymentRulesParagraph.setMultipliedLeading(1);
        document.add(paymentRulesParagraph);

        Chunk paymentNumber = new Chunk("_____ x 65€ = _____ €\n_____ x 50€ = _____ €\nTotal à payer = ______ €", FONT);
        Paragraph paymentNumberParagraph = new Paragraph();
        paymentNumberParagraph.add(paymentNumber);
        paymentNumberParagraph.setAlignment(Element.ALIGN_CENTER);
        paymentNumberParagraph.setSpacingBefore(10);
        document.add(paymentNumberParagraph);

        Chunk chunk = new Chunk("(Paiement possible en plusieurs fois seulement si fratrie)", FONT_SMALL_OBLIQUE);
        Paragraph paragraph = new Paragraph();
        paragraph.add(chunk);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.setSpacingBefore(-1);
        paragraph.setSpacingAfter(20);
        document.add(paragraph);

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
