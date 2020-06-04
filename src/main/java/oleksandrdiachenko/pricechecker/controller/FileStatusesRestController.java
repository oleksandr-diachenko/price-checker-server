package oleksandrdiachenko.pricechecker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oleksandrdiachenko.pricechecker.model.entity.User;
import oleksandrdiachenko.pricechecker.service.FileStatusService;
import oleksandrdiachenko.pricechecker.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/pricecheck/filestatuses")
class FileStatusesRestController {

    private final FileStatusService fileStatusService;
    private final UserService userService;

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<?> getFileStatuses(@PathVariable long userId) {
        Optional<User> userOptional = userService.findById(userId);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(fileStatusService.findByUserId(userOptional.get()));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @DeleteMapping()
    public ResponseEntity<?> deleteAll() {
        fileStatusService.deleteAll();
        return ResponseEntity.ok(EMPTY);
    }
}
