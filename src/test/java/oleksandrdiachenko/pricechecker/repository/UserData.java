package oleksandrdiachenko.pricechecker.repository;

import oleksandrdiachenko.pricechecker.model.entity.User;

public class UserData {

    public static User create() {
        User user = new User();
        user.setNickname("POSITIV");
        user.setPassword("12345");
        user.setEmail("mail@mail.com");
        user.setPhone("+222 (22) 222-22-22");
        return user;
    }
}
