package oleksandrdiachenko.pricechecker.service;

import oleksandrdiachenko.pricechecker.model.magazine.Magazine;
import oleksandrdiachenko.pricechecker.service.excel.Excel;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

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

    public Workbook getWorkbook(byte[] bytes, Integer urlColumn, Integer insertColumn)
            throws IOException, InvalidFormatException {
        List<List<String>> table = excel.read(bytes);
        for (List<String> row : table) {
            if (row.size() < urlColumn) {
                continue;
            }
            String url = String.valueOf(row.get(urlColumn - 1));
            for (Magazine magazine : magazines) {
                if (magazine.isThisWebsite(url)) {
                    String price = magazine.getPrice(magazine.getDocument(url));
                    insert(row, insertColumn - 1, price);
                }
            }
        }
        return excel.write(table);
    }

    private void insert(List<String> row, int column, String price) {
        while (row.size() <= column) {
            row.add("");
        }
        row.set(column, price);
    }
}
