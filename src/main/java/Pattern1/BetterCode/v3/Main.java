package Pattern1.BetterCode.v3;

public class Main {
    public static void main(String[] args) {
        Builder b = new Builder();
        b.setBrand("Apple");
        b.setName("iphone");
        b.getPrice(100.0);

        Product product = new Product(b);
    }
}
