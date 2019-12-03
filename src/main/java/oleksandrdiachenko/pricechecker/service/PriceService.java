package oleksandrdiachenko.pricechecker.service;

import oleksandrdiachenko.pricechecker.model.excel.Excel;
import oleksandrdiachenko.pricechecker.model.magazine.Magazine;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author Alexander Diachenko.
 */
@Service
public class PriceService {

    @Autowired
    private Excel excel;
    @Autowired
    private List<Magazine> magazines;

    public List<List<String>> buildPriceTable(String filePath, Integer urlColumn, Integer insertColumn)
            throws IOException, InvalidFormatException {
        List<List<String>> table = excel.read(filePath);
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
        return table;
    }

    private void insert(List<String> row, int column, String price) {
        while (row.size() <= column) {
            row.add("");
        }
        row.set(column, price);
    }
}
