package iceberg.generator.services.impl;

import iceberg.generator.exceptions.ServiceException;
import iceberg.generator.services.GraduationService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Implementation of {@link GraduationService}
 */
@Service
public class GraduationServiceImpl implements GraduationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraduationServiceImpl.class);

    private static final String SEMI_COLON = ";";

    private static final int COLUMN_NB = 2;

    @Override
    public Map<String, Integer> getGraduations(InputStream file) throws ServiceException {
        Map<String, Integer> extracted = new HashMap<>();
        try {
            extracted = readXlsxFile(file);
        } catch (IOException e) {
            LOGGER.error("File importation failed", e);
            throw new ServiceException("File reading failed!", e);
        }
        return extracted;
    }

    private Map<String, Integer> readXlsxFile(InputStream file) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        return getLines(workbook.getSheetAt(0).rowIterator());
    }

    private Map<String, Integer> getLines(Iterator<Row> rowIterator) {
        Map<String, Integer> graduations = new HashMap<>();
        //Add the below line
        rowIterator.next();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Cell graduateCell = row.getCell(0);
            Cell lengthCell = row.getCell(1);
            graduations.put(getStringCellValue(graduateCell), getIntegerCellValue(lengthCell));
        }
        return graduations;
    }

    private String getStringCellValue(Cell cell) {
        if (cell == null) {
            return SEMI_COLON;
        } else {
            return cell.getStringCellValue().trim();
        }
    }

    private Integer getIntegerCellValue(Cell cell) {
        if (cell == null) {
            return null;
        } else {
            return (int) cell.getNumericCellValue();
        }
    }
}
