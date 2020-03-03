package oleksandrdiachenko.pricechecker.service;

import lombok.extern.slf4j.Slf4j;
import oleksandrdiachenko.pricechecker.model.entity.File;
import oleksandrdiachenko.pricechecker.model.magazine.Magazine;
import oleksandrdiachenko.pricechecker.repository.FileRepository;
import oleksandrdiachenko.pricechecker.service.excel.Excel;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static oleksandrdiachenko.pricechecker.util.UrlUtils.getDomainName;
import static org.apache.commons.collections4.ListUtils.emptyIfNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * @author Alexander Diachenko.
 */
@Service
@Slf4j
public class PriceCheckService {

    private Excel excel;
    private List<Magazine> magazines;
    private FileRepository fileRepository;

    @Autowired
    public PriceCheckService(Excel excel, List<Magazine> magazines, FileRepository fileRepository) {
        this.excel = excel;
        this.magazines = magazines;
        this.fileRepository = fileRepository;
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
                    log.info("Searching price for [{}] in magazine [{}]", url, getDomainName(url));
                    String price = magazine.getPrice(magazine.getDocument(url));
                    log.info("Price for url: [{}] is [{}]", url, price);
                    insert(row, insertIndex, price);
                }
            }
        }
        log.info("Returning table: {}", table.toString());
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

    public Optional<File> getTable(long fileId) {
        return fileRepository.findById(fileId);
    }
}
