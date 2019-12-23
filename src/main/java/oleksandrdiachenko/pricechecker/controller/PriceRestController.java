package oleksandrdiachenko.pricechecker.controller;

import lombok.SneakyThrows;
import oleksandrdiachenko.pricechecker.service.MainService;
import oleksandrdiachenko.pricechecker.service.PriceCheckService;
import oleksandrdiachenko.pricechecker.util.WorkbookUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class PriceRestController {

    @Autowired
    private PriceCheckService priceCheckService;
    @Autowired
    private MainService mainService;

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

    @SneakyThrows
    @PostMapping(value = "/pricecheck")
    public ResponseEntity<?> acceptFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam("urlIndex") int urlIndex,
                                        @RequestParam("insertIndex") int insertIndex) {
        if (isNotValid(file)) {
            return ResponseEntity.badRequest().body("File extension should be .xls or .xlsx");
        }
        mainService.start(file.getBytes(), urlIndex, insertIndex);
        return ResponseEntity.accepted().body("File: " + file.getOriginalFilename() + " with url index : " + urlIndex +
                " and insert index: " + insertIndex + " accepted.");
    }

    public boolean isNotValid(MultipartFile file) {
        return !file.isEmpty()
                && file.getSize() > 0
                && !file.getContentType().toLowerCase().equals("application/vnd.ms-excel")
                && !file.getContentType().toLowerCase().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }
}

