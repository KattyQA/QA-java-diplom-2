package orderpackage;

import com.github.javafaker.Faker;

public class OrderGenerator {
    private String[] ingredients;

    public static Order defaultOrder() {

        return new Order(new String[]{"61c0c5a71d1f82001bdaaa6d"});
    }

    public static Order orderWithoutIngredients() {
        return new Order(null);

    }

    public static Order orderWithRandomHash() {
        Faker faker = new Faker();
        return new Order(new String[]{faker.letterify("???????????????????")});
    }
}
