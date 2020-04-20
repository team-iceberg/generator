package iceberg.generator.services.impl;

import com.itextpdf.text.DocumentException;
import iceberg.generator.exceptions.ServiceException;
import iceberg.generator.models.Membership;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.net.URISyntaxException;

@ExtendWith(SpringExtension.class)
@RunWith(JUnitPlatform.class)
class GeneratePdfServiceImplTest {

    private GeneratePdfServiceImpl generatePdfService;

    @BeforeEach
    public void setUp() {
        generatePdfService = new GeneratePdfServiceImpl();
    }

    @Test
    void should_create_pdf() throws DocumentException, IOException, URISyntaxException, ServiceException {
        Membership membership = Membership.builder().name("TEST NAME").birthdate("01/01/1970").entryNumber("2000").address("adresse test test").city(
                "city test")
                .phoneNumber("0123456789").mail("test@test.fr").build();
        generatePdfService.createFile(membership);
    }
}