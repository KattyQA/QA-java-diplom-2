package orderpackage;

public class OrderGenerator {
    private String[] ingredients;

    public static Order defaultOrder() {
        return new Order(new String[]{"61c0c5a71d1f82001bdaaa6d"});
    }

    public static Order orderWithoutIngredients() {
        return new Order(null);

    }

    public static Order orderWithTwoIngredients() {
        return new Order(new String[]{"61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa72"});
    }

    public static Order orderWithWrongHash() {
        return new Order(new String[]{"1111111111111111"});
    }
}
