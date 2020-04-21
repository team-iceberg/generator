package iceberg.generator.services;

import com.itextpdf.text.DocumentException;
import iceberg.generator.exceptions.ServiceException;
import iceberg.generator.models.Family;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public interface GeneratePdfService {

    File createFile(Family family) throws IOException, DocumentException, URISyntaxException, ServiceException;
}
