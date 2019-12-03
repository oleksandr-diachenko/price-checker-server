package oleksandrdiachenko.pricechecker.service;

import oleksandrdiachenko.pricechecker.model.excel.Excel;
import oleksandrdiachenko.pricechecker.model.excel.ExcelImpl;
import oleksandrdiachenko.pricechecker.model.magazine.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Diachenko.
 */
@Service
public class PriceService {

    public List<List<String>> buildPriceTable(String filePath, Integer urlColumn, Integer insertColumn)
            throws IOException, InvalidFormatException {
        Excel excel = new ExcelImpl();
        List<Magazine> magazines = getMagazines();
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

    private static List<Magazine> getMagazines() {
        List<Magazine> magazines = new ArrayList<>();
        magazines.add(new Makeup());
        magazines.add(new Korea());
        magazines.add(new RoseRoseShop());
        magazines.add(new BeautyNetKorea());
        magazines.add(new NowZenith());
        magazines.add(new Rozetka());
        magazines.add(new KoreaButik());
        magazines.add(new SweetCorea());
        magazines.add(new Cosmetea());
        magazines.add(new Sweetness());
        return magazines;
    }
}
