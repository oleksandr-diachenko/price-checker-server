package oleksandrdiachenko.pricechecker.controller;

import oleksandrdiachenko.pricechecker.model.entity.FileStatus;
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
public class FileStatusesModelAssembler implements RepresentationModelAssembler<FileStatus, EntityModel<FileStatus>> {

    @Override
    public EntityModel<FileStatus> toModel(FileStatus fileStatus) {
        return new EntityModel<>(fileStatus,
                linkTo(methodOn(FileStatusesRestController.class).getFileStatusById(fileStatus.getId())).withSelfRel());
    }

    @Override
    public CollectionModel<EntityModel<FileStatus>> toCollectionModel(Iterable<? extends FileStatus> fileStatuses) {
        List<EntityModel<FileStatus>> models = StreamSupport.stream(fileStatuses.spliterator(), false)
                .map(fileStatus -> new EntityModel<FileStatus>(fileStatus,
                        linkTo(methodOn(FileStatusesRestController.class).getFileStatusById(fileStatus.getId())).withSelfRel(),
                        linkTo(methodOn(FileStatusesRestController.class).getAllFileStatuses()).withRel("filestatuses")))
                .collect(Collectors.toList());
        return new CollectionModel<>(models,
                linkTo(methodOn(FileStatusesRestController.class).getAllFileStatuses()).withSelfRel());
    }
}
