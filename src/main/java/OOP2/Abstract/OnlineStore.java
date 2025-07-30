package OOP2.Abstract;

public class OnlineStore {
    public static void main(String[] args) {
        Product electronics = new Electronics("Smartphone", 5000);
        Product clothing = new Clothing("T-shirt", 500);

        System.out.println("Tax (electronics):" + electronics.calculateTax());
        System.out.println("Discount (electronics):" + electronics.calculateDiscount());
        System.out.println("Shipping cost (clothing):" + clothing.calculateShippingCost());
    }
}
