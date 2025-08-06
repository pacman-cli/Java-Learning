package OOP2.Abstract;

public class Electronics extends Product {
    Electronics(String name, double price) {
        super(name, price);
    }

    @Override
    double calculateDiscount() {
        return price * 0.1; //10% discount
    }

    @Override
    double calculateTax() {
        return price * 0.15;
    }

    @Override
    double calculateShippingCost() {
        return 10.0;
    }
}
