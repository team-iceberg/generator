package iceberg.generator.services;

import iceberg.generator.models.Family;
import java.io.File;

public interface GeneratePdfService {

  File createFile(Family family);
}
