package oleksandrdiachenko.pricechecker.controller;

import oleksandrdiachenko.pricechecker.service.PriceService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;

@Controller
public class PriceController {

    @Autowired
    private PriceService priceService;

    @GetMapping("/price-table")
    public String getPriceTable(Model model) throws IOException, InvalidFormatException {
        List<List<String>> priceTable = priceService.buildPriceTable("./Book1.xlsx", 1, 2);
        model.addAttribute("priceTable", priceTable);
        return "priceTable";
    }
}
