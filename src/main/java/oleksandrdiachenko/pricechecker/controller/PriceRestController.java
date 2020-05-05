package oleksandrdiachenko.pricechecker.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import oleksandrdiachenko.pricechecker.model.PriceCheckParameter;
import oleksandrdiachenko.pricechecker.model.entity.File;
import oleksandrdiachenko.pricechecker.service.FileService;
import oleksandrdiachenko.pricechecker.service.FileStatusService;
import oleksandrdiachenko.pricechecker.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import java.util.Collections;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@Validated
@Slf4j
class PriceRestController {

    private final QueueService queueService;
    private final FileStatusService fileStatusService;
    private final FileService fileService;

    @Autowired
    public PriceRestController(QueueService queueService, FileStatusService fileStatusService, FileService fileService) {
        this.queueService = queueService;
        this.fileStatusService = fileStatusService;
        this.fileService = fileService;
    }

    @GetMapping("/api/pricecheck/file/{id}")
    public @ResponseBody
    ResponseEntity<?> getFileById(@PathVariable(value = "id") long id) {
        Optional<File> fileOptional = fileService.getFileById(id);
        if (fileOptional.isPresent()) {
            return ResponseEntity.ok(fileOptional.get().getFile());
        }
        return new ResponseEntity<>(
                new ErrorResponse<>(Collections.singletonList("File with id: " + id + " not found")), NOT_FOUND);
    }

    @SneakyThrows
    @PostMapping(value = "/api/pricecheck")
    public ResponseEntity<?> acceptFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam("urlIndex") @Min(value = 1, message = "Url column should be greater than 0") int urlIndex,
                                        @RequestParam("insertIndex") @Min(value = 1, message = "Insert column should be greater than 0") int insertIndex) {
        PriceCheckParameter parameter = new PriceCheckParameter(file.getOriginalFilename(), urlIndex - 1, insertIndex - 1, file.getBytes());
        log.info("Added file {}", parameter);
        queueService.addToQueue(parameter);
        return ResponseEntity.accepted().build();
    }

    @GetMapping(value = "/api/pricecheck/filestatuses")
    public ResponseEntity<?> getFileStatuses() {
        return ResponseEntity.ok(fileStatusService.getAllFileStatuses());
    }
}

