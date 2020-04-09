package iceberg.generator.services;

import com.itextpdf.text.DocumentException;

import java.io.IOException;
import java.net.URISyntaxException;

public interface GeneratePdfService {

    void generatePdf() throws IOException, DocumentException, URISyntaxException;
}
