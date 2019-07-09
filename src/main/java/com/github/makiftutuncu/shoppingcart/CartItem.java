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

    private void setProduct(Product product) {
        // TODO: Validate
        this.product = product;
    }

    private void setQuantity(int quantity) {
        // TODO: Validate
        this.quantity = quantity;
    }

    @Override public String toString() {
        return String.format("%s x %d = %s", product.title(), quantity, MoneyPrinter.print(totalAmount()));
    }
}
