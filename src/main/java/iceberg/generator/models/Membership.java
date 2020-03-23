package iceberg.generator.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Membership {

    private int id;

    private String name;

    private String birthdate;

    private String startYear;

    private String guardianName;

    private String mail;

    private String phoneNumber;

    private String address;

    private String city;

    private boolean isMonitor;

    private boolean isOfficeMember;

    private String yearOfMedicalCertificate;

    public static final String SEPARATOR = ";";
}
