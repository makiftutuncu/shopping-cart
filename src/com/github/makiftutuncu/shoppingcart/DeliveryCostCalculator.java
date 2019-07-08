package com.github.makiftutuncu.shoppingcart;

public class DeliveryCostCalculator {
    private int costPerDelivery;
    private int costPerProduct;

    private static final int FIXED_COST = 299;

    public DeliveryCostCalculator(int costPerDelivery, int costPerProduct) {
        setCostPerDelivery(costPerDelivery);
        setCostPerProduct(costPerProduct);
    }

    public int getCostPerDelivery() {
        return costPerDelivery;
    }

    public void setCostPerDelivery(int costPerDelivery) {
        // TODO: Validate
        this.costPerDelivery = costPerDelivery;
    }

    public int getCostPerProduct() {
        return costPerProduct;
    }

    public void setCostPerProduct(int costPerProduct) {
        // TODO: Validate
        this.costPerProduct = costPerProduct;
    }

    public int calculateFor(ShoppingCart cart) {
        return (cart.numberOfDeliveries() * costPerDelivery) + (cart.numberOfProducts() * costPerProduct) + FIXED_COST;
    }
}
