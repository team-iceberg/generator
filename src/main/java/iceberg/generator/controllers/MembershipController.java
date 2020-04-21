package iceberg.generator.controllers;

import com.itextpdf.text.DocumentException;
import iceberg.generator.exceptions.ServiceException;
import iceberg.generator.models.Family;
import iceberg.generator.models.Membership;
import iceberg.generator.services.GeneratePdfService;
import iceberg.generator.services.MembershipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
    public ResponseEntity getRegistrations(@RequestPart("file") MultipartFile file) {
        LOGGER.info("Get list of membership after treatment");
        try {
            List<Family> families = membershipService.getMemberships(file.getInputStream());

            FileOutputStream fos = new FileOutputStream("sample.zip");
            ZipOutputStream zipOS = new ZipOutputStream(fos);

            families.forEach(family -> {
                try {
                    createZipFile(generatePdfService.createFile(family), zipOS);
                } catch (IOException | DocumentException | URISyntaxException | ServiceException e) {
                    e.printStackTrace();
                    LOGGER.error("Une erreur est survenue lors de la régénration du PDF pour la famille %s", family.getLastname());
                }
            });

            zipOS.close();
            fos.close();
            return ResponseEntity.status(HttpStatus.OK).body(fos);
        } catch (IOException | ServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private void createZipFile(File file, ZipOutputStream zipStream) throws IOException {
        System.out.println("Writing file : '" + file.getPath() + "' to zip file");

        FileInputStream fis = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(file.getPath());
        zipStream.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;

        while ((length = fis.read(bytes)) >= 0) {
            zipStream.write(bytes, 0, length);
        }
        zipStream.closeEntry();
        fis.close();
    }
}
