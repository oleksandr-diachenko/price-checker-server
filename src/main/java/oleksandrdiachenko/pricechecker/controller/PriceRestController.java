package oleksandrdiachenko.pricechecker.controller;

import oleksandrdiachenko.pricechecker.service.PriceService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class PriceRestController {

    @Autowired
    private PriceService priceService;

    private byte[] priceTable;

    @PostMapping("/api/price-check/{urlColumnNumber}/{insertColumnNumber}")
    public void startPriceCheck(@RequestParam("file") MultipartFile file,
                                @PathVariable Integer urlColumnNumber,
                                @PathVariable Integer insertColumnNumber)
            throws IOException, InvalidFormatException {
        priceTable = null;
        priceTable = priceService.buildPriceTable(file.getBytes(), urlColumnNumber, insertColumnNumber);
    }

    @GetMapping("/api/price-check/content")
    public @ResponseBody byte[] getPriceTable() {
        if (priceTable != null) {
            return priceTable;
        }
        return new byte[0];
    }
}
