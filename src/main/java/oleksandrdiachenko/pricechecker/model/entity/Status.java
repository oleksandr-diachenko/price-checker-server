package oleksandrdiachenko.pricechecker.model.entity;

public enum Status {

    ACCEPTED("Accepted"),
    PENDING("Pending"),
    COMPLETED("Completed"),
    ERROR("Error");

    private String status;

    Status(String status) {
        this.status = status;
    }
}
