package oleksandrdiachenko.pricechecker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oleksandrdiachenko.pricechecker.controller.exception.EntityNotFoundException;
import oleksandrdiachenko.pricechecker.model.entity.User;
import oleksandrdiachenko.pricechecker.service.UserService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/pricecheck/users")
public class UserRestController {

    private final UserService userService;
    private final UserModelAssembler assembler;


    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public EntityModel<User> getUserById(@PathVariable long id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user", id));
        return assembler.toModel(user);
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping()
    public CollectionModel<EntityModel<User>> getAllUsers() {
        List<User> fileStatus = userService.findAll();
        return assembler.toCollectionModel(fileStatus);
    }
}
