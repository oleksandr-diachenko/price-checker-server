package oleksandrdiachenko.pricechecker.controller;

import oleksandrdiachenko.pricechecker.service.PriceService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.FileInputStream;
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

    @GetMapping("/downloadPriceTable")
    public ResponseEntity<InputStreamResource> downloadFile() throws IOException {
        File file = new File("C:\\Users\\Oleksandr_Diachenko\\IdeaProjects\\price-checker-web\\Book1.xlsx");
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=" + file.getName())
                .contentType(MediaType.APPLICATION_PDF).contentLength(file.length())
                .body(resource);
    }
}
