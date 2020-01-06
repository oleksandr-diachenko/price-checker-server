package oleksandrdiachenko.pricechecker.controller;

import lombok.SneakyThrows;
import oleksandrdiachenko.pricechecker.model.PriceCheckParameter;
import oleksandrdiachenko.pricechecker.service.FileStatusService;
import oleksandrdiachenko.pricechecker.service.PriceCheckService;
import oleksandrdiachenko.pricechecker.service.QueueService;
import oleksandrdiachenko.pricechecker.service.validator.FileValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import java.util.Collections;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@Validated
class PriceRestController {

    private PriceCheckService priceCheckService;
    private FileValidator fileValidator;
    private QueueService queueService;
    private FileStatusService fileStatusService;

    @Autowired
    public PriceRestController(PriceCheckService priceCheckService, FileValidator fileValidator, QueueService queueService, FileStatusService fileStatusService) {
        this.priceCheckService = priceCheckService;
        this.fileValidator = fileValidator;
        this.queueService = queueService;
        this.fileStatusService = fileStatusService;
    }

    @GetMapping("/api/price-check/file/{id}")
    public @ResponseBody
    byte[] getTable(@PathVariable(value = "id") long id) {
        return priceCheckService.getTable(id).get().getFile();
    }

    @SneakyThrows
    @PostMapping(value = "/api/pricecheck")
    public ResponseEntity<?> acceptFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam("urlIndex") @Min(value = 1, message = "Url column should be greater than 0") int urlIndex,
                                        @RequestParam("insertIndex") @Min(value = 1, message = "Insert column should be greater than 0") int insertIndex) {
        if (!fileValidator.isValid(file)) {
            return new ResponseEntity<>(new ErrorResponse<>(Collections.singletonList("File extension should be .xls or .xlsx")), BAD_REQUEST);
        }
        queueService.start(new PriceCheckParameter(file.getOriginalFilename(), urlIndex - 1, insertIndex - 1, file.getBytes()));
        return ResponseEntity.accepted().build();
    }

    @GetMapping(value = "/api/pricecheck/filestatuses")
    public ResponseEntity<?> getFileStatuses() {
        return ResponseEntity.ok(fileStatusService.getAllFileStatuses());
    }
}

