package iceberg.generator.controllers;

import iceberg.generator.exceptions.ServiceException;
import iceberg.generator.models.Membership;
import iceberg.generator.services.MembershipService;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Controller to manage membership
 */
@RestController
@RequestMapping("/membership")
public class MembershipController {

  private static final Logger LOGGER = LoggerFactory.getLogger(MembershipController.class);

  @Autowired
  private MembershipService membershipService;

  /**
   * Manage uploaded memberships file
   * @param file memberships file
   * @return Memberships list
   */
  @PostMapping(value = "/file")
  public ResponseEntity<List<Membership>> getMemberships(@RequestPart("file") MultipartFile file) {
    LOGGER.info("Get list of membership after treatment");
    try {
      return ResponseEntity.status(HttpStatus.OK)
          .body(membershipService.getMemberships(file.getInputStream()));
    } catch (IOException | ServiceException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}
