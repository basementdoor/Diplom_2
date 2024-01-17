package diplom2.user;

import diplom2.models.User;

import static diplom2.utils.Utils.faker;

public class UserGenearator {

    public static User randomUser() {
        return new User()
                .withEmail(faker.internet().emailAddress())
                .withPassword(faker.internet().password(7, 10))
                .withName(faker.name().username());
    }

    public static User userWithoutName() {
        return new User()
                .withEmail(faker.internet().emailAddress())
                .withPassword(faker.internet().password(7, 10));
    }

}
