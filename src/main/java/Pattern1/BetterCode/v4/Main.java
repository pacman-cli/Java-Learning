package Pattern1.BetterCode.v4;

public class Main {
    public static void main(String[] args) {
        Product p = Product.getBuilder()
                .setName("Iphone")
                .setPrice(1000)
                .setBrand("Apple")
                .setCategory("Mobile")
                .setDescription("A brand new Iphone")
                .build();

        //Product product = new Product(b);//we can't call because the constructor is private

    }
}
