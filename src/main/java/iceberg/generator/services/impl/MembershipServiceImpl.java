package iceberg.generator.services.impl;

import com.google.common.base.Strings;
import iceberg.generator.exceptions.MembershipFileException;
import iceberg.generator.exceptions.ServiceException;
import iceberg.generator.models.Membership;
import iceberg.generator.services.GeneratePdfService;
import iceberg.generator.services.MembershipService;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.swing.text.DateFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link MembershipService}
 */
@Service
public class MembershipServiceImpl implements MembershipService {

  private static final Logger LOGGER = LoggerFactory.getLogger(MembershipServiceImpl.class);

  private static final String SEMI_COLON = ";";
  private static final int NAME_IDX = 2;
  private static final int BIRTHDATE_IDX = 3;
  private static final int ADDRESS_IDX = 4;
  private static final int CITY_IDX = 5;
  private static final int PHONE_IDX = 6;
  private static final int MAIL_IDX = 8;
  private static final int ENTRY_IDX = 7;
  private static final int COLUMN_NB = 17;

  @Override
  public List<Membership> getMemberships(InputStream file) throws ServiceException {
    List<Membership> extracted = new ArrayList<>();
    try {
      List<String> listFiles = readXlsxFile(file);
      if (listFiles.size() > 1) {
        listFiles.remove(0);
        extracted = listFiles.stream().map(line -> Arrays.asList(line.split(SEMI_COLON)))
            .filter(line -> !line.stream().map(String::trim).allMatch(Strings::isNullOrEmpty))
            .map(this::getEntityFromLine).filter(Objects::nonNull).collect(
                Collectors.toList());

        int nbErrors = (int) extracted.stream().filter(Membership::hasError).count();
        if (nbErrors > 0) {
          String message = String.format("Imported file has %d lines with errors", nbErrors);
          LOGGER.error(message);
          throw new MembershipFileException(message);
        }
      }
    } catch (IOException e) {
      LOGGER.error("File importation failed", e);
      throw new ServiceException("File reading failed!", e);
    }
    return extracted;
  }

  private Membership getEntityFromLine(List<String> line) {
    return Membership.builder()
        .address(line.get(ADDRESS_IDX))
        .birthdate(line.get(BIRTHDATE_IDX))
        .city(line.get(CITY_IDX))
        .entryNumber(line.get(ENTRY_IDX))
        .mail(line.get(MAIL_IDX))
        .name(line.get(NAME_IDX))
        .phoneNumber(line.get(PHONE_IDX))
        .build();
  }

  private List<String> readXlsxFile(InputStream file) throws IOException {
    XSSFWorkbook workbook = new XSSFWorkbook(file);
    return getLines(workbook.getSheetAt(0).rowIterator());
  }

  private List<String> getLines(Iterator<Row> rowIterator) {
    List<String> listFiles = new ArrayList<>();
    while (rowIterator.hasNext()) {
      Row row = rowIterator.next();
      StringBuilder tmp = new StringBuilder();
      for (int i = 0; i < COLUMN_NB; i++) {
        Cell cell = row.getCell(i);
        tmp.append(getCellValue(cell));
      }
      if (!tmp.toString().isEmpty()) {
        listFiles.add(tmp.toString());
      }
    }
    return listFiles;
  }

  private String getCellValue(Cell cell) {
    if (cell == null) {
      return SEMI_COLON;
    } else {
      CellType cellType = cell.getCellType();
      DateFormatter dateFormatter = new DateFormatter();
      SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
      dateFormatter.setFormat(format);
      switch (cellType) {
        case NUMERIC:
          if (DateUtil.isCellDateFormatted(cell)) {
            return format.format(cell.getDateCellValue()) + SEMI_COLON;
          }
          return (int) cell.getNumericCellValue() + SEMI_COLON;
        case STRING:
          return cell.getStringCellValue().trim() + SEMI_COLON;
        case BOOLEAN:
          return cell.getBooleanCellValue() + SEMI_COLON;
        default:
          return cell.getStringCellValue().isEmpty() ? SEMI_COLON : cell + SEMI_COLON;
      }
    }
  }
}
