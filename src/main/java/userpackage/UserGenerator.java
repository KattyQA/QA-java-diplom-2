package userpackage;

import com.github.javafaker.Faker;


public class UserGenerator {

    public static User randomUser() {
        Faker faker = new Faker();
        return new User()
                .withEmail(faker.internet().emailAddress())
                .withPassword(faker.letterify("????????"))
                .withName(faker.name().name());
    }

    public static User userWithoutEmail() {
        Faker faker = new Faker();
        return new User()
                .withEmail("")
                .withPassword(faker.letterify("????????"))
                .withName(faker.name().name());
    }

    public static User userWithoutPassword() {
        Faker faker = new Faker();
        return new User()
                .withEmail(faker.internet().emailAddress())
                .withPassword("")
                .withName(faker.name().name());
    }

    public static User userWithoutName() {
        Faker faker = new Faker();
        return new User()
                .withEmail(faker.internet().emailAddress())
                .withPassword(faker.letterify("????????"))
                .withName("");
    }
}
