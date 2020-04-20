package iceberg.generator.controllers;

import com.itextpdf.text.DocumentException;
import iceberg.generator.exceptions.ServiceException;
import iceberg.generator.models.Membership;
import iceberg.generator.services.GeneratePdfService;
import iceberg.generator.services.MembershipService;
import org.apache.commons.codec.Resources;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
class MembershipControllerTest {

    @InjectMocks
    private MembershipController membershipController;

    @Mock
    private MembershipService membershipService;

    @Mock
    private GeneratePdfService generatePdfService;

    @Test
    void should_get_memberships_from_file() throws IOException, ServiceException, URISyntaxException, DocumentException {
        MultipartFile multipartFile = new MockMultipartFile("file", Resources.getInputStream("test.xlsx"));
        List<Membership> memberships = new ArrayList<>();
        File file = mock(File.class);

        when(membershipService.getMemberships(multipartFile.getInputStream())).thenReturn(memberships);
        when(generatePdfService.createFile(any(Membership.class))).thenReturn(file);

        ResponseEntity<List<Membership>> listResponseEntity = membershipController.getRegistrations(multipartFile);

        assertThat(listResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(listResponseEntity.getBody()).isEqualTo(memberships);

    }

    @Test
    void should_fail_to_get_memberships_from_file_sue_to_service_exception()
            throws IOException, ServiceException {
        MultipartFile multipartFile = new MockMultipartFile("file", Resources.getInputStream("test.xlsx"));

        when(membershipService.getMemberships(any(InputStream.class))).thenThrow(ServiceException.class);

        ResponseEntity<List<Membership>> listResponseEntity = membershipController.getRegistrations(multipartFile);

        assertThat(listResponseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
