package com.github.makiftutuncu.shoppingcart;

import java.util.Objects;

public class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        setProduct(product);
        setQuantity(quantity);
    }

    public Product product() {
        return product;
    }

    public int quantity() {
        return quantity;
    }

    public CartItem addingQuantity(int quantity) {
        setQuantity(this.quantity + quantity);
        return this;
    }

    public int totalAmount() {
        return quantity * product.price();
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItem)) return false;

        CartItem ci = (CartItem) o;

        return quantity == ci.quantity && product.equals(ci.product);
    }

    @Override public int hashCode() {
        return Objects.hash(product, quantity);
    }

    @Override public String toString() {
        return String.format("%s x %d = %s", product.title(), quantity, MoneyPrinter.print(totalAmount()));
    }

    private void setProduct(Product product) {
        if (product == null) throw new IllegalArgumentException("Cart item product cannot be null!");
        this.product = product;
    }

    private void setQuantity(int quantity) {
        if (quantity < 1) throw new IllegalArgumentException("Cart item quantity must be positive!");
        this.quantity = quantity;
    }
}
