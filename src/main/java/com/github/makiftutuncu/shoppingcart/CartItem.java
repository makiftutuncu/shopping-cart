package com.github.makiftutuncu.shoppingcart;

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
