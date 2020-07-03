package oleksandrdiachenko.pricechecker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oleksandrdiachenko.pricechecker.controller.exception.EntityNotFoundException;
import oleksandrdiachenko.pricechecker.model.entity.FileStatus;
import oleksandrdiachenko.pricechecker.model.entity.User;
import oleksandrdiachenko.pricechecker.service.dataservice.FileStatusService;
import oleksandrdiachenko.pricechecker.service.dataservice.UserService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/pricecheck/users")
public class UserRestController {

    private final UserService userService;
    private final UserModelAssembler userModelAssembler;
    private final FileStatusesModelAssembler fileStatusesModelAssembler;
    private final FileStatusService fileStatusService;


    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public EntityModel<User> getUserById(@PathVariable long id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user", id));
        return userModelAssembler.toModel(user);
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping()
    public CollectionModel<EntityModel<User>> getAllUsers() {
        List<User> fileStatus = userService.findAll();
        return userModelAssembler.toCollectionModel(fileStatus);
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/{userId}/filestatuses")
    public CollectionModel<EntityModel<FileStatus>> getFileStatusesByUserId(@PathVariable long userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("file status", userId));

        List<EntityModel<FileStatus>> employees = fileStatusService.findByUser(user).stream()
                .map(fileStatusesModelAssembler::toModel)
                .collect(Collectors.toList());

        return new CollectionModel<>(employees,
                linkTo(methodOn(UserRestController.class).getFileStatusesByUserId(userId)).withSelfRel());
    }
}
