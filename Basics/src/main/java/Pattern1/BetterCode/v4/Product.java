package Pattern1.BetterCode.v4;


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

    private Product(Builder builder) {
        if (builder.getPrice(100.0) > 0) {
            this.price = builder.getPrice(100.0);
        }
        this.name = builder.getName();
        this.description = builder.getDescription();
        this.brand = builder.getBrand();
        this.category = builder.getCategory();
        this.discount = builder.getDiscount();
        this.createdAt = builder.getCreatedAt();
        this.updatedAt = builder.getUpdatedAt();
        this.images = builder.getImages();
    }

    //we'll be call this before the creation of the product object that's why using static keyword
    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder {
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

        public Product build() {
            return new Product(this);
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

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setPrice(double price) {
            this.price = price;
            return this;
        }

        public Builder setBrand(String brand) {
            this.brand = brand;
            return this;
        }

        public Builder setCategory(String category) {
            this.category = category;
            return this;
        }

        public Builder setDiscount(int discount) {
            this.discount = discount;
            return this;
        }

        public Builder setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder setImages(List<String> images) {
            this.images = images;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }
    }
}

