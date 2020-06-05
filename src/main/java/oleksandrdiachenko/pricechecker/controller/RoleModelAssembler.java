package oleksandrdiachenko.pricechecker.controller;

import oleksandrdiachenko.pricechecker.model.entity.Role;
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
public class RoleModelAssembler implements RepresentationModelAssembler<Role, EntityModel<Role>> {

    @Override
    public EntityModel<Role> toModel(Role role) {
        return new EntityModel<>(role,
                linkTo(methodOn(RoleRestController.class).getRoleById(role.getId())).withSelfRel());
    }

    @Override
    public CollectionModel<EntityModel<Role>> toCollectionModel(Iterable<? extends Role> roles) {
        List<EntityModel<Role>> models = StreamSupport.stream(roles.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.toList());
        return new CollectionModel<>(models,
                linkTo(methodOn(FileStatusesRestController.class).getAllFileStatuses()).withSelfRel());
    }
}
