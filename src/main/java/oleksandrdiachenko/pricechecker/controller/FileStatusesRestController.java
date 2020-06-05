package oleksandrdiachenko.pricechecker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oleksandrdiachenko.pricechecker.controller.exception.EntityNotFoundException;
import oleksandrdiachenko.pricechecker.model.entity.FileStatus;
import oleksandrdiachenko.pricechecker.model.entity.User;
import oleksandrdiachenko.pricechecker.service.FileStatusService;
import oleksandrdiachenko.pricechecker.service.UserService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/pricecheck/filestatuses")
class FileStatusesRestController {

    private final FileStatusService fileStatusService;
    private final UserService userService;
    private final FileStatusesModelAssembler assembler;

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/users/{userId}")
    public CollectionModel<EntityModel<FileStatus>> getFileStatusesByUserId(@PathVariable long userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("file status", userId));

        List<EntityModel<FileStatus>> employees = fileStatusService.findByUserId(user).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return new CollectionModel<>(employees,
                linkTo(methodOn(FileStatusesRestController.class).getFileStatusesByUserId(userId)).withSelfRel());
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public EntityModel<FileStatus> getFileStatusById(@PathVariable long id) {
        FileStatus fileStatus = fileStatusService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("file status", id));
        return assembler.toModel(fileStatus);
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping()
    public CollectionModel<EntityModel<FileStatus>> getAllFileStatuses() {
        List<FileStatus> fileStatus = fileStatusService.findAll();
        return assembler.toCollectionModel(fileStatus);
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @DeleteMapping()
    public ResponseEntity<?> deleteAll() {
        fileStatusService.deleteAll();
        return ResponseEntity.ok(EMPTY);
    }
}
