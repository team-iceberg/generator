package iceberg.generator.controllers;

import com.itextpdf.text.DocumentException;
import iceberg.generator.exceptions.ServiceException;
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
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

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
            List<Membership> memberships = membershipService.getMemberships(file.getInputStream());
            List<File> files = new ArrayList<>();
            memberships.forEach(membership -> {
                try {
                    files.add(generatePdfService.createFile(membership));
                } catch (IOException | DocumentException | URISyntaxException | ServiceException e) {
                    e.printStackTrace();
                    LOGGER.error("Une erreur est survenue lors de la régénration du PDF pour le membre %s", membership.getName());
                }
            });

            return ResponseEntity.status(HttpStatus.OK).body(memberships);
        } catch (IOException | ServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
