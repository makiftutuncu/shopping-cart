package com.github.makiftutuncu.shoppingcart;

public class DeliveryCostCalculator {
    private int costPerCategory;
    private int costPerProduct;
    private int fixedCost;

    public DeliveryCostCalculator(int costPerCategory, int costPerProduct, int fixedCost) {
        setCostPerCategory(costPerCategory);
        setCostPerProduct(costPerProduct);
        setFixedCost(fixedCost);
    }

    public int calculateFor(int numberOfCategories, int numberOfItems) {
        if (numberOfCategories < 1) throw new IllegalArgumentException("Delivery cost calculator number of categories must be positive!");
        if (numberOfItems < 1) throw new IllegalArgumentException("Delivery cost calculator number of items must be positive!");
        return (numberOfCategories * costPerCategory) + (numberOfItems * costPerProduct) + fixedCost;
    }

    private void setCostPerCategory(int costPerCategory) {
        if (costPerCategory < 1) throw new IllegalArgumentException("Delivery cost calculator cost per category must be positive!");
        this.costPerCategory = costPerCategory;
    }

    private void setCostPerProduct(int costPerProduct) {
        if (costPerProduct < 1) throw new IllegalArgumentException("Delivery cost calculator cost per product must be positive!");
        this.costPerProduct = costPerProduct;
    }

    private void setFixedCost(int fixedCost) {
        if (fixedCost < 1) throw new IllegalArgumentException("Delivery cost calculator fixed cost must be positive!");
        this.fixedCost = fixedCost;
    }
}
