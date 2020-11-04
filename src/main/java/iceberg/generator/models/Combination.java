package iceberg.generator.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Setter
@Getter
public class Combination {

    List<Object> graduates;

    String combination;

    Integer totalLength;

    Integer differentWith3000;
}
