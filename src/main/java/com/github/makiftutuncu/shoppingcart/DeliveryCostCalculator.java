package com.github.makiftutuncu.shoppingcart;

/**
 * Delivery cost calculator is responsible for calculating a dynamic delivery cost.
 * It depends on number of different categories and number of different products in the cart.
 */
public class DeliveryCostCalculator {
    private int costPerCategory;
    private int costPerProduct;
    private int fixedCost;

    /**
     * Creates a delivery cost calculator
     *
     * @param costPerCategory Unit cost per category, must be positive
     * @param costPerProduct  Unit cost per product, must be positive
     * @param fixedCost       A fixed cost as a base value, must be positive
     */
    public DeliveryCostCalculator(int costPerCategory, int costPerProduct, int fixedCost) {
        setCostPerCategory(costPerCategory);
        setCostPerProduct(costPerProduct);
        setFixedCost(fixedCost);
    }

    /**
     * Calculates delivery cost for given values
     *
     * @param numberOfCategories Number of different categories in the cart, must be positive
     * @param numberOfProducts   Number of different products in the cart, must be positive
     *
     * @return Calculated delivery cost
     */
    public int calculateFor(int numberOfCategories, int numberOfProducts) {
        if (numberOfCategories < 1) throw new IllegalArgumentException("Delivery cost calculator number of categories must be positive!");
        if (numberOfProducts < 1) throw new IllegalArgumentException("Delivery cost calculator number of items must be positive!");
        return (numberOfCategories * costPerCategory) + (numberOfProducts * costPerProduct) + fixedCost;
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
