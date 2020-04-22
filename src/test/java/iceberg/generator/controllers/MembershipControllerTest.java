package iceberg.generator.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import iceberg.generator.exceptions.ServiceException;
import iceberg.generator.models.Family;
import iceberg.generator.models.Membership;
import iceberg.generator.services.MembershipService;
import iceberg.generator.services.impl.GeneratePdfServiceImpl;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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

@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
class MembershipControllerTest {

  @InjectMocks
  private MembershipController membershipController;

  @Mock
  private MembershipService membershipService;

  @Mock
  private GeneratePdfServiceImpl generatePdfService;

  @Test
  void should_get_memberships_from_file()
      throws IOException, ServiceException {
    MultipartFile multipartFile = new MockMultipartFile("file",
        Resources.getInputStream("test.xlsx"));
    Family family1 = new Family();
    String address1 = "address 1";
    family1.setAddress(address1);
    String city1 = "city1";
    family1.setCity(city1);
    family1.setName("last name");
    String mail1 = "mail1";
    family1.setMail(mail1);
    Membership membership1a = new Membership();
    membership1a.setAddress(address1);
    membership1a.setBirthdate("birthdate 1a");
    membership1a.setCity(city1);
    membership1a.setEntryNumber("20000");
    membership1a.setMail(mail1);
    membership1a.setName("name 1a");
    membership1a.setPhoneNumber("phone 1a");

    Membership membership1b = new Membership();
    membership1b.setAddress(address1);
    membership1b.setBirthdate("birthdate 1b");
    membership1b.setCity(city1);
    membership1b.setEntryNumber("20000");
    membership1b.setMail(mail1);
    membership1b.setName("name 1b");
    membership1b.setPhoneNumber("phone 1b");
    List<Membership> memberships1 = Arrays.asList(membership1a, membership1b);
    family1.setMemberships(memberships1);

    Family family2 = new Family();
    String address2 = "address 2";
    family2.setAddress(address2);
    String city2 = "city2";
    family2.setCity(city2);
    family2.setName("last2");
    String mail2 = "mail2";
    family2.setMail(mail2);
    Membership membership2a = new Membership();
    membership2a.setAddress(address2);
    membership2a.setBirthdate("birthdate 2a");
    membership2a.setCity(city2);
    membership2a.setEntryNumber("20000");
    membership2a.setMail(mail2);
    membership2a.setName("name 2a");
    membership2a.setPhoneNumber("phone 2a");

    Membership membership2b = new Membership();
    membership2b.setAddress(address2);
    membership2b.setBirthdate("birthdate 2b");
    membership2b.setCity(city2);
    membership2b.setEntryNumber("20000");
    membership2b.setMail(mail2);
    membership2b.setName("name 2b");
    membership2b.setPhoneNumber("phone 2b");
    List<Membership> memberships2 = Arrays.asList(membership2a, membership2b);
    family2.setMemberships(memberships2);
    List<Family> families = Arrays.asList(family1, family2);

    when(membershipService.getMemberships(any())).thenReturn(families);
    when(generatePdfService.createFile(family1)).thenCallRealMethod();
    when(generatePdfService.createFile(family2)).thenCallRealMethod();

    ResponseEntity<byte[]> listResponseEntity =
        membershipController.getRegistrations(multipartFile);

    assertThat(listResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(listResponseEntity.getBody()).isNotNull();

  }

  @Test
  void should_fail_to_get_memberships_from_file_sue_to_service_exception()
      throws IOException, ServiceException {
    MultipartFile multipartFile = new MockMultipartFile("file",
        Resources.getInputStream("test.xlsx"));

    when(membershipService.getMemberships(any(InputStream.class)))
        .thenThrow(ServiceException.class);

    ResponseEntity<byte[]> listResponseEntity =
        membershipController.getRegistrations(multipartFile);

    assertThat(listResponseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Test
  void should_fail_to_get_memberships_from_file_sue_to_DocumentException()
      throws IOException, ServiceException {
    MultipartFile multipartFile = new MockMultipartFile("file",
        Resources.getInputStream("test.xlsx"));
    Family family1 = new Family();
    String address1 = "address 1";
    family1.setAddress(address1);
    String city1 = "city1";
    family1.setCity(city1);
    family1.setName("last1");
    String mail1 = "mail1";
    family1.setMail(mail1);
    Membership membership1a = new Membership();
    membership1a.setAddress(address1);
    membership1a.setBirthdate("birthdate 1a");
    membership1a.setCity(city1);
    membership1a.setEntryNumber("20000");
    membership1a.setMail(mail1);
    membership1a.setName("name 1a");
    membership1a.setPhoneNumber("phone 1a");

    Membership membership1b = new Membership();
    membership1b.setAddress(address1);
    membership1b.setBirthdate("birthdate 1b");
    membership1b.setCity(city1);
    membership1b.setEntryNumber("20000");
    membership1b.setMail(mail1);
    membership1b.setName("name 1b");
    membership1b.setPhoneNumber("phone 1b");
    List<Membership> memberships1 = Arrays.asList(membership1a, membership1b);
    family1.setMemberships(memberships1);

    List<Family> families = Collections.singletonList(family1);

    when(membershipService.getMemberships(any())).thenReturn(families);
    when(generatePdfService.createFile(family1)).thenThrow(ServiceException.class);

    ResponseEntity<byte[]> listResponseEntity =
        membershipController.getRegistrations(multipartFile);

    assertThat(listResponseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

  }
}
