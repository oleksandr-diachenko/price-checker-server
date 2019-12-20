package oleksandrdiachenko.pricechecker.service;

import oleksandrdiachenko.pricechecker.model.magazine.Magazine;
import oleksandrdiachenko.pricechecker.service.excel.Excel;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * @author Alexander Diachenko.
 */
@Service
public class PriceCheckService {

    private Excel excel;
    private List<Magazine> magazines;

    @Autowired
    public PriceCheckService(Excel excel, List<Magazine> magazines) {
        this.excel = excel;
        this.magazines = magazines;
    }

    public Workbook getWorkbook(byte[] bytes, int urlColumn, int insertColumn)
            throws IOException, InvalidFormatException {
        List<List<String>> table = excel.read(bytes);
        for (List<String> row : table) {
            for (Magazine magazine : emptyIfNull(magazines)) {
                String url = retrieveUrl(urlColumn, row);
                if (magazine.isThisWebsite(url)) {
                    String price = magazine.getPrice(magazine.getDocument(url));
                    insert(row, insertColumn - 1, price);
                }
            }
        }
        return excel.write(table);
    }

    private String retrieveUrl(int urlColumn, List<String> row) {
        return String.valueOf(row.size() < urlColumn ? "" : row.get(urlColumn - 1));
    }

    private void insert(List<String> row, int column, String price) {
        while (row.size() < column) {
            row.add(EMPTY);
        }
        row.add(column, price);
    }
}
