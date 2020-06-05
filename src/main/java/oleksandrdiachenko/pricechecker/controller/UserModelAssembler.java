package oleksandrdiachenko.pricechecker.controller;

import oleksandrdiachenko.pricechecker.model.entity.User;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {

    @Override
    public EntityModel<User> toModel(User user) {
        EntityModel<User> model = new EntityModel<>(user,
                linkTo(methodOn(FileStatusesRestController.class).getFileStatusById(user.getId())).withSelfRel());
        user.getRoles().forEach(role ->
                model.add(linkTo(methodOn(RoleRestController.class).getRoleById(role.getId())).withRel("role")));
        return model;
    }

    @Override
    public CollectionModel<EntityModel<User>> toCollectionModel(Iterable<? extends User> users) {
        List<EntityModel<User>> models = StreamSupport.stream(users.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.toList());
        return new CollectionModel<>(models,
                linkTo(methodOn(UserRestController.class).getAllUsers()).withSelfRel());
    }
}
