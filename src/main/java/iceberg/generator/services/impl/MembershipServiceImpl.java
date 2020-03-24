package iceberg.generator.services.impl;

import iceberg.generator.models.Membership;
import iceberg.generator.services.MembershipService;
import org.springframework.http.codec.multipart.FilePart;

import java.util.List;

public class MembershipServiceImpl implements MembershipService {

    @Override
    public List<Membership> getMemberships(FilePart file) {
        return null;
    }
}
