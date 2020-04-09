package iceberg.generator.services.impl;

import com.itextpdf.text.DocumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

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
    void should_create_pdf() throws DocumentException, IOException, URISyntaxException {
        generatePdfService.generatePdf();
    }
}