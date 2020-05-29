package oleksandrdiachenko.pricechecker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oleksandrdiachenko.pricechecker.model.entity.User;
import oleksandrdiachenko.pricechecker.service.UserService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/pricecheck/users")
class UserRestController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<User>> getUserById(@PathVariable(value = "id") long id) {
        return userService.findById(id)
                .map(user -> new EntityModel<>(user,
                        linkTo(methodOn(UserRestController.class).getUserById(user.getId())).withSelfRel()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public @ResponseBody
    ResponseEntity<?> saveUser(@RequestBody User user) {
        try {
            User savedUser = userService.save(user);
            EntityModel<User> userResource = new EntityModel<>(savedUser,
                    linkTo(methodOn(UserRestController.class).getUserById(savedUser.getId())).withSelfRel());
            return ResponseEntity
                    .created(new URI(userResource.getRequiredLink(IanaLinkRelations.SELF).getHref()))
                    .body(userResource);
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body("Unable to create " + user);
        }
    }
}
