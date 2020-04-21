package iceberg.generator.services.impl;

import iceberg.generator.exceptions.ServiceException;
import iceberg.generator.models.Family;
import iceberg.generator.models.Membership;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@RunWith(JUnitPlatform.class)
class GeneratePdfServiceImplTest {

  private GeneratePdfServiceImpl generatePdfService;

  @BeforeEach
  public void setUp() {
    generatePdfService = new GeneratePdfServiceImpl();
  }

  @Test
  void should_create_pdf() throws ServiceException {
    Membership membershipA = Membership.builder().name("TEST A").birthdate("01/01/1970")
        .entryNumber("2000").address("adresse test test")
        .city("city test").phoneNumber("0123456789").mail("test@test.fr").build();

    Membership membershipB = Membership.builder().name("TEST B").birthdate("01/01/1970")
        .entryNumber("2000").address("adresse test test")
        .city("city test").phoneNumber("0123456789").mail("test@test.fr").build();

    Membership membershipC = Membership.builder().name("TEST C").birthdate("01/01/1970")
        .entryNumber("2000").address("adresse test test")
        .city("city test").phoneNumber("0123456789").mail("test@test.fr").build();

    Membership membershipD = Membership.builder().name("TEST D").birthdate("01/01/1970")
        .entryNumber("2000").address("adresse test test")
        .city("city test").phoneNumber("0123456789").mail("test@test.fr").build();

    Membership membershipE = Membership.builder().name("TEST E").birthdate("01/01/1970")
        .entryNumber("2000").address("adresse test test")
        .city("city test").phoneNumber("0123456789").mail("test@test.fr").build();

    Family family = Family.builder().lastname("TEST").address("adresse test test").city("city test")
        .phoneNumber("0123456789")
        .mail("test@test.fr")
        .memberships(Arrays.asList(membershipA, membershipB, membershipC, membershipD, membershipE))
        .build();

    generatePdfService.createFile(family);
  }
}
