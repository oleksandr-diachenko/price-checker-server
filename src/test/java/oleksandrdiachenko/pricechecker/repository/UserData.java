package oleksandrdiachenko.pricechecker.repository;

import oleksandrdiachenko.pricechecker.model.entity.User;

public class UserData {

    public static User create() {
        User user = new User();
        user.setUsername("POSITIV");
        user.setPassword("12345");
        user.setEmail("mail@mail.com");
        return user;
    }
}
