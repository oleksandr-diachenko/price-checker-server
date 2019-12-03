package oleksandrdiachenko.pricechecker.controller;

import oleksandrdiachenko.pricechecker.service.PriceService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@Controller
public class TestController {

    @Autowired
    private PriceService priceService;

    @GetMapping("/")
    public String testMethod(Model model) throws IOException, InvalidFormatException {
        List<List<String>> priceTable = priceService.buildPriceTable(getFilePath("Book1.xlsx"), 1, 2);
        model.addAttribute("priceTable", priceTable);
        return "test";
    }

    private String getFilePath(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource != null) {
            return new File(resource.getFile()).getAbsolutePath();
        }
        return "";
    }
}
