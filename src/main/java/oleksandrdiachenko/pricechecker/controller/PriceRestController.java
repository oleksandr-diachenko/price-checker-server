package oleksandrdiachenko.pricechecker.controller;

import oleksandrdiachenko.pricechecker.service.PriceService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class PriceRestController {

    @Autowired
    private PriceService priceService;

    @RequestMapping("/api/price-table/{urlColumnNumber}/{insertColumnNumber}")
    public List<List<String>> getPriceTable(@PathVariable Integer urlColumnNumber,
                                            @PathVariable Integer insertColumnNumber)
            throws IOException, InvalidFormatException {
        return priceService.buildPriceTable("./price.xlsx", urlColumnNumber, insertColumnNumber);
    }
}