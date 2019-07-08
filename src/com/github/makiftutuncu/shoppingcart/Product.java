package com.github.makiftutuncu.shoppingcart;

import java.util.UUID;

public class Product {
    private UUID id;
    private String title;
    private int price;
    private Category category;

    public Product(String title, int price, Category category) {
        this.id = UUID.randomUUID();
        setTitle(title);
        setPrice(price);
        setCategory(category);
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        // TODO: Validate
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        // TODO: Validate
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        // TODO: Validate
        this.category = category;
    }

    @Override public String toString() {
        return String.format("%s (%s)", title, MoneyPrinter.print(price));
    }
}
