package iceberg.generator.services;

import iceberg.generator.models.Membership;
import org.springframework.http.codec.multipart.FilePart;

import java.io.IOException;
import java.util.List;

public interface MembershipService {

    List<Membership> getMemberships(FilePart file) throws IOException;
}
