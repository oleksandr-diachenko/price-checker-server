package oleksandrdiachenko.pricechecker.controller;

import oleksandrdiachenko.pricechecker.service.PriceCheckService;
import oleksandrdiachenko.pricechecker.service.WorkbookService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class PriceRestController {

    @Autowired
    private PriceCheckService priceCheckService;

    @Autowired
    private WorkbookService workbookService;

    private Workbook priceTable;

    @PostMapping("/api/price-check/{urlColumnNumber}/{insertColumnNumber}")
    public void startPriceCheck(@RequestParam("file") MultipartFile file,
                                @PathVariable Integer urlColumnNumber,
                                @PathVariable Integer insertColumnNumber)
            throws IOException, InvalidFormatException {
        priceTable = null;
        priceTable = priceCheckService.getWorkbook(file.getBytes(), urlColumnNumber, insertColumnNumber);
    }

    @GetMapping("/api/price-check/content")
    public @ResponseBody
    byte[] getPriceTable() throws IOException {
        if (priceTable != null) {
            return workbookService.getBytes(priceTable);
        }
        return new byte[0];
    }
}
