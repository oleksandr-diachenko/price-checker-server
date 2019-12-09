package oleksandrdiachenko.pricechecker.controller;

import oleksandrdiachenko.pricechecker.service.PriceService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@RestController
public class PriceRestController {

    @Autowired
    private PriceService priceService;

    @GetMapping("/api/price-table/{urlColumnNumber}/{insertColumnNumber}")
    public List<List<String>> getPriceTable(@PathVariable Integer urlColumnNumber,
                                            @PathVariable Integer insertColumnNumber)
            throws IOException, InvalidFormatException {
        return priceService.buildPriceTable("./file/price.xlsx", urlColumnNumber, insertColumnNumber);
    }

    @PostMapping("/api/file-upload")
    public void uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        try (OutputStream out = new FileOutputStream(new File("./file/price.xlsx"))) {
            out.write(file.getBytes());
        }
    }
}
