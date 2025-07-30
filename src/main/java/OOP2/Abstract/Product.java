package OOP2.Abstract;

abstract class Product {
    protected String name;
    protected double price;

    Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    abstract double calculateDiscount();

    abstract double calculateTax();

    abstract double calculateShippingCost();
}
