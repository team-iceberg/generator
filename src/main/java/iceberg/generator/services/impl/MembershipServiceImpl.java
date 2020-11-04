package iceberg.generator.services.impl;

import com.google.common.base.Strings;
import iceberg.generator.exceptions.MembershipFileException;
import iceberg.generator.exceptions.ServiceException;
import iceberg.generator.models.Family;
import iceberg.generator.models.Membership;
import iceberg.generator.services.MembershipService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.swing.text.DateFormatter;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementation of {@link MembershipService}
 */
@Service
public class MembershipServiceImpl implements MembershipService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MembershipServiceImpl.class);

    private static final String SEMI_COLON = ";";

    private static final int GROUPE_IDX = 0;

    private static final int NAME_IDX = 3;

    private static final int BIRTHDATE_IDX = 4;

    private static final int ADDRESS_IDX = 5;

    private static final int CITY_IDX = 6;

    private static final int PHONE_IDX = 7;

    private static final int MAIL_IDX = 9;

    private static final int ENTRY_IDX = 8;

    private static final int COLUMN_NB = 18;

    @Override
    public List<Family> getMemberships(InputStream file) throws ServiceException {
        List<Membership> extracted = new ArrayList<>();
        try {
            List<String> listFiles = readXlsxFile(file);
            if (listFiles.size() > 1) {
                listFiles.remove(0);
                extracted = listFiles.stream().map(line -> Arrays.asList(line.split(SEMI_COLON)))
                        .filter(line -> !line.stream().map(String::trim).allMatch(Strings::isNullOrEmpty)).map(this::getEntityFromLine)
                        .filter(Objects::nonNull).collect(Collectors.toList());

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
        return groupingByAddress(extracted);
    }

    private Membership getEntityFromLine(List<String> line) {
        String entryYear = line.get(ENTRY_IDX);
        if(entryYear.equals("2020")){
            return null;
        }
        return Membership.builder().address(line.get(ADDRESS_IDX)).birthdate(line.get(BIRTHDATE_IDX)).city(line.get(CITY_IDX))
                .entryNumber(line.get(ENTRY_IDX)).mail(line.get(MAIL_IDX)).name(line.get(NAME_IDX)).groupe(line.get(GROUPE_IDX))
                .phoneNumber(line.get(PHONE_IDX)).build();
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

    private List<Family> groupingByAddress(List<Membership> extracted) {
        List<String> addresses = extracted.stream().map(Membership::getAddress).distinct().collect(Collectors.toList());

        return addresses.stream().map(s -> {
            List<Membership> memberships = extracted.stream().filter(membership -> membership.getAddress().equals(s)).collect(Collectors.toList());
            Membership oldMembership = getOldMembership(memberships);

            return Family.builder().name(oldMembership.getName()).address(s).city(oldMembership.getCity())
                    .mail(oldMembership.getMail()).phoneNumber(oldMembership.getPhoneNumber()).memberships(memberships).group(oldMembership.getGroupe()).build();
        }).collect(Collectors.toList());
    }

    private Membership getOldMembership(List<Membership> memberships) {
        final Membership[] oldMembership = { null };
        memberships.forEach(membership -> {
            try {
                if (oldMembership[0] == null) {
                    oldMembership[0] = membership;
                } else {
                    Date oldMembershipBirthdate = new SimpleDateFormat("dd/MM/yyyy").parse(oldMembership[0].getBirthdate());
                    Date membershipBirthdate = new SimpleDateFormat("dd/MM/yyyy").parse(membership.getBirthdate());

                    if (membershipBirthdate.compareTo(oldMembershipBirthdate) < 0) {
                        oldMembership[0] = membership;
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
                LOGGER.error("Une erreur est survenue lors de la récupération du membre le vieux", e);
            }
        });

        return oldMembership[0];
    }
}
