package iceberg.generator.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to manage membership
 */
@RestController
@RequestMapping("/membership")
public class MembershipController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MembershipController.class);

    @GetMapping
    public ResponseEntity getMemberships() {
        LOGGER.info("Get list of membership after treatment");
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
