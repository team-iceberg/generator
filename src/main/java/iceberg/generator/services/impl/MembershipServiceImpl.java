package iceberg.generator.services.impl;

import iceberg.generator.models.Membership;
import iceberg.generator.services.MembershipService;
import org.apache.tomcat.util.digester.Rule;
import org.springframework.http.codec.multipart.FilePart;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MembershipServiceImpl implements MembershipService {

    @Override
    public List<Membership> getMemberships(FilePart file) {
        BufferedInputStream inputStream = convertFilePartToInputStream(file);

        List<Membership> result = convertInputStreamToRules(inputStream);
        return null;
    }

    private BufferedInputStream convertFilePartToInputStream(FilePart file) {
        final BufferedInputStream[] inputStreams = new BufferedInputStream[1];
        file.content().subscribe(dataBuffer -> {
            BufferedInputStream dataBufferAsInputStream = new BufferedInputStream(dataBuffer.asInputStream());
            inputStreams[0] = inputStreams[0] == null ? dataBufferAsInputStream : new BufferedInputStream(new SequenceInputStream(inputStreams[0], dataBufferAsInputStream));
        });
        return inputStreams[0];
    }

    private List<Map<String, Object>> convertInputStreamToRules(BufferedInputStream file) throws IOException {
        List<Map<String, Object>> rules;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file));
        rules = bufferedReader.lines()
                .skip(1)
                .map(line -> Membership.convertCSVLineToParams(line))
                .collect(Collectors.toList());
        // Blocking line
        bufferedReader.close();
        return rules;
    }
}
