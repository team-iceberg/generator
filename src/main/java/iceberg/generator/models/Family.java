package iceberg.generator.models;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Family {

  private String lastname;

  private String mail;

  private String phoneNumber;

  private String address;

  private String city;

  private List<Membership> memberships;
}
