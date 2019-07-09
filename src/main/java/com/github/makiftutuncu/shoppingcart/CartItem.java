package com.github.makiftutuncu.shoppingcart;

import java.util.Objects;

/**
 * Cart item represents a {@link com.github.makiftutuncu.shoppingcart.Product} that is added to the cart. It contains quantity of the product.
 */
public class CartItem {
    private Product product;
    private int quantity;

    /**
     * Creates a cart item
     *
     * @param product  Product to add to the cart, cannot be null
     * @param quantity Quantity of the product, must be positive
     */
    public CartItem(Product product, int quantity) {
        setProduct(product);
        setQuantity(quantity);
    }

    /**
     * @return Product of the cart item
     */
    public Product product() {
        return product;
    }

    /**
     * @return Quantity of product of the cart item
     */
    public int quantity() {
        return quantity;
    }

    /**
     * Modifies quantity by adding given quantity
     *
     * @param quantity Quantity to add
     *
     * @return Cart item itself after modification
     */
    public CartItem addingQuantity(int quantity) {
        setQuantity(this.quantity + quantity);
        return this;
    }

    /**
     * @return Total amount of the cart item based on unit price x quantity
     */
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
