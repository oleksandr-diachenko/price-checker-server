package oleksandrdiachenko.pricechecker.controller;

import oleksandrdiachenko.pricechecker.service.PriceCheckService;
import oleksandrdiachenko.pricechecker.util.WorkbookUtils;
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

    private Workbook priceTable;

    @PostMapping("/api/price-check/{urlIndex}/{insertIndex}")
    public void startPriceCheck(@RequestParam("file") MultipartFile file,
                                @PathVariable int urlIndex,
                                @PathVariable int insertIndex)
            throws IOException, InvalidFormatException {
        priceTable = null;
        priceTable = priceCheckService.getWorkbook(file.getBytes(), urlIndex - 1, insertIndex - 1);
    }

    @GetMapping("/api/price-check/content")
    public @ResponseBody
    byte[] getPriceTable() throws IOException {
        if (priceTable != null) {
            return WorkbookUtils.getBytes(priceTable);
        }
        return new byte[0];
    }
}
