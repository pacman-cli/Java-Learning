package Pattern1.BetterCode.v3;


import java.util.List;

public class Product {
    private String name;
    private String description;
    private double price;
    private String brand;
    private String category;
    private int discount;
    private String createdAt;
    private String updatedAt;
    private List<String> images;

    Product(Builder builder) {
        if (builder.getPrice(100.0) > 0) {
            this.price = builder.getPrice(100.0);
        }
        this.name = builder.getName();
        this.description = builder.getDescription();
        //..... continues validating
    }
}

