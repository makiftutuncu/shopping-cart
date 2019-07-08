package com.github.makiftutuncu.shoppingcart;

public class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        setProduct(product);
        setQuantity(quantity);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        // TODO: Validate
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        // TODO: Validate
        this.quantity = quantity;
    }

    public CartItem withAddedQuantity(int quantity) {
        setQuantity(this.quantity + quantity);
        return this;
    }

    public int totalAmount() {
        return quantity * product.getPrice();
    }

    @Override public String toString() {
        return String.format("%s x %d = %s", product.getTitle(), quantity, MoneyPrinter.print(totalAmount()));
    }
}
