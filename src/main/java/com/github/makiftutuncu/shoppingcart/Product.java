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

    public UUID id() {
        return id;
    }

    public String title() {
        return title;
    }

    public int price() {
        return price;
    }

    public Category category() {
        return category;
    }

    @Override public String toString() {
        return String.format("%s (%s)", title, MoneyPrinter.print(price));
    }

    private void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) throw new IllegalArgumentException("Product title cannot be null or empty!");
        this.title = title;
    }

    private void setPrice(int price) {
        if (price < 1) throw new IllegalArgumentException("Product price must be positive!");
        this.price = price;
    }

    public void setCategory(Category category) {
        if (category == null) throw new IllegalArgumentException("Product category cannot be null!");
        this.category = category;
    }
}
