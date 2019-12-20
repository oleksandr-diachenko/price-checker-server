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

    public Workbook getWorkbook(byte[] bytes, int urlIndex, int insertIndex)
            throws IOException, InvalidFormatException {
        List<List<String>> table = excel.read(bytes);
        if (!isPositives(urlIndex, insertIndex)) {
            return excel.write(table);
        }
        for (List<String> row : table) {
            for (Magazine magazine : emptyIfNull(magazines)) {
                String url = retrieveUrl(row, urlIndex);
                if (magazine.isThisWebsite(url)) {
                    String price = magazine.getPrice(magazine.getDocument(url));
                    insert(row, insertIndex, price);
                }
            }
        }
        return excel.write(table);
    }

    private boolean isPositives(int urlColumn, int insertColumn) {
        return urlColumn >= 0 && insertColumn >= 0;
    }

    private String retrieveUrl(List<String> row, int index) {
        if (row.size() <= index) {
            return EMPTY;
        }
        return row.get(index);
    }

    private void insert(List<String> row, int index, String price) {
        while (row.size() < index) {
            row.add(EMPTY);
        }
        row.add(index, price);
    }
}
