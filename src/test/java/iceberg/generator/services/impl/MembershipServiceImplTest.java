package iceberg.generator.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import iceberg.generator.exceptions.MembershipFileException;
import iceberg.generator.exceptions.ServiceException;
import iceberg.generator.models.Membership;
import java.util.List;
import org.apache.commons.codec.Resources;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
class MembershipServiceImplTest {

  private MembershipServiceImpl membershipService;

  @BeforeEach
  void setUp() {
    membershipService = new MembershipServiceImpl();
  }

  @Test
  void should_get_memberships() throws ServiceException {
    List<Membership> memberships =
        membershipService.getMemberships(Resources.getInputStream("test.xlsx"));

    assertThat(memberships).isNotNull();
    assertThat(memberships.size()).isEqualTo(1);
    Membership membership = memberships.get(0);
    assertThat(membership.getName()).isEqualTo("Théo Jasmin");
    assertThat(membership.getBirthdate()).isEqualTo("02/02/2020");
    assertThat(membership.getAddress()).isEqualTo("3 rue des Vignes");
    assertThat(membership.getCity()).isEqualTo("Honolulu");
    assertThat(membership.getPhoneNumber()).isEqualTo("01 23 45 67 89");
    assertThat(membership.getEntryNumber()).isEqualTo("2000");
    assertThat(membership.getMail()).isEqualTo("theo.jasmin@gmail.com");
  }

  @Test
  void should_fail_to_get_memberships_due_to_missing_info() throws ServiceException {
    assertThrows(MembershipFileException.class, () -> {
      membershipService.getMemberships(Resources.getInputStream("test_error.xlsx"));
    });
  }
}
