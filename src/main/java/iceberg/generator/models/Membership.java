package iceberg.generator.models;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Membership {

  private String name;

  private String birthdate;

  private String mail;

  private String phoneNumber;

  private String address;

  private String city;

  private String entryNumber;

  public boolean hasError() {
    return Strings.isNullOrEmpty(name) || Strings.isNullOrEmpty(birthdate)
        || Strings.isNullOrEmpty(phoneNumber)
        || Strings.isNullOrEmpty(address) || Strings.isNullOrEmpty(city)
        || Strings.isNullOrEmpty(entryNumber);
  }
}
