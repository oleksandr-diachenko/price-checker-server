package oleksandrdiachenko.pricechecker.model.entity;

public enum Status {

    ACCEPTED("Accepted"),
    PENDING("Pending"),
    COMPLETED("Completed");

    private String status;

    Status(String status) {
        this.status = status;
    }
}
