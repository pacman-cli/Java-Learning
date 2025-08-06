package Pattern1.BetterCode.v3;


import java.util.List;

public class Builder {
    private String name;
    private String description;
    private double price;
    private String brand;
    private String category;
    private int discount;
    private String createdAt;
    private String updatedAt;
    private List<String> images;

    Builder() {

    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice(double v) {
        return price;
    }

    public String getBrand() {
        return brand;
    }

    public String getCategory() {
        return category;
    }

    public int getDiscount() {
        return discount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public List<String> getImages() {
        return images;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setName(String name) {
        this.name = name;
    }
}


