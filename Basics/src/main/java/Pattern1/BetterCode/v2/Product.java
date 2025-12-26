package Pattern1.BetterCode.v2;


import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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

    Product(HashMap<String, Object> map) {
        if (map.get("price").equals(0)) {
            this.price = (double) map.get("price");
        }
    }
}
