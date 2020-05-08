package oleksandrdiachenko.pricechecker.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import oleksandrdiachenko.pricechecker.model.PriceCheckParameter;
import oleksandrdiachenko.pricechecker.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;

@RestController
@Validated
@Slf4j
@RequestMapping("/api/pricecheck")
class PriceRestController {

    private final QueueService queueService;

    @Autowired
    public PriceRestController(QueueService queueService) {
        this.queueService = queueService;
    }

    @SneakyThrows
    @PostMapping()
    public ResponseEntity<?> acceptFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam("urlIndex") @Min(value = 1, message = "Url column should be greater than 0") int urlIndex,
                                        @RequestParam("insertIndex") @Min(value = 1, message = "Insert column should be greater than 0") int insertIndex) {
        PriceCheckParameter parameter = new PriceCheckParameter(file.getOriginalFilename(), urlIndex - 1, insertIndex - 1, file.getBytes());
        log.info("Added file {}", parameter);
        queueService.addToQueue(parameter);
        return ResponseEntity.accepted().build();
    }
}
