package oleksandrdiachenko.pricechecker.controller;

import lombok.extern.slf4j.Slf4j;
import oleksandrdiachenko.pricechecker.service.FileStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@Slf4j
@RequestMapping("/api/pricecheck/filestatuses")
class FileStatusesRestController {

    private final FileStatusService fileStatusService;

    @Autowired
    public FileStatusesRestController(FileStatusService fileStatusService) {
        this.fileStatusService = fileStatusService;
    }

    @GetMapping()
    public ResponseEntity<?> getFileStatuses() {
        return ResponseEntity.ok(fileStatusService.findAll());
    }
}
