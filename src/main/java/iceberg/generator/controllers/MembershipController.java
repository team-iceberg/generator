package iceberg.generator.controllers;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

import com.itextpdf.text.DocumentException;
import iceberg.generator.exceptions.ServiceException;
import iceberg.generator.models.Family;
import iceberg.generator.services.GeneratePdfService;
import iceberg.generator.services.MembershipService;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Controller to manage membership
 */
@RestController
@RequestMapping("/membership")
public class MembershipController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MembershipController.class);

    @Autowired
    private MembershipService membershipService;

    @Autowired
    private GeneratePdfService generatePdfService;

    /**
     * Manage uploaded memberships file
     *
     * @param file memberships file
     * @return Memberships list
     */
    @PostMapping(value = "/file")
    public ResponseEntity<byte[]> getRegistrations(@RequestPart("file") MultipartFile file) {
        LOGGER.info("Get list of membership after treatment");
        try {
            List<Family> families = membershipService.getMemberships(file.getInputStream());

            File zip = Files.createTempFile("tmp", ".zip").toFile();
            FileOutputStream fos = new FileOutputStream(zip);
            ZipOutputStream zipOut = new ZipOutputStream(fos);

            families.forEach(family -> createZipOutputStream(generatePdfService.createFile(family), zipOut));

            zipOut.close();
            fos.close();
            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/zip"))
                .header(CONTENT_DISPOSITION, "attachment; filename=\"" + zip.getName() + "\"")
                .body(Files.readAllBytes(zip.toPath()));
        } catch (IOException | ServiceException e) {
            LOGGER.error("Echec de l'export des fichiers de réinscription", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private void createZipOutputStream(File file, ZipOutputStream zipOut) throws ServiceException {
        try (FileInputStream fis = new FileInputStream(file)) {
            String name = file.getName().split("_")[0] + ".pdf";
            ZipEntry zipEntry = new ZipEntry(name);
            zipOut.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
        } catch (IOException ioe) {
            LOGGER.error("Failed to create temporary zip file", ioe);
            throw new ServiceException("Failed to create temporary zip file");
        }
    }
}
