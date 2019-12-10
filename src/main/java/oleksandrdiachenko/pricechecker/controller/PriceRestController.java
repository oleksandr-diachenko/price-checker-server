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

    @PostMapping("/api/price-table/{urlColumnNumber}/{insertColumnNumber}")
    public @ResponseBody
    byte[] getPriceTable(@RequestParam("file") MultipartFile file,
                         @PathVariable Integer urlColumnNumber,
                         @PathVariable Integer insertColumnNumber)
            throws IOException, InvalidFormatException {
        return priceService.buildPriceTable(file.getBytes(), urlColumnNumber, insertColumnNumber);
    }
}
