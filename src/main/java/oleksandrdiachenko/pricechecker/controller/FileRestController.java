package oleksandrdiachenko.pricechecker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oleksandrdiachenko.pricechecker.model.entity.File;
import oleksandrdiachenko.pricechecker.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/pricecheck/files")
class FileRestController {

    private final FileService fileService;

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public @ResponseBody
    ResponseEntity<?> getFileById(@PathVariable(value = "id") long id) {
        Optional<File> fileOptional = fileService.findById(id);
        if (fileOptional.isPresent()) {
            return ResponseEntity.ok(fileOptional.get().getFile());
        }
        return new ResponseEntity<>(
                new ErrorResponse<>(Collections.singletonList("File with id: " + id + " not found")), NOT_FOUND);
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @DeleteMapping()
    public ResponseEntity<?> deleteAll() {
        fileService.deleteAll();
        return ResponseEntity.ok(EMPTY);
    }
}
