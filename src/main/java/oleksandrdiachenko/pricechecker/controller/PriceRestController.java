package oleksandrdiachenko.pricechecker.controller;

import lombok.SneakyThrows;
import oleksandrdiachenko.pricechecker.service.FileValidator;
import oleksandrdiachenko.pricechecker.service.MainService;
import oleksandrdiachenko.pricechecker.service.PriceCheckService;
import oleksandrdiachenko.pricechecker.util.WorkbookUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import java.io.IOException;
import java.util.Collections;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@Validated
class PriceRestController {

    @Autowired
    private PriceCheckService priceCheckService;
    @Autowired
    private MainService mainService;
    @Autowired
    private FileValidator fileValidator;

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
                                        @RequestParam("urlIndex") @Min(value = 1, message = "Url index should be greater than 0") int urlIndex,
                                        @RequestParam("insertIndex") @Min(value = 1, message = "Insert index should be greater than 0") int insertIndex) {
        if (!fileValidator.isValid(file)) {
            return new ResponseEntity<>(new ErrorResponse<>(Collections.singletonList("File extension should be .xls or .xlsx")), BAD_REQUEST);
        }
        mainService.start(file.getBytes(), urlIndex, insertIndex);
        return ResponseEntity.accepted().build();
    }
}

