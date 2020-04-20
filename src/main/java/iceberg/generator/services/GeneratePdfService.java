package iceberg.generator.services;

import com.itextpdf.text.DocumentException;
import iceberg.generator.exceptions.ServiceException;
import iceberg.generator.models.Membership;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

public interface GeneratePdfService {

    File createFile(Membership membership) throws IOException, DocumentException, URISyntaxException, ServiceException;
}
