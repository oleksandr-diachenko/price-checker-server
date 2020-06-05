package oleksandrdiachenko.pricechecker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oleksandrdiachenko.pricechecker.controller.exception.EntityNotFoundException;
import oleksandrdiachenko.pricechecker.model.entity.Role;
import oleksandrdiachenko.pricechecker.service.RoleService;
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
@RequestMapping("/api/pricecheck/roles")
public class RoleRestController {

    private final RoleService roleService;
    private final RoleModelAssembler assembler;


    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public EntityModel<Role> getRoleById(@PathVariable long id) {
        Role role = roleService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("role", id));
        return assembler.toModel(role);
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping()
    public CollectionModel<EntityModel<Role>> getAllRoles() {
        List<Role> roles = roleService.findAll();
        return assembler.toCollectionModel(roles);
    }
}
